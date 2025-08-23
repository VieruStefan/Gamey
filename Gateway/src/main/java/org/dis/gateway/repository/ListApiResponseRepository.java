package org.dis.gateway.repository;

import org.dis.gateway.model.ListItem;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
@Deprecated(forRemoval = true)
public interface ListApiResponseRepository extends ReactiveMongoRepository<ListItem, String> {
}
