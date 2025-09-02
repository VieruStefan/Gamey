package org.dis.worker.detail.service.mapper;

import com.github.slugify.Slugify;
import org.dis.worker.detail.model.Product;
import org.dis.worker.detail.service.GameIdGenerator;
import org.dis.worker.detail.service.GameIdGeneratorWithSlug;
import org.dis.worker.detail.service.dto.ProductDTO;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ProductMapper {
   ProductDTO toDto(Product product);

   @Mapping(target = "jobId", ignore = true)
   Product toEntity(ProductDTO productDto);

   @AfterMapping
   default void normalizePlatform(@MappingTarget Product product, ProductDTO productDto) {
      String rawPlatform = productDto.getPlatform();
      String finalPlatform = "UNKNOWN";
      if (rawPlatform == null) {
         product.setPlatform(finalPlatform);
         return;
      }
      String upperCasePlatform = rawPlatform.toUpperCase();

      if (upperCasePlatform.contains("PLAYSTATION") || (upperCasePlatform.contains("PS") && upperCasePlatform.length() < 6)) {
         finalPlatform = "Playstation";
      } else if (upperCasePlatform.contains("XBOX") || upperCasePlatform.contains("X1") || upperCasePlatform.contains("XSX")) {
         finalPlatform = "XBOX";
      } else if (upperCasePlatform.contains("SWITCH") || upperCasePlatform.contains("NINTENDO")) {
         finalPlatform = "Switch";
      } else if (upperCasePlatform.contains("PC") || upperCasePlatform.contains("CALCULATOR")) {
         finalPlatform = "PC";
      }
      product.setPlatform(finalPlatform);
   }

   @AfterMapping
   default void generateIdForEntity(@MappingTarget Product product) {
      product.setId(UUID.randomUUID().toString());
//      product.setGameId(GameIdGenerator.generateGameId(product.getTitle()));
      product.setGameId(GameIdGeneratorWithSlug.generate(product.getTitle()));
   }

}
