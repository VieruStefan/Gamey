package org.dis.gamedata.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "products")
public class Product {
   @Id
   private String id;
   private String price;
   private String title;
   private String url;
   private String platform;
}