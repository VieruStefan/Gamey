package org.dis.master;

import org.apache.kafka.clients.admin.NewTopic;
import org.dis.master.service.KafkaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/diffbot")
public class Diffbot
{
   final static List<String> websites = List.of(
   "https://www.jocurinoi.ro/toate-jocurile%26filter_id=527%26limit=100",
   "https://www.buy2play.ro/categorie-produs/jocuri/jocuri-playstation/jocuri-ps5-noi/",
   "https://altex.ro/jocuri-ps5/cpl/",
   "https://www.lumea-jocurilor.ro/ps5/jatekok",
   "https://www.eneba.com/ro/store/psn-games",
//        "https://www.eneba.com/ro/psn-ea-sportstm-college-football-26-standard-edition-ps5-psn-key-united-states",
//        product
   "https://www.skroutz.ro/c/4306/jocuri-ps5.html",
   "https://www.mobile-zone.ro/jocuri-ps5",
   "https://www.cel.ro/jocuri/platforma-i1090/playstation-5/"
   );
   KafkaService kafkaService;
   
   @Autowired
   public Diffbot(KafkaService kafkaService)
   {
      this.kafkaService = kafkaService;
   }
   
   @GetMapping("/send")
   public String send() throws InterruptedException
   {
      for (String website : websites)
      {
         kafkaService.sendMessage("websites", website);
         Thread.sleep(10000);
      }
      return "hello";
   }
}
