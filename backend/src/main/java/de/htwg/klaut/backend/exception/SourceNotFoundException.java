package de.htwg.klaut.backend.exception;

import de.htwg.klaut.backend.model.db.CompositeId;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such source")
public class SourceNotFoundException extends RuntimeException {

    public SourceNotFoundException(String sourceUrl) {
        super("found no source at " + sourceUrl);
    }

    public SourceNotFoundException(CompositeId modelId) {
        super("found no source files for model with id " + modelId);
    }
}
