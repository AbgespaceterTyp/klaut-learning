package de.htwg.klaut.backend.model.db;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class SubscriptionConverter implements DynamoDBTypeConverter<String, SubscriptionInformation> {

    private Logger logger = LogManager.getLogger(SubscriptionConverter.class);
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convert(SubscriptionInformation object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            logger.error(e);
        }
        return null;
    }

    @Override
    public SubscriptionInformation unconvert(String object) {
        try {
            return mapper.readValue(object, SubscriptionInformation.class);
        } catch (IOException e) {
            logger.error(e);
        }
        return null;
    }
}
