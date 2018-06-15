package de.htwg.klaut.backend.service;

import de.htwg.klaut.backend.exception.OrganizationCreationException;
import de.htwg.klaut.backend.exception.OrganizationNotFoundException;
import de.htwg.klaut.backend.model.db.Organization;
import de.htwg.klaut.backend.model.db.SubscriptionInformation;
import de.htwg.klaut.backend.model.dto.SubscriptionInformationDto;

import java.util.Optional;

public interface IOrganizationService {

    /**
     * Creates a new organization with given name and icon.
     * @param name the name of new organization
     * @param iconUrl the icon url of new organization
     * @return the updated {@Link Organization}
     */
    Organization createOrganization(String name, String iconUrl) throws OrganizationCreationException;

    /**
     * IMPORTANT: This Method result value depends on calling thread.
     * Do not use in threads running background.
     * @return the current organization, but null in case of background thread.
     */
    String getCurrentOrganization();

    /**
     * Sets the given organization as current one.
     * @param organization the organization to set
     */
    void setCurrentOrganization(String organization);

    SubscriptionInformation getSubscription() throws OrganizationNotFoundException;

    SubscriptionInformation updateSubscription(SubscriptionInformation subscriptionInformation) throws OrganizationNotFoundException;

    SubscriptionInformation updateSubscription(SubscriptionInformationDto subscriptionInformationDto);

    Optional<Organization> findByName(String name);
}
