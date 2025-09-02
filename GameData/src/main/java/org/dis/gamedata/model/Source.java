package org.dis.gamedata.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
class Source {
   private String url;
   private String platform;
   private List<PriceHistoryItem> priceHistory;
}
