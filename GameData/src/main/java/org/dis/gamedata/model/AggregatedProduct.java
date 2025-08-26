package org.dis.gamedata.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.dis.gamedata.service.dto.ProductDTO;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@AllArgsConstructor
public class AggregatedProduct {
   @Id
   private String gameId;
   private int count;
   private List<ProductDTO> documents;
}
