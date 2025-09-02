package org.dis.scraper.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.dis.scraper.dto.ApiResponse;
import org.dis.scraper.dto.DiffbotResponseDTO;
import org.dis.scraper.dto.ProductDTO;
import org.dis.scraper.service.KafkaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DiscoveryListener {
   private static final Logger logger = LoggerFactory.getLogger(DiscoveryListener.class);
   private final KafkaService kafkaService;
   private final ObjectMapper objectMapper = new ObjectMapper();

   public DiscoveryListener(KafkaService kafkaService) {
      this.kafkaService = kafkaService;
   }

   @KafkaListener(topics = "api.site-discovery", groupId = "website-scraping")
   public void sendRequest(String payload) {
      logger.info("Received the payload to be scraped {}", payload);
      kafkaService.sendMessage("api.requests", payload);
   }

   @KafkaListener(topics = "api.responses", groupId = "website-scraping")
   public void processResponse(String payload) {
      logger.debug("Processing response: {}.", payload);
      try {
         ApiResponse apiResponse = objectMapper.readValue(payload, ApiResponse.class);
         DiffbotResponseDTO response = objectMapper.readValue(apiResponse.getProducts(), DiffbotResponseDTO.class);
         logger.debug("Received Diffbot items: {}", response.getObjects().getFirst().getItems());
         logger.debug("Size of items: {}", response.getObjects().getFirst().getItems().size());
         logger.debug("Size of products: {}", response.getObjects().getFirst().getProducts().size());
         List<ProductDTO> products = response.getObjects().getFirst().getProducts();
         logger.debug("Successfully deserialized {} products.", products.size());
         for (ProductDTO dto : products) {
            logger.info("Processing product {} with API Details", dto.getTitle());
            String productJson = objectMapper.writeValueAsString(dto);
            Map<String, Object> map = new HashMap<>();
            map.put("jobId", apiResponse.getJobId());
            map.put("product", productJson);
            kafkaService.sendMessage("gamedata.products", objectMapper.writeValueAsString(map));
            logger.info("Response sent to gamedata.products: {}", objectMapper.writeValueAsString(map));
         }
      } catch (Exception e) {
         logger.error(e.getMessage());
      }
   }
}
