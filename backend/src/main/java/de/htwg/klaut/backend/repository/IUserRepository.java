package de.htwg.klaut.backend.repository;

import de.htwg.klaut.backend.model.CloudUser;
import de.htwg.klaut.backend.model.db.CompositeId;
import org.socialsignin.spring.data.dynamodb.repository.DynamoDBPagingAndSortingRepository;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.socialsignin.spring.data.dynamodb.repository.EnableScanCount;

import java.util.Optional;

@EnableScan
@EnableScanCount
public interface IUserRepository extends DynamoDBPagingAndSortingRepository<CloudUser, CompositeId> {
    Optional<CloudUser> readByEmailAndOrganization(String email, String organization);
}
