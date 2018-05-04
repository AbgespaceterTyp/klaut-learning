package de.htwg.klaut.backend.service;

import de.htwg.klaut.backend.model.Organization;
import de.htwg.klaut.backend.repository.IOrganizationRepository;
import org.springframework.stereotype.Service;

@Service
public class OrganizationService implements IOrganizationService {

    private IOrganizationRepository organizationRepository;

    public OrganizationService(IOrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    @Override
    public Organization createOrganization(String name, String iconUrl) {
        return organizationRepository.save(new Organization(name, iconUrl));
    }
}
