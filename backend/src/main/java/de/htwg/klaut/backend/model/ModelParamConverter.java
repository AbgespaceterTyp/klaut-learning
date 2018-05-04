package de.htwg.klaut.backend.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class ModelParamConverter implements DynamoDBTypeConverter<String, IModelParams> {

    private Logger logger = LogManager.getLogger(ModelParamConverter.class);
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convert(IModelParams modelParams) {
        String result = null;
        try {
            result = mapper.writeValueAsString(modelParams);
        } catch (JsonProcessingException e) {
            logger.error(e);
        }
        return result;
    }

    @Override
    public IModelParams unconvert(String s) {
        IModelParams modelParam = null;
        try {
            // TODO LG allow other param types too
            modelParam = mapper.readValue(s, Word2VecParams.class);
        } catch (IOException e) {
            logger.error(e);
        }
        return modelParam;
    }
}
