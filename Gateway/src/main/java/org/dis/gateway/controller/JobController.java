package org.dis.gateway.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.dis.gateway.service.KafkaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/job")
@CrossOrigin("http://localhost:3000")
public class JobController {
   private static final Logger logger = LoggerFactory.getLogger(JobController.class);

   private final ObjectMapper objectMapper = new ObjectMapper();
   final static List<String> websites = List.of(
//            "https://www.jocurinoi.ro/toate-jocurile%26filter_id=527%26limit=32",
//            "https://www.jocurinoi.ro/toate-jocurile&filter_id=519,386,527&page=7",
         "https://www.jocurinoi.ro/ps5&limit=48",
         "https://www.buy2play.ro/categorie-produs/jocuri/jocuri-playstation/jocuri-ps5-noi/",
         "https://altex.ro/jocuri-ps5/cpl/",
         "https://www.lumea-jocurilor.ro/ps5/jatekok",
//         "https://www.eneba.com/ro/store/psn-games",
//        "https://www.eneba.com/ro/psn-ea-sportstm-college-football-26-standard-edition-ps5-psn-key-united-states",
////        product
         "https://www.skroutz.ro/c/4306/jocuri-ps5.html",
         "https://www.mobile-zone.ro/jocuri-ps5",
         "https://www.cel.ro/jocuri/platforma-i1090/playstation-5/"
   );
   private final KafkaService kafkaService;

   public JobController(KafkaService kafkaService) {
      this.kafkaService = kafkaService;
   }

   @GetMapping("/create-job")
   public Map<String, String> createJob() throws InterruptedException {
      String jobId = UUID.randomUUID().toString();
      logger.info("New job created with ID: {}", jobId);
      Map<String, String> response = new HashMap<>();
      response.put("message", "Job creation process started.");
      response.put("jobId", jobId);

      for (String website : websites) {
         kafkaService.sendMessage("scraping.request.site-discovery.v1", website);
      }
      return response;
   }
}
