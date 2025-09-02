package org.dis.gamedata.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@AllArgsConstructor
public class AggregatedProduct {
   @Id
   private String gameId;
   private String title;
   private String image;
   private List<Source> sources;
}