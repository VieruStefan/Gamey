package org.dis.scraper.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ProductDto
{
   @JsonAlias("name")
   private String title;
   @JsonAlias("description")
   private String summary;
   private String link;
   @JsonProperty("image")
   @JsonAlias("image")
   private String image;
   @JsonAlias("price")
   private String price;
   private String primary;
}
