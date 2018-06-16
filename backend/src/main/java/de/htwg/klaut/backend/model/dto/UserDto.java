package de.htwg.klaut.backend.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {

    private String email;

    private String firstName;

    private String lastName;
}
