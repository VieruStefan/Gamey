package org.dis.gamedata.service.mapper;

import org.dis.gamedata.model.Product;
import org.dis.gamedata.service.dto.ProductDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
   Product fromDto(ProductDTO productDTO);
   ProductDTO toDto(Product product);
}