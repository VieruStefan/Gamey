package org.dis.gateway.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductResponseDto {
   RequestDto request;
   List<ProductObjectDto> objects;
}
