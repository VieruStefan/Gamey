package org.dis.reducer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Service
public class ReducerService
{
   ObjectMapper objectMapper = new ObjectMapper();
   private final Sinks.Many<String> updateSink = Sinks.many().multicast().onBackpressureBuffer();
   
   @KafkaListener(topics = "web-scraping-list-output", groupId = "website-scraping")
   public void listenOnOutputList(String list)
   {
      try
      {
         Object jsonObject = objectMapper.readValue(list, Object.class);
         String jsonResponse = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
         System.out.println("Received list " + jsonResponse);
         updateSink.tryEmitNext(jsonResponse);
      }
      catch (JsonProcessingException e)
      {
         System.err.println("Cannot process product JSON: " + e.getMessage());
      }
   }
   
   @KafkaListener(topics = "web-scraping-product-output", groupId = "website-scraping")
   public void listenOnOutputProduct(String product)
   {
      try
      {
         Object jsonObject = objectMapper.readValue(product, Object.class);
         String jsonResponse = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
         System.out.println("Received product " + jsonResponse);
         updateSink.tryEmitNext(jsonResponse);
         
      } catch (JsonProcessingException e) {
         System.err.println("Cannot process product JSON: " + e.getMessage());
         // Optionally, you could push an error event to the sink
         // productUpdateSink.tryEmitError(e);
      }
   }
   
   public Flux<String> getUpdateStream()
   {
      return updateSink.asFlux();
   }
}
