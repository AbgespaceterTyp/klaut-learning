package de.htwg.klaut.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "The own user can not be deleted")
public class UnableToDeleteOwnUserException extends RuntimeException {
}
