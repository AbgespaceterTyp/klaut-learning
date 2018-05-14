package de.htwg.klaut.backend.controller;

import de.htwg.klaut.backend.model.dto.UserDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("{organization}/user")
public class UserController {

    @GetMapping(path = "me")
    public ResponseEntity<UserDto> getCurrentUser(@PathVariable String organization) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        return new ResponseEntity<>(new UserDto(currentPrincipalName), HttpStatus.OK);
    }
}
