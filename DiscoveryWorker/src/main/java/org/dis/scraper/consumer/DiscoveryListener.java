package org.dis.scraper.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.dis.scraper.dto.DiffbotResponseDto;
import org.dis.scraper.dto.ListObjectDto;
import org.dis.scraper.dto.ProductDTO;
import org.dis.scraper.service.KafkaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class DiscoveryListener {
   private static final Logger logger = LoggerFactory.getLogger(DiscoveryListener.class);
   private final KafkaService kafkaService;
   private final ObjectMapper objectMapper = new ObjectMapper();
   private static int repeats = 0;

   public DiscoveryListener(KafkaService kafkaService) {
      this.kafkaService = kafkaService;
   }

   @KafkaListener(topics = "api.site-discovery", groupId = "website-scraping")
   public void sendRequest(String site) {
      site = URLDecoder.decode(site, StandardCharsets.UTF_8);
      logger.info("Scraping website: {} with API List.", site);
      kafkaService.sendMessage("api.requests", site);
   }

   @KafkaListener(topics = "api.responses", groupId = "website-scraping")
   public void processResponse(String sites) {
      logger.debug("Processing response: {}.", sites);
      try {
         DiffbotResponseDto response = objectMapper.readValue(sites, DiffbotResponseDto.class);
         logger.debug("Received Diffbot items: {}", response.getObjects().getFirst().getItems());
         logger.debug("Size of items: {}", response.getObjects().getFirst().getItems().size());
         logger.debug("Size of products: {}", response.getObjects().getFirst().getProducts().size());
         List<ProductDTO> products = response.getObjects().getFirst().getProducts();
         List<ListObjectDto.Item> items = response.getObjects().getFirst().getItems();
         logger.debug("Successfully deserialized {} products.", products.size());
         for (ProductDTO dto : products) {
            int i = products.indexOf(dto);
            if (items != null && !items.isEmpty() && i < items.size()) {
               String image = items.get(i).getImage();
               logger.debug("Adding image: {}", image);
               logger.debug("Decoded version of the image: {}", URLDecoder.decode(image, StandardCharsets.UTF_8));
               dto.setImage(URLDecoder.decode(image, StandardCharsets.UTF_8));
            }
            logger.info("Processing product {} with API Details", dto.getTitle());
            String productJson = objectMapper.writeValueAsString(dto);
            kafkaService.sendMessage("gamedata.products", productJson);
            logger.info("Response sent to gamedata.products: {}", productJson);
         }
      } catch (Exception e) {
         logger.error(e.getMessage());
      }
   }
}
