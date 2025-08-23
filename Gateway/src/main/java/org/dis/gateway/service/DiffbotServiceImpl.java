package org.dis.gateway.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dis.gateway.repository.ProductRepository;
import org.dis.gateway.service.dto.*;
import org.dis.gateway.service.mapper.DiffbotMapper;
import org.dis.gateway.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Service
public class DiffbotServiceImpl
      implements DiffbotService {
   private static final Logger logger = LoggerFactory.getLogger(DiffbotServiceImpl.class);
   private final ObjectMapper objectMapper;
   @Value("${diffbot.api.token}")
   private String apiToken;
   private final String basepath = "https://api.diffbot.com/v3/";
   private final KafkaServiceImpl kafkaService;
   private final WebClient client;
   private final DiffbotMapper diffbotMapper;
   private final ProductRepository productRepository;

   public DiffbotServiceImpl(KafkaServiceImpl kafkaService,
                             WebClient webClient,
                             ObjectMapper objectMapper,
                             DiffbotMapper diffbotMapper,
                             ProductRepository productRepository) {
      this.kafkaService = kafkaService;
      this.client = webClient;
      this.objectMapper = objectMapper;
      this.diffbotMapper = diffbotMapper;
      this.productRepository = productRepository;
   }

   @Override
   @Transactional
   public void sendRequest(String api, String resource) {
      if (!api.equals("list"))
      {
         logger.error("Invalid api received. Only \"list\" API is supported.");
      }
      final String requestId = UUID.randomUUID().toString().substring(0, 8);
      String uri = UriComponentsBuilder.fromUriString(basepath)
            .path(api)
            .queryParam("token", apiToken)
            .queryParam("url", resource)
            .build()
            .encode()
            .toUriString();

      logger.info("[{}] Starting request for api='{}', resource='{}'", requestId, api, resource);

      Retry retrySpec = Retry.fixedDelay(Long.MAX_VALUE, Duration.ofSeconds(15))
            .filter(throwable -> throwable instanceof WebClientResponseException &&
                  ((WebClientResponseException) throwable).getStatusCode() == HttpStatus.TOO_MANY_REQUESTS)
            .doBeforeRetry(retrySignal ->
                  logger.warn("[{}] Received 429 Too Many Requests. Retrying in 15 seconds... (Attempt #{})",
                        requestId, retrySignal.totalRetries() + 1));

      this.client.get().uri(uri)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(String.class)
            .retryWhen(retrySpec)
            .subscribe(
                  response -> {
                     try {
                        if (api.equals("list")) {
                           ListResponseDto listResponseDto = objectMapper.readValue(response, ListResponseDto.class);
                           List<ProductDTO> products = listResponseDto.getObjects().getFirst().getProducts();
                           for (ProductDTO productDto : products) {
                              Product product = diffbotMapper.fromDto(productDto);
                              productRepository.save(product).subscribe();
                           }
                           kafkaService.sendMessage("api." + api + ".responses.v1", response);
                           logger.info("[{}] Successfully processed and sent to Kafka for api='{}'", requestId, api);
                        } else {
                           throw new RuntimeException("Cannot deserialize response from " + api);
                        }
                     }
                     catch (JsonProcessingException e)
                     {
                        throw new RuntimeException(e);
                     }
                  },
                  error -> {
                     logger.error("[{}] Failed to process request for api='{}' after all retries. Final error: {}",
                           requestId, api, error.getMessage());
                  }
            );
   }
}
