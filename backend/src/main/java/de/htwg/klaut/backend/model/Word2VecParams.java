package de.htwg.klaut.backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Word2VecParams implements ModelParams {

    private int minWordFrequency;
    private int iterations;
    private int layerSize;
    private int seed;
    private int windowSize;
}
