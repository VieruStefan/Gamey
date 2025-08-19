package org.dis.gateway.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDTO {
   private String price;
   private String title;
   private String url;
   private String platform;
}
