package de.htwg.klaut.backend.model.db;

import lombok.Data;

@Data
public class Word2VecParams implements IModelParams {

    private int minWordFrequency;
    private int iterations;
    private int layerSize;
    private int seed;
    private int windowSize;
}
