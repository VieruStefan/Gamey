package org.dis.gateway.service.impl;

import org.dis.gateway.service.DiffbotService;
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
public class DiffbotServiceImpl
implements DiffbotService
{
   private static final Logger logger = LoggerFactory.getLogger(DiffbotServiceImpl.class);
   @Value("${diffbot.api.token}")
   private String apiToken;
   private final String basepath = "https://api.diffbot.com/v3/";
   private final KafkaServiceImpl kafkaService;
   private final WebClient client;

   public DiffbotServiceImpl(KafkaServiceImpl kafkaService, WebClient webClient)
   {
      this.kafkaService = kafkaService;
      this.client = webClient;
   }

   @Override
   public void sendRequest(String api, String resource) {
      final String requestId = UUID.randomUUID().toString().substring(0, 8);
      String uri = UriComponentsBuilder.fromUriString(basepath)
            .path(api)
            .queryParam("token", apiToken)
            .queryParam("url", resource)
            .build()
            .toUriString();

      logger.info("[{}] Starting request for api='{}', resource='{}'", requestId, api, resource);

      Retry retrySpec = Retry.fixedDelay(5, Duration.ofSeconds(10))
            .filter(throwable -> throwable instanceof WebClientResponseException &&
                  ((WebClientResponseException) throwable).getStatusCode() == HttpStatus.TOO_MANY_REQUESTS)
            .doBeforeRetry(retrySignal ->
                  logger.warn("[{}] Received 429 Too Many Requests. Retrying in 10 seconds... (Attempt #{})",
                        requestId, retrySignal.totalRetries() + 1));

      this.client.get().uri(uri)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(String.class)
            .retryWhen(retrySpec)
            .subscribe(
                  response -> {
                     kafkaService.sendMessage("api." + api + ".responses.v1", response);
                     logger.info("[{}] Successfully processed and sent to Kafka for api='{}'", requestId, api);
                  },
                  error -> {
                     logger.error("[{}] Failed to process request for api='{}' after all retries. Final error: {}",
                           requestId, api, error.getMessage());
                  }
            );
   }
}
