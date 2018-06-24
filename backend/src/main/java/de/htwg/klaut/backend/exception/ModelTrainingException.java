package de.htwg.klaut.backend.exception;

import de.htwg.klaut.backend.model.db.CompositeId;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "model is already in training")
public class ModelTrainingException extends RuntimeException {

    public ModelTrainingException(CompositeId modelId) {
        super("failed to train model with id " + modelId);
    }
}
