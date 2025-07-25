package org.dis.master.controller;

import org.dis.master.service.KafkaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/diffbot")
@CrossOrigin("localhost:3000")
public class DiffbotController
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
   
   @Autowired
   KafkaService kafkaService;
   
   @GetMapping("/list")
   public ResponseEntity<String> list() throws InterruptedException
   {
      for (String website : websites)
      {
         kafkaService.sendMessage("web-scraping-list", website);
         System.out.println("Sent " + website + " to be scraped.");
         Thread.sleep(10000);
      }
      return new ResponseEntity<>("All websites are to be scraped with DiffBot List", HttpStatus.OK);
   }
   
   @GetMapping("/product")
   public ResponseEntity<String> product()
   {
      kafkaService.sendMessage("web-scraping-product",
                               "https://www.eneba.com/ro/psn-ea-sportstm-college-football-26-standard-edition-ps5-psn-key-united-states");
      return new ResponseEntity<>("Sent product to scraping with DiffBot Product.", HttpStatus.OK);
   }
}
