package org.dis.gateway.controller;

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
@CrossOrigin(origins = {"http://gamey.gcp:3000", "http://34.171.39.26:3000"})
public class JobController {
   private static final Logger logger = LoggerFactory.getLogger(JobController.class);

   final static List<String> websites = List.of(
         "https://www.jocurinoi.ro/toate-jocurile&limit=100&filter_id=527",
         "https://www.skroutz.ro/c/4306/jocuri-ps5.html",
         "https://www.mobile-zone.ro/jocuri",
         "https://www.cel.ro/jocuri/",
         "https://www.lumea-jocurilor.ro/jocuri",
         "https://altex.ro/jocuri/cpl/",
         "https://www.buy2play.ro/categorie-produs/jocuri/?per_page=96"
   );
   private final KafkaService kafkaService;

   public JobController(KafkaService kafkaService) {
      this.kafkaService = kafkaService;
   }

   @GetMapping("/create-job")
   public Map<String, String> createJob() {
      String jobId = UUID.randomUUID().toString();
      logger.info("New job created with ID: {}", jobId);
      Map<String, String> response = new HashMap<>();
      response.put("message", "Job creation process started.");
      response.put("jobId", jobId);

      for (String website : websites) {
         kafkaService.sendMessage("scraping.request.site-discovery.v1", website);
         break;
      }
      return response;
   }
}
