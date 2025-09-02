package org.dis.gamedata.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.dis.gamedata.model.Job;
import org.dis.gamedata.repository.JobRepository;
import org.dis.gamedata.service.KafkaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
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
         "https://www.jocurinoi.ro/toate-jocurile?limit=100&page=1&filter_id=527",
         "https://www.jocurinoi.ro/toate-jocurile?limit=100&page=2&filter_id=527",
         "https://www.jocurinoi.ro/toate-jocurile?limit=100&page=3&filter_id=527",
         "https://www.jocurinoi.ro/toate-jocurile?limit=100&page=4&filter_id=527",
         "https://www.jocurinoi.ro/toate-jocurile?limit=100&page=5&filter_id=527",
         "https://www.skroutz.ro/c/4306/jocuri-ps5.html",
         "https://www.skroutz.ro/c/4306/jocuri-ps5.html?page=2",
         "https://www.skroutz.ro/c/4306/jocuri-ps5.html?page=3",
         "https://www.skroutz.ro/c/4306/jocuri-ps5.html?page=4",
         "https://www.mobile-zone.ro/jocuri",
         "https://www.mobile-zone.ro/jocuri?p=2",
         "https://www.cel.ro/jocuri/",
         "https://www.cel.ro/jocuri/0a-2",
         "https://www.buy2play.ro/categorie-produs/jocuri/?per_page=96",
         "https://www.buy2play.ro/categorie-produs/jocuri/page/2/?per_page=96"
   );
   private final KafkaService kafkaService;
   private final ObjectMapper objectMapper;
   private final JobRepository jobRepository;

   public JobController(KafkaService kafkaService, ObjectMapper objectMapper, JobRepository jobRepository) {
      this.kafkaService = kafkaService;
      this.objectMapper = objectMapper;
      this.jobRepository = jobRepository;
   }

   @PostMapping
   public Map<String, String> createJob() {
      String jobId = UUID.randomUUID().toString();
      logger.info("New job created with ID: {}", jobId);
      Map<String, String> response = new HashMap<>();
      response.put("message", "Job creation process started.");
      response.put("jobId", jobId);

      for (String website : websites) {
         Map<String, String> request = new HashMap<>();
         request.put("website", website);
         request.put("jobId", jobId);
         try {
            kafkaService.sendMessage("api.site-discovery", objectMapper.writeValueAsString(request));
         }
         catch (Exception e){
            logger.error(e.getMessage());
         }
      }
      Job job = new Job();
      job.setId(jobId);
      job.setDate(LocalDate.now());
      jobRepository.save(job).subscribe();
      return response;
   }

   @GetMapping("/test")
   public String test() {
      return "test works!";
   }
}
