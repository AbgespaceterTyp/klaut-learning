package de.htwg.klaut.backend.service;

import de.htwg.klaut.backend.exception.ImageNotFoundException;
import de.htwg.klaut.backend.exception.OrganizationCreationException;
import de.htwg.klaut.backend.exception.OrganizationNotFoundException;
import de.htwg.klaut.backend.model.db.Organization;
import de.htwg.klaut.backend.model.db.SubscriptionInformation;
import de.htwg.klaut.backend.model.db.SubscriptionLevel;
import de.htwg.klaut.backend.model.dto.SubscriptionInformationDto;
import de.htwg.klaut.backend.model.dto.SubscriptionRenewDto;
import de.htwg.klaut.backend.repository.IOrganizationRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Optional;

@Service
@Slf4j
public class OrganizationService implements IOrganizationService {

    private IOrganizationRepository organizationRepository;

    private IS3StorageService storageService;

    private static ThreadLocal<String> currentOrganization = new ThreadLocal<>();

    public OrganizationService(IOrganizationRepository organizationRepository, IS3StorageService storageService) {
        this.organizationRepository = organizationRepository;
        this.storageService = storageService;
    }

    @Override
    public Organization createOrganization(String name) throws OrganizationCreationException {
        log.debug("creating organization", name);

        final Optional<Organization> existingOrganization = organizationRepository.findByName(name);
        if (existingOrganization.isPresent()) {
            throw new OrganizationCreationException(name);
        }

        Organization organization = Organization.builder()
                .name(name)
                .iconUrl("")
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
        return subscriptionInformation;
    }

    @Override
    public SubscriptionInformation renewSubscription(SubscriptionRenewDto subscriptionRenewDto) throws OrganizationNotFoundException {
        final Organization organization = get(currentOrganization.get());
        SubscriptionInformation subscriptionInformationToUpdate = organization.getSubscriptionInformation();
        if (null == subscriptionInformationToUpdate) {
            subscriptionInformationToUpdate = new SubscriptionInformation();
        }

        switch (subscriptionRenewDto.getSubscriptionLevel()) {
            case FREE:
                subscriptionInformationToUpdate.setMaxTrainings(1);
                break;
            case COPPER:
                subscriptionInformationToUpdate.setMaxTrainings(5);
                subscriptionInformationToUpdate.setRemainingTrainings(subscriptionInformationToUpdate.getRemainingTrainings() + 10);
                break;
            case SILVER:
                subscriptionInformationToUpdate.setMaxTrainings(10);
                subscriptionInformationToUpdate.setRemainingTrainings(subscriptionInformationToUpdate.getRemainingTrainings() + 30);
                break;
            case GOLD:
                subscriptionInformationToUpdate.setMaxTrainings(25);
                subscriptionInformationToUpdate.setRemainingTrainings(subscriptionInformationToUpdate.getRemainingTrainings() + 100);
                break;
            case PLATIN:
                subscriptionInformationToUpdate.setMaxTrainings(50);
                subscriptionInformationToUpdate.setRemainingTrainings(subscriptionInformationToUpdate.getRemainingTrainings() + 500);
                break;
        }
        subscriptionInformationToUpdate.setSubscriptionLevel(subscriptionRenewDto.getSubscriptionLevel());

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

    @Override
    public void changeImage(MultipartFile image) {
        Optional<String> imageUri = storageService.addImage(image, getCurrentOrganization());
        if (imageUri.isPresent()) {
            Organization organization = organizationRepository.findByKey(getCurrentOrganization());
            organization.setIconUrl(imageUri.get());
            organizationRepository.save(organization);
        }
    }

    @Override
    public InputStream loadImage() {
        Organization organization = organizationRepository.findByKey(getCurrentOrganization());
        if (StringUtils.isEmpty(organization.getIconUrl())) {
            throw new ImageNotFoundException();
        }
        Optional<InputStream> sourceFile = storageService.getSourceFile(organization.getIconUrl());
        if (sourceFile.isPresent()) {
            return sourceFile.get();
        }
        throw new ImageNotFoundException();

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
