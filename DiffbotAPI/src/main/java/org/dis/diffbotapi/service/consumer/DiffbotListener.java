package org.dis.diffbotapi.service.consumer;

import org.dis.diffbotapi.service.DiffbotService;
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

   public DiffbotListener(DiffbotService diffbotService) {
      this.diffbotService = diffbotService;
   }

   @KafkaListener(topics = "api.requests", groupId = "website-scraping")
   public void processApiRequest(String payload) {
      payload = URLDecoder.decode(payload, StandardCharsets.UTF_8);
      processRequest("list", payload);
   }

   public void processRequest(String api, String product) {
      logger.info("Scraping with {}: {}", api, product);
      diffbotService.sendRequest(api, product);
   }
}
