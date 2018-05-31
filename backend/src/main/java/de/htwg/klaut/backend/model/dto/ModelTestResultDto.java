package de.htwg.klaut.backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collection;
import java.util.LinkedList;

@Data
@AllArgsConstructor
public class ModelTestResultDto {
    private Collection<String> results = new LinkedList<>();
}
