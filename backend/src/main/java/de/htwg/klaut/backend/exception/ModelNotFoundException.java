package de.htwg.klaut.backend.exception;

import de.htwg.klaut.backend.model.db.CompositeId;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

public class ModelNotFoundException extends HttpClientErrorException {

    public ModelNotFoundException(CompositeId modelId) {
        super(HttpStatus.NOT_FOUND, modelId.toString());
    }
}
