package org.dis.gamedata.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
class PriceHistoryItem {
   private String jobId;
   private String price;
   private String scrapedAt;
}
