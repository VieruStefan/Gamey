package org.dis.gateway.consumer;

import org.dis.gateway.controller.JobController;
import org.dis.gateway.service.DiffbotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class DiffbotListener {
   private static final Logger logger = LoggerFactory.getLogger(DiffbotListener.class);
   private final DiffbotService diffbotService;

   public DiffbotListener(DiffbotService diffbotService) {
      this.diffbotService = diffbotService;
   }

   @KafkaListener(topics = "api.product.requests.v1", groupId = "diffbot-worker-group")
   public void processProductApiRequest(String product) throws InterruptedException {
      Thread.sleep(4000);
      processApiRequest("product", product);
   }

   @KafkaListener(topics = "api.list.requests.v1", groupId = "diffbot-worker-group")
   public void processListApiRequest(String site) throws InterruptedException {
      Thread.sleep(2000);
      processApiRequest("list", site);
   }

   public void processApiRequest(String api, String product) throws InterruptedException {
      logger.info("Scraping with {}: {}", api, product);
      diffbotService.sendRequest(api, product);
   }
}
