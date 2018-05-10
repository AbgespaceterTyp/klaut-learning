package de.htwg.klaut.backend.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import de.htwg.klaut.backend.model.db.CompositeId;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;

@DynamoDBTable(tableName = "user")
@Data
@RequiredArgsConstructor
public class User {

    @Id
    private CompositeId compositeId;

    @NonNull
    @DynamoDBAttribute
    private String email;

    @NonNull
    @DynamoDBAttribute
    private String lastName;

    @NonNull
    @DynamoDBAttribute
    private String firstName;

    @NonNull
    @DynamoDBAttribute
    private String passwordHash;
}
