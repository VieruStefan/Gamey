package org.dis.scraper.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.dis.scraper.dto.DiffbotResponseDto;
import org.dis.scraper.dto.ProductDto;
import org.dis.scraper.service.DiffbotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
public class DiffbotServiceImpl
implements DiffbotService
{
   private static final Logger logger = LoggerFactory.getLogger(DiffbotServiceImpl.class);
   private final ObjectMapper objectMapper = new ObjectMapper();
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
      jsonData ->
      {
         try
         {
            DiffbotResponseDto response = objectMapper.readValue(jsonData, DiffbotResponseDto.class);
            List<ProductDto> products = response.getObjects().get(0).getItems();
            logger.debug("Successfully deserialized {} products.", products.size());
            int i = 0;
            for (ProductDto dto : products) {
               logger.debug("Processing product: {}", dto.getTitle());
               kafkaService.sendMessage("scraping.request.product-detail.v1", dto.getLink());
               logger.info("Response sent to web-scraping-{}-output", api);
               Thread.sleep(5000);
               if (++i == 3)
               {
                  break;
               }
            }
         } catch (Exception e) {
             logger.error(e.getMessage());
         }
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
