package org.dis.gateway.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@Document(collection = "products")
public class Product {
   @Id
   private String id;
   private OfferPriceDetails offerPriceDetails;
   private String title;
   private String pageUrl;
   private String text;
   @Data
   public static class OfferPriceDetails {
      private BigDecimal amount;
      private String text;
   }
}