package de.htwg.klaut.backend.service;

import de.htwg.klaut.backend.model.db.Organization;
import de.htwg.klaut.backend.model.db.SubscriptionInformation;
import de.htwg.klaut.backend.repository.IOrganizationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrganizationService implements IOrganizationService {

    private IOrganizationRepository organizationRepository;

    private static ThreadLocal<String> currentOrganization = new ThreadLocal<>();

    public OrganizationService(IOrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    @Override
    public Organization createOrganization(String name, String iconUrl) {
        // TODO set correct organization key
        return organizationRepository.save(new Organization(name, iconUrl, name.trim().toLowerCase(), new SubscriptionInformation()));
    }


    @Override
    public String getCurrentOrganization() {
        return currentOrganization.get();
    }

    @Override
    public void setCurrentOrganization(String organization) {
        currentOrganization.set(organization);
    }

    @Override
    public SubscriptionInformation getSubscription() {
        // TODO handle exceptions when no organization found (should never happen;) )
        final Organization organization = organizationRepository.findByKey(currentOrganization.get());
        return organization.getSubscriptionInformation();
    }
}
