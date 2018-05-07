package de.htwg.klaut.backend.exception;

import de.htwg.klaut.backend.model.db.CompositeId;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="No such model")
public class ModelNotFoundException extends RuntimeException {

    public ModelNotFoundException(CompositeId modelId) {
        super("found no model with id " + modelId);
    }
}
