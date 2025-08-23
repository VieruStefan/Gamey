package org.dis.gamedata.controller;

import org.dis.gamedata.repository.ProductRepository;
import org.dis.gamedata.service.dto.ProductDTO;
import org.dis.gamedata.service.mapper.ProductMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductsController {
   private final ProductRepository productRepository;
   private final ProductMapper productMapper;

   public ProductsController(ProductRepository productRepository, ProductMapper productMapper) {
      this.productRepository = productRepository;
      this.productMapper = productMapper;
   }

   @GetMapping
   public List<ProductDTO> getProducts() {
      return productRepository.findAll().collectList().block().stream().map(productMapper::toDto).toList();
   }
}
