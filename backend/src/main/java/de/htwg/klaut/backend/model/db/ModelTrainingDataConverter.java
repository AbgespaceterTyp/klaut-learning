package de.htwg.klaut.backend.model.db;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;

public class ModelTrainingDataConverter implements DynamoDBTypeConverter<String, Set<ModelTrainingData>> {

    private Logger logger = LogManager.getLogger(ModelTrainingDataConverter.class);
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convert(Set<ModelTrainingData> modelTrainingData) {
        try {
            return mapper.writeValueAsString(modelTrainingData);
        } catch (JsonProcessingException e) {
            logger.error(e);
        }
        return null;
    }

    @Override
    public Set<ModelTrainingData> unconvert(String s) {
        /*try {
            return mapper.readValues(s, );
        } catch (IOException e) {
            logger.error(e);
        }*/
        return null;
    }
}
