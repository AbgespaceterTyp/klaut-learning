package de.htwg.klaut.backend.model;

import lombok.Data;

@Data
public class Word2VecParams implements IModelParams {

    private int minWordFrequency;
    private int iterations;
    private int layerSize;
    private int seed;
    private int windowSize;
}
