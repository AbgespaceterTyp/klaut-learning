package de.htwg.klaut.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No organization image set")
public class ImageNotFoundException extends RuntimeException {
}
