package org.dis.scraper.consumer;

import org.dis.scraper.service.impl.DiffbotServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ScrapeListener
{
   private static final Logger logger = LoggerFactory.getLogger(ScrapeListener.class);
   DiffbotServiceImpl diffbotService;
   
   public ScrapeListener(DiffbotServiceImpl diffbotService)
   {
      this.diffbotService = diffbotService;
   }
   
   @KafkaListener(topics = "web-scraping-list", groupId = "website-scraping")
   public ResponseEntity<String> listenOnList(String website)
   {
      logger.info("Scraping website: {} with API List.", website);
      diffbotService.sendRequest("list", website);
      
      return new ResponseEntity<>("Sent " + website + " to scraping with DiffBot List.", HttpStatus.OK);
   }
   
   @KafkaListener(topics = "web-scraping-product", groupId = "website-scraping")
   public ResponseEntity<String> listenOnProduct(String product)
   {
      logger.info("Scraping website: {} with API Product.", product);
      diffbotService.sendRequest("product", product);
      
      return new ResponseEntity<>("Sent " + product + " to scraping with DiffBot Product.", HttpStatus.OK);
   }
}
