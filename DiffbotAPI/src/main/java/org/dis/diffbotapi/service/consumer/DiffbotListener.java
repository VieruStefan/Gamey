package org.dis.diffbotapi.service.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.dis.diffbotapi.dto.ApiRequest;
import org.dis.diffbotapi.service.DiffbotService;
import org.dis.diffbotapi.service.KafkaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Service
public class DiffbotListener {
   private static final Logger logger = LoggerFactory.getLogger(DiffbotListener.class);
   private final DiffbotService diffbotService;
   private final ObjectMapper objectMapper = new ObjectMapper();

   public DiffbotListener(DiffbotService diffbotService) {
      this.diffbotService = diffbotService;
   }

   @KafkaListener(topics = "api.requests", groupId = "website-scraping")
   public void processApiRequest(String payload) {
      try {
         ApiRequest apiRequest = objectMapper.readValue(payload, ApiRequest.class);
         apiRequest.setWebsite(URLDecoder.decode(apiRequest.getWebsite(), StandardCharsets.UTF_8));
         logger.info("[{}]Scraping website: {}", apiRequest.getJobId(), apiRequest.getWebsite());
         diffbotService.sendRequest(apiRequest.getJobId(), apiRequest.getWebsite());
      }
      catch (Exception e) {
         logger.error(e.getMessage());
      }
   }
}
