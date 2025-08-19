package org.dis.gateway.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ListItemDto {
   String title;
   String summary;
   String date;
   String link;
   String image;
}
