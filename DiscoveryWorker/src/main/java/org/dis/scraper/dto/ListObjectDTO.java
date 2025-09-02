package org.dis.scraper.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ListObjectDTO {
   private String type;
   private String pageUrl;
   private List<String> nextPages;
   private String resolvedPageUrl;
   private String nextPage;
   private String title;
   private List<Item> items;
   private List<ProductDTO> products;

   @Data
   @JsonIgnoreProperties(ignoreUnknown = true)
   public static class Item {
      private String image;
   }
}