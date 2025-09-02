package org.dis.gamedata.controller;

import org.bson.Document;
import org.dis.gamedata.repository.ProductRepository;
import org.dis.gamedata.model.AggregatedProduct;
import org.dis.gamedata.service.dto.PageableAggregationResult;
import org.dis.gamedata.service.dto.ProductDTO;
import org.dis.gamedata.service.mapper.ProductMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.bind.annotation.*;
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
   public Mono<Page<AggregatedProduct>> findProductsWithDuplicateGameId(
         @RequestParam(required = false) String search,
         @RequestParam(required = false) String platform,
         Pageable pageable) {

      Criteria criteria = new Criteria();
      if (search != null) {
         criteria.and("title").regex(".*" + search + ".*", "i");
      }
      if (platform != null) {
         criteria.and("platform").is(platform);
      }

      MatchOperation initialMatch = Aggregation.match(criteria);
      GroupOperation groupByURLAndPlatform = Aggregation.group("url", "platform")
            .first("title").as("title")
            .first("image").as("image")
            .first("gameId").as("gameId")
            .push(new Document("jobId", "$jobId").append("price", "$price")).as("priceHistory");

      GroupOperation groupByGameId = Aggregation.group("gameId")
            .first("title").as("title")
            .first("image").as("image")
            .push(
                  // Note: We pull from the previous stage's _id field
                  new Document("url", "$_id.url")
                        .append("platform", "$_id.platform")
                        .append("priceHistory", "$priceHistory")
            ).as("sources");

      MatchOperation matchMultipleSources = Aggregation.match(
            Criteria.where("sources.1").exists(true)
      );

      FacetOperation facetOperation = Aggregation.facet()
            .and(
                  Aggregation.skip(pageable.getOffset()),
                  Aggregation.limit(pageable.getPageSize())
            ).as("data")
            .and(
                  Aggregation.count().as("count")
            ).as("totalCount");

      Aggregation aggregation = Aggregation.newAggregation(
            initialMatch,
            groupByURLAndPlatform,
            groupByGameId,
            matchMultipleSources,
            facetOperation
      );

      return reactiveMongoTemplate.aggregate(aggregation,
                  "products",
                  PageableAggregationResult.class)
            .single()
            .map(result -> new PageImpl<>(
                  result.getData(),
                  pageable,
                  result.getTotal()
            ));
   }
}
