package de.htwg.klaut.backend.exception;

import de.htwg.klaut.backend.model.db.CompositeId;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such organization")
public class OrganizationNotFoundException extends RuntimeException {

    public OrganizationNotFoundException(String organizationKey) {
        super("found no organization for key " + organizationKey);
    }
}
