package org.dis.worker.detail.service.impl;

import org.dis.worker.detail.service.DiffbotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class DiffbotServiceImpl
implements DiffbotService
{
   private static final Logger logger = LoggerFactory.getLogger(DiffbotServiceImpl.class);
   
   @Value("${diffbot.api.token}")
   private String apiToken;
   String basepath = "https://api.diffbot.com/v3/";
   KafkaServiceImpl kafkaService;
   
   public DiffbotServiceImpl(KafkaServiceImpl kafkaService)
   {
      this.kafkaService = kafkaService;
   }
   
   @Override
   public void sendRequest(String api, String resource)
   {
      String uri = UriComponentsBuilder.fromUriString(basepath)
                                       .path(api)
                                       .queryParam("token", apiToken)
                                       .queryParam("url", resource)
                                       .build()
                                       .toUriString();
      logger.debug("Sending request to {}", uri);
      WebClient client = WebClient.create();
      client.get().uri(uri)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(String.class)
            .subscribe(
      response ->
      {
         kafkaService.sendMessage("scraping.result.product-detail.v1", response);
         logger.info("Response sent to web-scraping-{}-output", api);
      }, error ->
      {
         logger.error("Error sending response to web-scraping-{}-output", api, error);
      }, () ->
      {
         logger.info("Api call done");
      }
      );
   }
}
