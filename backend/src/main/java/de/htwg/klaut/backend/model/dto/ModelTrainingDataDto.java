package de.htwg.klaut.backend.model.dto;

import de.htwg.klaut.backend.model.db.ModelTrainingData;
import lombok.Data;

import java.util.Date;

@Data
public class ModelTrainingDataDto {

    private String modelUrl;
    private Date lastTrainingStart;
    private Date lastTrainingEnd;

    public ModelTrainingDataDto(ModelTrainingData trainingData) {
        setModelUrl(trainingData.getModelUrl());
        setLastTrainingStart(trainingData.getLastTrainingStart());
        setLastTrainingEnd(trainingData.getLastTrainingEnd());
    }
}
