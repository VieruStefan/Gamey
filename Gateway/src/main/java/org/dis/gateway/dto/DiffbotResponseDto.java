package org.dis.gateway.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class DiffbotResponseDto
{
   private Object request;
   private List<ListObjectDto> objects;
}
