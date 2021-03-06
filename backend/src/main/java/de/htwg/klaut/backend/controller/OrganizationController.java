package de.htwg.klaut.backend.controller;

import de.htwg.klaut.backend.exception.OrganizationNotFoundException;
import de.htwg.klaut.backend.model.db.Organization;
import de.htwg.klaut.backend.model.dto.*;
import de.htwg.klaut.backend.service.IOrganizationService;
import de.htwg.klaut.backend.service.IUserService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@RestController
@Log4j2
public class OrganizationController {

    private final static String ADMIN = "admin";

    private final IOrganizationService organizationService;

    private final IUserService userService;

    public OrganizationController(IOrganizationService organizationService, IUserService userService) {
        this.organizationService = organizationService;
        this.userService = userService;
    }

    @GetMapping(IOrganizationControllerPathConst.SUBSCRIPTION_MAPPING)
    public ResponseEntity<SubscriptionInformationDto> subscription(@PathVariable String organization) {
        return new ResponseEntity<>(new SubscriptionInformationDto(organizationService.getSubscription()), HttpStatus.OK);
    }

    @PostMapping(IOrganizationControllerPathConst.SUBSCRIPTION_MAPPING)
    public ResponseEntity updateSubscription(@PathVariable String organization, @RequestBody SubscriptionRenewDto newSubscriptionDto) {
        return new ResponseEntity<>(organizationService.renewSubscription(newSubscriptionDto), HttpStatus.OK);
    }

    @PostMapping(IOrganizationControllerPathConst.ORGANIZATION_MAPPING)
    public ResponseEntity createOrganization(@RequestBody CreateOrganizationDto createOrganizationDto) {
        Organization organization = organizationService.createOrganization(createOrganizationDto.getName());

        userService.create(createOrganizationDto.getAdminEmail(), ADMIN, ADMIN,
                createOrganizationDto.getAdminPassword(), organization.getKey());
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PostMapping(IOrganizationControllerPathConst.ORGANIZATION_KEY_MAPPING)
    public ResponseEntity findOrganizationKeyByName(@RequestBody SearchOrganizationKeyRequestDto searchOrganizationKeyRequestDto) {
        Optional<Organization> organization =
                organizationService.findByName(searchOrganizationKeyRequestDto.getOrganizationName());
        if (organization.isPresent()) {
            SearchOrganizationKeyResponseDto searchOrganizationKeyResponseDto = new SearchOrganizationKeyResponseDto(organization.get().getKey());
            return ResponseEntity.ok(searchOrganizationKeyResponseDto);
        }
        throw new OrganizationNotFoundException(searchOrganizationKeyRequestDto.getOrganizationName());
    }

    @PutMapping(IOrganizationControllerPathConst.ORGANIZATION_IMAGE_MAPPING)
    public ResponseEntity changeImage(@PathVariable String organization, @RequestBody MultipartFile image) {
        organizationService.changeImage(image);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(path = IOrganizationControllerPathConst.ORGANIZATION_IMAGE_MAPPING,
            produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public byte[] loadImage(@PathVariable String organization) {
        try {
            InputStream is = organizationService.loadImage();

            return IOUtils.toByteArray(is);
        } catch (IOException ex) {
            log.error("Failed to download image");
        }
        return null;
    }

}
