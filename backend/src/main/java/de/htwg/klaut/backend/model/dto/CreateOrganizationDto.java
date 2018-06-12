package de.htwg.klaut.backend.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CreateOrganizationDto {

    @NotNull
    private String name;

    @NotNull
    private String adminEmail;

    @NotNull
    private String adminPassword;

}
