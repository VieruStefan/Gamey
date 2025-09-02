package org.dis.worker.detail.model;

import com.mongodb.lang.Nullable;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "products")
public class Product {
   @Id
   private String id;
   private String price;
   private String title;
   @Indexed(unique = true)
   private String url;
   private String platform;
   private String gameId;
   @Nullable
   private String image;
   private String jobId;
}