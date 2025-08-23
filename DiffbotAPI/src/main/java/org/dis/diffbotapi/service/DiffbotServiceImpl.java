package org.dis.diffbotapi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.UUID;

@Service
public class DiffbotServiceImpl implements DiffbotService {
   private static final Logger logger = LoggerFactory.getLogger(DiffbotServiceImpl.class);
   @Value("${diffbot.api.token}")
   private String apiToken;
   private final String basepath = "https://api.diffbot.com/v3/";
   private final KafkaService kafkaService;
   private final WebClient client;

   public DiffbotServiceImpl(KafkaService kafkaService,
                             WebClient webClient) {
      this.kafkaService = kafkaService;
      this.client = webClient;
   }

   @Override
   public void sendRequest(String api, String resource) {
      if (!api.equals("list")) {
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

      Retry retrySpec = Retry.fixedDelay(Long.MAX_VALUE, Duration.ofSeconds(20))
            .filter(throwable -> throwable instanceof WebClientResponseException &&
                  ((WebClientResponseException) throwable).getStatusCode() == HttpStatus.TOO_MANY_REQUESTS)
            .doBeforeRetry(retrySignal ->
                  logger.warn("[{}] Received 429 Too Many Requests. Retrying in 20 seconds... (Attempt #{})",
                        requestId, retrySignal.totalRetries() + 1));

      this.client.get().uri(uri)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(String.class)
            .retryWhen(retrySpec)
            .subscribe(
                  response -> {
                     kafkaService.sendMessage("api.responses", response);
                     logger.info("[{}] Successfully processed and sent to api.response: '{}'", requestId, response);
                  },
                  error -> {
                     logger.error("[{}] Failed to process request for api='{}' after all retries. Final error: {}",
                           requestId, api, error.getMessage());
                  }
            );
   }
}
