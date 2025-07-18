package org.dis.scraper;

import org.apache.kafka.clients.admin.NewTopic;
import org.dis.scraper.service.DiffbotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.annotation.ApplicationScope;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class Scrape
{
   @Autowired
   DiffbotService diffbotService;
   
   @KafkaListener(topics = "websites", groupId = "website-scraping")
   public String listen(String website)
   {
      diffbotService.sendRequest("list", website);
      
      System.out.println("Scraping website: " + website);
      return website;
   }
}
