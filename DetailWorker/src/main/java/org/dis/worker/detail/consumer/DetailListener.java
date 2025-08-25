package org.dis.worker.detail.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dis.worker.detail.model.Product;
import org.dis.worker.detail.repository.ProductRepository;
import org.dis.worker.detail.service.dto.ProductDTO;
import org.dis.worker.detail.service.mapper.ProductMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class DetailListener {
   private static final Logger logger = LoggerFactory.getLogger(DetailListener.class);
   private final ProductMapper productMapper;
   private final ObjectMapper objectMapper;
   private final ProductRepository productRepository;

   public DetailListener(ProductMapper productMapper, ObjectMapper objectMapper, ProductRepository productRepository) {
      this.productMapper = productMapper;
      this.objectMapper = objectMapper;
      this.productRepository = productRepository;
   }

   @KafkaListener(topics = "gamedata.products", groupId = "website-scraping")
   public void receiveProduct(String productJson) {
      try {
         ProductDTO productDto = objectMapper.readValue(productJson, ProductDTO.class);
         logger.debug("Deserialized product: {}.", productDto);
         Product product = productMapper.toEntity(productDto);
         logger.info("Added product {} to database.", product.toString());
         productRepository.save(product).subscribe();
      }
      catch (JsonProcessingException e) {
         logger.error("Error parsing product JSON: {}", productJson);
         logger.error(e.getMessage());
      }
      catch (Exception e) {
         logger.error(e.getMessage());
      }
   }
}
