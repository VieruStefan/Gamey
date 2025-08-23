package org.dis.worker.detail.service.mapper;

import org.dis.worker.detail.model.Product;
import org.dis.worker.detail.service.dto.ProductDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
   ProductDTO toDto(Product product);
   Product toEntity(ProductDTO productDto);
}
