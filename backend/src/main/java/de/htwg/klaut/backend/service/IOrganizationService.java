package de.htwg.klaut.backend.service;

import de.htwg.klaut.backend.exception.OrganizationCreationException;
import de.htwg.klaut.backend.exception.OrganizationNotFoundException;
import de.htwg.klaut.backend.model.db.Organization;
import de.htwg.klaut.backend.model.db.SubscriptionInformation;
import de.htwg.klaut.backend.model.dto.SubscriptionInformationDto;
import de.htwg.klaut.backend.model.dto.SubscriptionRenewDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Optional;

public interface IOrganizationService {

    /**
     * Creates a new organization with given name and icon.
     * @param name the name of new organization
     * @return the updated {@Link Organization}
     */
    Organization createOrganization(String name) throws OrganizationCreationException;

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

    SubscriptionInformation renewSubscription(SubscriptionRenewDto newSubscriptionDto);

    Optional<Organization> findByName(String name);

    /**
     * Uploads the given image to the data source and replaces the current one if present
     * @param image
     */
    void changeImage(MultipartFile image);

    InputStream loadImage();
}
