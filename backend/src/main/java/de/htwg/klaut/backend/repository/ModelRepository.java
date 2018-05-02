package de.htwg.klaut.backend.repository;

import de.htwg.klaut.backend.model.db.Model;
import org.socialsignin.spring.data.dynamodb.repository.DynamoDBPagingAndSortingRepository;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.socialsignin.spring.data.dynamodb.repository.EnableScanCount;

@EnableScan
@EnableScanCount
public interface ModelRepository extends DynamoDBPagingAndSortingRepository<Model, String> {
}
