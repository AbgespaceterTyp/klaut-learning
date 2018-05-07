package de.htwg.klaut.backend.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ModelDto implements Serializable {

    private String name;
    private String description;
}
