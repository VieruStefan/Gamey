package org.dis.master.dto;

import lombok.Data;

import java.util.List;

@Data
public class DiffbotResponseDto
{
   private Object request;
   private List<ListObjectDto> objects;
}
