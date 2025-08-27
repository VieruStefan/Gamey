package org.dis.scraper.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ProductDTO
{
   private String price;
   private String title;
   private String url;
   private String platform;
   private String image;
}
