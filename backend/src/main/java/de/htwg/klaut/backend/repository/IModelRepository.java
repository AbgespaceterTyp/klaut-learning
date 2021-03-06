package de.htwg.klaut.backend.repository;

import de.htwg.klaut.backend.model.db.CompositeId;
import de.htwg.klaut.backend.model.db.Model;
import org.socialsignin.spring.data.dynamodb.repository.DynamoDBPagingAndSortingRepository;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.socialsignin.spring.data.dynamodb.repository.EnableScanCount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;

@EnableScan
@EnableScanCount
public interface IModelRepository extends DynamoDBPagingAndSortingRepository<Model, CompositeId> {

    Collection<Model> findByOrganization(String organization);
}
