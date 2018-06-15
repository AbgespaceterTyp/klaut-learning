package de.htwg.klaut.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "failed to create model")
public class OrganizationCreationException extends RuntimeException {

    public OrganizationCreationException(String modelName) {
        super("failed to create model with name " + modelName);
    }
}
