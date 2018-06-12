package de.htwg.klaut.backend.repository;

import de.htwg.klaut.backend.model.db.Organization;
import org.socialsignin.spring.data.dynamodb.repository.DynamoDBPagingAndSortingRepository;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.socialsignin.spring.data.dynamodb.repository.EnableScanCount;

import java.util.Optional;

@EnableScan
@EnableScanCount
public interface IOrganizationRepository extends DynamoDBPagingAndSortingRepository<Organization, String> {

    Organization findByKey(String key);

    Optional<Organization> findByName(String name);

}
