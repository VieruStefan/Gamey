package org.dis.gamedata.controller;

import org.dis.gamedata.model.Product;
import org.dis.gamedata.repository.ProductRepository;
import org.dis.gamedata.model.AggregatedProduct;
import org.dis.gamedata.service.dto.ProductDTO;
import org.dis.gamedata.service.mapper.ProductMapper;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/api/products")
public class ProductsController {
   private final ProductRepository productRepository;
   private final ProductMapper productMapper;
   private final ReactiveMongoTemplate reactiveMongoTemplate;

   public ProductsController(ProductRepository productRepository, ProductMapper productMapper, ReactiveMongoTemplate reactiveMongoTemplate) {
      this.productRepository = productRepository;
      this.productMapper = productMapper;
      this.reactiveMongoTemplate = reactiveMongoTemplate;
   }

   @GetMapping
   public Flux<ProductDTO> getProducts() {
      return productRepository.findAll()
            .map(productMapper::toDto);
   }

   @GetMapping("{id}")
   public Mono<ProductDTO> getProduct(@PathVariable String id) {
      return productRepository.findById(id)
            .map(productMapper::toDto);
   }

   @GetMapping("/aggregation/")
   public Flux<AggregatedProduct> findProductsWithDuplicateGameId() {
      GroupOperation groupOperation = Aggregation.group("gameId")
            .count().as("count")
            .push("$$ROOT").as("documents");

      MatchOperation matchOperation = Aggregation.match(
            Criteria.where("count").gte(2)
      );

      Aggregation aggregation = Aggregation.newAggregation(
            groupOperation,
            matchOperation
      );

      return reactiveMongoTemplate.aggregate(aggregation, Product.class, AggregatedProduct.class);
   }
}
