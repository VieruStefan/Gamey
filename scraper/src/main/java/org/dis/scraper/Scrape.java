package org.dis.scraper;

import org.dis.scraper.service.DiffbotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class Scrape
{
   @Autowired
   DiffbotService diffbotService;
   
   @KafkaListener(topics = "web-scraping-list", groupId = "website-scraping")
   public ResponseEntity<String> listenOnList(String website)
   {
      System.out.println("Scraping website: " + website + " with API List.");
      diffbotService.sendRequest("list", website);
      
      return new ResponseEntity<>("Sent " + website + " to scraping with DiffBot List.", HttpStatus.OK);
   }
   
   @KafkaListener(topics = "web-scraping-product", groupId = "website-scraping")
   public ResponseEntity<String> listenOnProduct(String product)
   {
      System.out.println("Scraping website: " + product + " with API Product.");
      diffbotService.sendRequest("product", product);
      
      return new ResponseEntity<>("Sent " + product + " to scraping with DiffBot Product.", HttpStatus.OK);
   }
}
