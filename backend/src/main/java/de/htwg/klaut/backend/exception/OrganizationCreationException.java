package de.htwg.klaut.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "failed to create organization")
public class OrganizationCreationException extends RuntimeException {

    public OrganizationCreationException(String name) {
        super("failed to create organization with name " + name);
    }
}
