package com.skillstorm.taxguruplatform.controllers;

import com.skillstorm.taxguruplatform.domain.dtos.UserAuthDto;
import com.skillstorm.taxguruplatform.exceptions.AppUserAlreadyExistsException;
import com.skillstorm.taxguruplatform.exceptions.AppUserAuthException;
import com.skillstorm.taxguruplatform.exceptions.AppUserNotFoundException;
import com.skillstorm.taxguruplatform.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthService authService;

    @Autowired
    public AuthenticationController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public UserAuthDto registerUser(@RequestBody UserAuthDto body) throws AppUserAlreadyExistsException {
        return authService.registerUser(body.getUsername(), body.getPassword());
    }

    @PostMapping("/login")
    public UserAuthDto loginUser(@RequestBody UserAuthDto body) throws AppUserNotFoundException, AppUserAuthException {
        return authService.loginUser(body.getUsername(), body.getPassword());
    }

}
