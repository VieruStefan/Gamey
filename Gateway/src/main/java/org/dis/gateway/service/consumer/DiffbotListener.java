package org.dis.gateway.service.consumer;

import org.dis.gateway.service.DiffbotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
public class DiffbotListener {
   private static final Logger logger = LoggerFactory.getLogger(DiffbotListener.class);
   private final DiffbotService diffbotService;

   public DiffbotListener(DiffbotService diffbotService) {
      this.diffbotService = diffbotService;
   }

   @KafkaListener(
         topics = {"api.product.requests.v1", "api.list.requests.v1"},
         groupId = "diffbot-worker-group"
   )
   public void processApiRequest(
         String payload,
         @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) throws InterruptedException {

      System.out.println("Processing message from topic: " + topic);

      switch (topic) {
         case "api.product.requests.v1":
            Thread.sleep(10000);
            processRequest("product", payload);
            break;
         case "api.list.requests.v1":
            Thread.sleep(5000);
            processRequest("list", payload);
            break;
         default:
            System.err.println("Received message on an unknown topic: " + topic);
            break;
      }
   }

   public void processRequest(String api, String product) {
      logger.info("Scraping with {}: {}", api, product);
      diffbotService.sendRequest(api, product);
   }
}
