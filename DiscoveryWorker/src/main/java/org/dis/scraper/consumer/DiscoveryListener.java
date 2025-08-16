package org.dis.scraper.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.dis.scraper.dto.DiffbotResponseDto;
import org.dis.scraper.dto.ProductDto;
import org.dis.scraper.service.DiffbotService;
import org.dis.scraper.service.KafkaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscoveryListener
{
   private static final Logger logger = LoggerFactory.getLogger(DiscoveryListener.class);
   private final DiffbotService diffbotService;
   private final KafkaService kafkaService;
   private final ObjectMapper objectMapper = new ObjectMapper();

   public DiscoveryListener(DiffbotService diffbotService, KafkaService kafkaService)
   {
      this.diffbotService = diffbotService;
      this.kafkaService = kafkaService;
   }
   
   @KafkaListener(topics = "scraping.request.site-discovery.v1", groupId = "website-scraping")
   public void sendRequest(String site)
   {
      logger.info("Scraping website: {} with API List.", site);
//      diffbotService.sendRequest("list", site);
      kafkaService.sendMessage("api.list.requests.v1", site);
   }

   @KafkaListener(topics = "api.list.responses.v1", groupId = "website-scraping")
   public void processResponse(String sites)
   {
      logger.info("Scraping website: {} with API List.", sites);
      try
      {
         DiffbotResponseDto response = objectMapper.readValue(sites, DiffbotResponseDto.class);
         List<ProductDto> products = response.getObjects().get(0).getItems();
         logger.debug("Successfully deserialized {} products.", products.size());
         int i = 0;
         for (ProductDto dto : products) {
            logger.debug("Processing product: {}", dto.getTitle());
            kafkaService.sendMessage("scraping.request.product-detail.v1", dto.getLink());
            logger.info("Response sent to scraping.request.product-detail.v1");
            if (++i == 3)
            {
               break;
            }
         }
      } catch (Exception e) {
         logger.error(e.getMessage());
      }
   }
}
