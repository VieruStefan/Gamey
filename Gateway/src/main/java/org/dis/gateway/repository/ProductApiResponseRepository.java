package org.dis.gateway.repository;

import org.dis.gateway.model.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductApiResponseRepository extends ReactiveMongoRepository<Product, String> {
}
