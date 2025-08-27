package org.dis.worker.detail.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDTO {
   private String id;
   private String price;
   private String title;
   private String url;
   private String platform;
   private String gameId;
   private String image;
}
