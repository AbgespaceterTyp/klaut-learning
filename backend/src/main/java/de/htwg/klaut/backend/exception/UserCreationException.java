package de.htwg.klaut.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR,
        reason = "Failed to create user. Email address is already in use")
public class UserCreationException extends RuntimeException {
}
