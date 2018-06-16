package de.htwg.klaut.backend.model.db;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserDto {

    public UserDto() {
    }

    @NotNull
    private String email;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String password;
}
