package de.htwg.klaut.backend.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class Word2VecParams implements ModelParams {

    private int minWordFrequency;
    private int iterations;
    private int layerSize;
    private int seed;
    private int windowSize;
}
