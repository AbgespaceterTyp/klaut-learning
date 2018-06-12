package de.htwg.klaut.backend.model.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class SearchOrganizationKeyResponseDto {

    @NonNull
    private String organizationKey;
}
