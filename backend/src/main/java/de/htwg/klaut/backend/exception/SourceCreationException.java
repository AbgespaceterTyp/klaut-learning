package de.htwg.klaut.backend.exception;

import de.htwg.klaut.backend.model.db.CompositeId;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "failed to create source")
public class SourceCreationException extends RuntimeException {

    public SourceCreationException(CompositeId modelId) {
        super("failed to create source for model with id " + modelId);
    }
}
