package org.dis.master.dto;

import lombok.Data;

import java.util.List;

@Data
public class ListObjectDto {
   private String diffbotUri;
   private String icon;
   private String pageUrl;
   private String type;
   private String title;
   private List<ProductDto> items;
}