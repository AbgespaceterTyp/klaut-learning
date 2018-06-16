package de.htwg.klaut.backend.controller;

import de.htwg.klaut.backend.model.db.CreateUserDto;
import de.htwg.klaut.backend.model.dto.CurrentUserDto;
import de.htwg.klaut.backend.service.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(IUserControllerPathConst.CONTROLLER_MAPPING)
public class UserController {

    private IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping(IUserControllerPathConst.ME_MAPPING)
    public ResponseEntity<CurrentUserDto> currentUser(@PathVariable String organization) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return new ResponseEntity<>(new CurrentUserDto(currentPrincipalName), HttpStatus.OK);
    }

    @DeleteMapping(IUserControllerPathConst.ME_MAPPING)
    public ResponseEntity logout(@PathVariable String organization) {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity create(@PathVariable String organization, @RequestBody @Valid CreateUserDto createUserDto) {
        userService.create(createUserDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
