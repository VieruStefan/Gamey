package org.dis.master.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
public class ProductDto
{
   private String link;
   
   @JsonAlias("name")
   private String title;
   
   @JsonProperty("image")
   @JsonAlias("image")
   private String image;
   
   @JsonAlias("description")
   private String summary;
   
   @JsonAlias("price")
   private String price;
   
   private String primary;
   
   private String date;
}
