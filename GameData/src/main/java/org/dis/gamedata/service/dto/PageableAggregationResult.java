package org.dis.gamedata.service.dto;

import lombok.Data;
import org.dis.gamedata.model.AggregatedProduct;

import java.util.List;

@Data
public class PageableAggregationResult {
   private List<AggregatedProduct> data;
   private List<TotalCount> totalCount;

   public long getTotal() {
      return totalCount != null && !totalCount.isEmpty() ? totalCount.getFirst().getCount() : 0;
   }
}

@Data
class TotalCount {
   private long count;
}