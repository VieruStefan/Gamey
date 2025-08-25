package org.dis.scraper.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ListObjectDto {
   private String type;
   private String pageUrl;
   private List<String> nextPages;
   private String resolvedPageUrl;
   private String nextPage;
   private String title;
   private List<Object> items;
   private List<ProductDTO> products;
}