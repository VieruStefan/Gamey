package org.dis.gateway.service.mapper;

import org.dis.gateway.service.dto.ListItemDto;
import org.dis.gateway.service.dto.ProductObjectDto;
import org.dis.gateway.model.ListItem;
import org.dis.gateway.model.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring") // Tells MapStruct to create a Spring Bean
public interface DiffbotMapper {
   Product fromDto(ProductObjectDto productResponseDto);
   ListItem fromDto(ListItemDto itemDto);
}