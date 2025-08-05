package org.dis.reducer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

@Service
public class ReducerService
{
   private static final Logger logger = LoggerFactory.getLogger(ReducerService.class);
   private final KafkaService kafkaService;
   ObjectMapper objectMapper = new ObjectMapper();
   private final Sinks.Many<String> updateSink = Sinks.many().multicast().onBackpressureBuffer();
   
   public ReducerService(KafkaService kafkaService)
   {
      this.kafkaService = kafkaService;
   }
   
   @KafkaListener(topics = "web-scraping-list-output", groupId = "website-scraping")
   public void listenOnOutputList(String list)
   {
      try
      {
         Object jsonObject = objectMapper.readValue(list, Object.class);
         String jsonResponse = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
         logger.info("Received list {}", jsonResponse);
         kafkaService.sendMessage("web-scraping-list-to-products", jsonResponse);
      }
      catch (JsonProcessingException e)
      {
         logger.error("Cannot process list JSON: {}", e.getMessage());
      }
   }
   
   public void listenOnOutputProduct(String product)
   {
      try
      {
         Object jsonObject = objectMapper.readValue(product, Object.class);
         String jsonResponse = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
         logger.info("Received product {}", jsonResponse);
         updateSink.tryEmitNext(jsonResponse);
         
      } catch (JsonProcessingException e) {
         logger.error("Cannot process product JSON: {}", e.getMessage());
         // Optionally, you could push an error event to the sink
         // productUpdateSink.tryEmitError(e);
      }
   }
   
   public Sinks.Many<String> getUpdateSink()
   {
      return updateSink;
   }
}
