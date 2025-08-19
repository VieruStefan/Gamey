package org.dis.gateway.service.mapper;

import org.dis.gateway.service.dto.ListItemDto;
import org.dis.gateway.service.dto.ProductDTO;
import org.dis.gateway.service.dto.ProductObjectDto;
import org.dis.gateway.model.ListItem;
import org.dis.gateway.model.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DiffbotMapper {
   Product fromDto(ProductDTO productDTO);
   ListItem fromDto(ListItemDto itemDto);
}