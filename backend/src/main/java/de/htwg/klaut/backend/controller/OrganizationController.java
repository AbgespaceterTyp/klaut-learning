package de.htwg.klaut.backend.controller;

import de.htwg.klaut.backend.model.dto.SubscriptionInformationDto;
import de.htwg.klaut.backend.model.dto.UserDto;
import de.htwg.klaut.backend.service.IOrganizationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("{organization}")
public class OrganizationController {

    private final IOrganizationService organizationService;

    public OrganizationController(IOrganizationService organizationService){
        this.organizationService = organizationService;
    }

    @GetMapping(path = "user/me")
    public ResponseEntity<UserDto> currentUser(@PathVariable String organization) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        return new ResponseEntity<>(new UserDto(currentPrincipalName), HttpStatus.OK);
    }

    @GetMapping(path = "subscription")
    public ResponseEntity<SubscriptionInformationDto> subscription(@PathVariable String organization) {
        return new ResponseEntity<>(new SubscriptionInformationDto(organizationService.getSubscription()), HttpStatus.OK);
    }
}
