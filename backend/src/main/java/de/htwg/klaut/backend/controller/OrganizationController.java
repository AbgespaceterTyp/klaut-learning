package de.htwg.klaut.backend.controller;

import de.htwg.klaut.backend.model.db.Organization;
import de.htwg.klaut.backend.model.dto.CreateOrganizationDto;
import de.htwg.klaut.backend.model.dto.SearchOrganizationKeyRequestDto;
import de.htwg.klaut.backend.model.dto.SearchOrganizationKeyResponseDto;
import de.htwg.klaut.backend.model.dto.SubscriptionInformationDto;
import de.htwg.klaut.backend.service.IOrganizationService;
import de.htwg.klaut.backend.service.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
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
    public ResponseEntity updateSubscription(@PathVariable String organization, @RequestBody SubscriptionInformationDto subscriptionInformationDto) {
        organizationService.updateSubscription(subscriptionInformationDto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(IOrganizationControllerPathConst.ORGANIZATION_MAPPING)
    public ResponseEntity createOrganization(@RequestBody CreateOrganizationDto createOrganizationDto) {
        Organization organization = organizationService.createOrganization(createOrganizationDto.getName(), "");

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
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

}
