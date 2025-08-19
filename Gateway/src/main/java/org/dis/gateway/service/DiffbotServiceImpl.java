package org.dis.gateway.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dis.gateway.service.dto.ListItemDto;
import org.dis.gateway.service.dto.ListResponseDto;
import org.dis.gateway.service.dto.ProductObjectDto;
import org.dis.gateway.service.dto.ProductResponseDto;
import org.dis.gateway.service.mapper.DiffbotMapper;
import org.dis.gateway.model.ListItem;
import org.dis.gateway.model.Product;
import org.dis.gateway.repository.ListApiResponseRepository;
import org.dis.gateway.repository.ProductApiResponseRepository;
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
   private final ListApiResponseRepository listApiResponseRepository;
   private final ProductApiResponseRepository productApiResponseRepository;

   public DiffbotServiceImpl(KafkaServiceImpl kafkaService, WebClient webClient, ObjectMapper objectMapper, DiffbotMapper diffbotMapper, ListApiResponseRepository listApiResponseRepository, ProductApiResponseRepository productApiResponseRepository) {
      this.kafkaService = kafkaService;
      this.client = webClient;
      this.objectMapper = objectMapper;
      this.diffbotMapper = diffbotMapper;
      this.listApiResponseRepository = listApiResponseRepository;
      this.productApiResponseRepository = productApiResponseRepository;
   }

   @Override
   @Transactional
   public void sendRequest(String api, String resource) {
      final String requestId = UUID.randomUUID().toString().substring(0, 8);
      String uri = UriComponentsBuilder.fromUriString(basepath)
            .path(api)
            .queryParam("token", apiToken)
            .queryParam("url", resource)
            .build()
            .encode()
            .toUriString();

      logger.info("[{}] Starting request for api='{}', resource='{}'", requestId, api, resource);

      Retry retrySpec = Retry.fixedDelay(5, Duration.ofSeconds(30))
            .filter(throwable -> throwable instanceof WebClientResponseException &&
                  ((WebClientResponseException) throwable).getStatusCode() == HttpStatus.TOO_MANY_REQUESTS)
            .doBeforeRetry(retrySignal ->
                  logger.warn("[{}] Received 429 Too Many Requests. Retrying in 30 seconds... (Attempt #{})",
                        requestId, retrySignal.totalRetries() + 1));

      this.client.get().uri(uri)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(String.class)
            .retryWhen(retrySpec)
            .subscribe(
                  response -> {
                     try {
                        if (api.equals("product")) {
                           ProductResponseDto productResponseDto = objectMapper.readValue(response, ProductResponseDto.class);
                           ProductObjectDto productObjectDto = productResponseDto.getObjects().getFirst();
                           Product product = diffbotMapper.fromDto(productObjectDto);
                           productApiResponseRepository.save(product).subscribe();
                        } else if (api.equals("list")) {
                           ListResponseDto listResponseDto = objectMapper.readValue(response, ListResponseDto.class);
                           List<ListItemDto> listItemDto = listResponseDto.getObjects().getFirst().getItems();
                           for (ListItemDto item : listItemDto) {
                              ListItem listItem = diffbotMapper.fromDto(item);
                              listApiResponseRepository.save(listItem).subscribe();
                           }
                        } else {
                           throw new RuntimeException("Cannot deserialize response from " + api);
                        }
                        kafkaService.sendMessage("api." + api + ".responses.v1", response);
                        logger.info("[{}] Successfully processed and sent to Kafka for api='{}'", requestId, api);
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
