package org.dis.gateway.service.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Deprecated(forRemoval = true)
public class ProductObjectDto {
   @JsonAlias("diffbotUri")
   String id;
   OfferPriceDetailsDto offerPriceDetails;
   String title;
   String pageUrl;
   String text;

   @Data
   @JsonIgnoreProperties(ignoreUnknown = true)
   public static class OfferPriceDetailsDto {
      BigDecimal amount;
      String text;
   }
}
