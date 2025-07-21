package org.dis.reducer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class Reducer
{
   ObjectMapper objectMapper = new ObjectMapper();
   
   @KafkaListener(topics = "web-scraping-list-output", groupId = "website-scraping")
   public ResponseEntity<String> listenOnOutputList(String list)
   {
      try
      {
         Object jsonObject = objectMapper.readValue(list, Object.class);
         String jsonResponse = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
         System.out.println("Received list " + jsonResponse);
         return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
      }
      catch (JsonProcessingException e)
      {
         return new ResponseEntity<>("Cannot process response.", HttpStatus.BAD_REQUEST);
      }
   }
   
   @KafkaListener(topics = "web-scraping-product-output", groupId = "website-scraping")
   public ResponseEntity<String> listenOnOutputProduct(String product)
   {
      try
      {
         Object jsonObject = objectMapper.readValue(product, Object.class);
         String jsonResponse = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
         System.out.println("Received product " + jsonResponse);
         return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
      }
      catch (JsonProcessingException e)
      {
         return new ResponseEntity<>("Cannot process response.", HttpStatus.BAD_REQUEST);
      }
   }
   
}
