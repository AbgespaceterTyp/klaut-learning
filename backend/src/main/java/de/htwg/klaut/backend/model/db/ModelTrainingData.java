package de.htwg.klaut.backend.model.db;

import lombok.Data;

import java.util.Date;

@Data
public class ModelTrainingData {

    private String modelUrl;
    private Date lastTrainingStart;
    private Date lastTrainingEnd;
}
