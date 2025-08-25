package org.dis.gamedata.controller;

import org.dis.gamedata.service.KafkaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/job")
public class JobController {
   private static final Logger logger = LoggerFactory.getLogger(JobController.class);

   final static List<String> websites = List.of(
         "https://www.lumea-jocurilor.ro/jocuri",
         "https://www.lumea-jocurilor.ro/jocuri?p=2",
         "https://www.lumea-jocurilor.ro/jocuri?p=3",
         "https://www.lumea-jocurilor.ro/jocuri?p=4",
         "https://www.lumea-jocurilor.ro/jocuri?p=5",
         "https://www.jocurinoi.ro/toate-jocurile",
         "https://www.jocurinoi.ro/toate-jocurile?page=2&filter_id=527",
         "https://www.jocurinoi.ro/toate-jocurile?page=3&filter_id=527",
         "https://www.jocurinoi.ro/toate-jocurile?page=4&filter_id=527",
         "https://www.jocurinoi.ro/toate-jocurile?page=5&filter_id=527",
         "https://www.skroutz.ro/c/4306/jocuri-ps5.html",
         "https://www.mobile-zone.ro/jocuri",
         "https://www.mobile-zone.ro/jocuri?p=2",
         "https://www.cel.ro/jocuri/",
         "https://www.cel.ro/jocuri/0a-2",
         "https://www.buy2play.ro/categorie-produs/jocuri/",
         "https://www.buy2play.ro/categorie-produs/jocuri/page/2/"
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
         kafkaService.sendMessage("api.site-discovery", website);
      }
      return response;
   }

   @GetMapping("/test")
   public String test() {
      return "test works!";
   }
}
