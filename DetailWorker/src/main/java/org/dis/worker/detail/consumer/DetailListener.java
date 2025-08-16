package org.dis.worker.detail.consumer;

import org.dis.worker.detail.service.DiffbotService;
import org.dis.worker.detail.service.KafkaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class DetailListener {
   private static final Logger logger = LoggerFactory.getLogger(DetailListener.class);
   private final DiffbotService diffbotService;
   private final KafkaService kafkaService;

   public DetailListener(DiffbotService diffbotService, KafkaService kafkaService) {
      this.diffbotService = diffbotService;
      this.kafkaService = kafkaService;
   }

   @KafkaListener(topics = "scraping.request.product-detail.v1", groupId = "website-scraping")
   public void sendRequest(String product) {
      logger.info("Sending product: {} to Product API.", product);
//      diffbotService.sendRequest("product", product);
      kafkaService.sendMessage("api.product.requests.v1", product);
   }

   @KafkaListener(topics = "api.product.responses.v1", groupId = "website-scraping")
   public void processResponse(String product) {
      logger.info("Scraped product: {}", product);
//      diffbotService.sendRequest("product", product);
      kafkaService.sendMessage("scraping.result.product-detail.v1", product);
   }
}
