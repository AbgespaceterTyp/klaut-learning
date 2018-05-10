package de.htwg.klaut.backend.service;

import de.htwg.klaut.backend.model.Organization;

public interface IOrganizationService {

    Organization createOrganization(String name, String iconUrl);

    String getCurrentOrganization();

    void setCurrentOrganization(String organization);
}
