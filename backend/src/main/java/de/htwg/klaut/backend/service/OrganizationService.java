package de.htwg.klaut.backend.service;

import de.htwg.klaut.backend.exception.OrganizationCreationException;
import de.htwg.klaut.backend.exception.OrganizationNotFoundException;
import de.htwg.klaut.backend.model.db.Organization;
import de.htwg.klaut.backend.model.db.SubscriptionInformation;
import de.htwg.klaut.backend.model.db.SubscriptionLevel;
import de.htwg.klaut.backend.model.dto.SubscriptionInformationDto;
import de.htwg.klaut.backend.repository.IOrganizationRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class OrganizationService implements IOrganizationService {

    private IOrganizationRepository organizationRepository;

    private static ThreadLocal<String> currentOrganization = new ThreadLocal<>();

    public OrganizationService(IOrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    @Override
    public Organization createOrganization(String name, String iconUrl) throws OrganizationCreationException {
        log.debug("creating organization", name);

        final Optional<Organization> existingOrganization = organizationRepository.findByName(name);
        if(existingOrganization.isPresent()){
            throw new OrganizationCreationException(name);
        }

        Organization organization = Organization.builder()
                .iconUrl(iconUrl)
                .name(name)
                .subscriptionInformation(new SubscriptionInformation())
                .build();
        return organizationRepository.save(organization);
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
    public SubscriptionInformation getSubscription() throws OrganizationNotFoundException {
        return get(currentOrganization.get()).getSubscriptionInformation();
    }

    @Override
    public SubscriptionInformation updateSubscription(SubscriptionInformation subscriptionInformation) throws OrganizationNotFoundException {
        final Organization organization = get(currentOrganization.get());
        organization.setSubscriptionInformation(subscriptionInformation);
        organizationRepository.save(organization);
        return  subscriptionInformation;
    }

    @Override
    public SubscriptionInformation updateSubscription(SubscriptionInformationDto subscriptionInformationDto) throws OrganizationNotFoundException {
        final Organization organization = get(currentOrganization.get());
        SubscriptionInformation subscriptionInformationToUpdate = organization.getSubscriptionInformation();
        if (null == subscriptionInformationToUpdate) {
            subscriptionInformationToUpdate = new SubscriptionInformation();
        }
        subscriptionInformationToUpdate.setSubscriptionLevel(subscriptionInformationDto.getSubscriptionLevel());
        subscriptionInformationToUpdate.setRemainingTrainings(subscriptionInformationToUpdate.getRemainingTrainings() + subscriptionInformationDto.getRemainingTrainings());

        switch (subscriptionInformationDto.getSubscriptionLevel()){
            case FREE:
                subscriptionInformationToUpdate.setMaxTrainings(1);
                break;
            case COOPER:
                subscriptionInformationToUpdate.setMaxTrainings(5);
                break;
            case SILVER:
                subscriptionInformationToUpdate.setMaxTrainings(10);
                break;
            case GOLD:
                subscriptionInformationToUpdate.setMaxTrainings(25);
                break;
            case PLATIN:
                subscriptionInformationToUpdate.setMaxTrainings(50);
                break;
        }

        if (SubscriptionLevel.FREE.equals(subscriptionInformationToUpdate.getSubscriptionLevel())) {
            subscriptionInformationToUpdate.setMaxUploadInKb(SubscriptionInformation.FREE_SUBS_MAX_UPLOAD_IN_KB);
        } else {
            subscriptionInformationToUpdate.setMaxUploadInKb(SubscriptionInformation.NORMAL_SUBS_MAX_UPLOAD_IN_KB);
        }

        organization.setSubscriptionInformation(subscriptionInformationToUpdate);
        organizationRepository.save(organization);
        return subscriptionInformationToUpdate;
    }

    @Override
    public Optional<Organization> findByName(String name) {
        return organizationRepository.findByName(name);
    }

    private Organization get(String organizationKey) {
        if (StringUtils.isEmpty(organizationKey)) {
            throw new OrganizationNotFoundException("Unknown organization");
        }
        final Organization organization = organizationRepository.findByKey(organizationKey);
        if (null == organization) {
            throw new OrganizationNotFoundException(organizationKey);
        }
        return organization;
    }
}
