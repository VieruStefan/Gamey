package org.dis.gateway.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "list_items")
public class ListItem {
   @Id
   private String id;
   private String title;
   private String summary;
   private String date;
   private String link;
   private String image;
}
