package org.dis.reducer.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

@Service
public class AggregatorListener
{
   private static final Logger logger = LoggerFactory.getLogger(AggregatorListener.class);
   ObjectMapper objectMapper = new ObjectMapper();
   private final Sinks.Many<String> updateSink = Sinks.many().replay().latest();


   @KafkaListener(topics = "scraping.result.product-detail.v1", groupId = "website-scraping")
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
