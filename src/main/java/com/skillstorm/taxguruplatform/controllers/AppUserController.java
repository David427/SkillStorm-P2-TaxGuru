package com.skillstorm.taxguruplatform.controllers;

import com.skillstorm.taxguruplatform.domain.dtos.AppUserDto;
import com.skillstorm.taxguruplatform.exceptions.AppUserNotFoundException;
import com.skillstorm.taxguruplatform.exceptions.ForbiddenException;
import com.skillstorm.taxguruplatform.services.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class AppUserController {

    private final AppUserService appUserService;

    @Autowired
    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping("/data")
    public ResponseEntity<AppUserDto> getAppUser(
            @RequestParam String username,
            Authentication auth)
            throws AppUserNotFoundException, ForbiddenException {
        if (!username.equals(auth.getName())) {
            throw new ForbiddenException("You are not authorized to access this endpoint.");
        }

        return new ResponseEntity<>(appUserService.findByUsername(username), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<AppUserDto> fullUpdateAppUser(
            @RequestParam String username,
            @RequestBody AppUserDto appUserDto,
            Authentication auth)
            throws AppUserNotFoundException, ForbiddenException {
        appUserDto.setUsername(username);

        if (!username.equals(auth.getName())) {
            throw new ForbiddenException("You are not authorized to access this endpoint.");
        }

        return new ResponseEntity<>(appUserService.fullUpdate(appUserDto), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteAppUser(
            @RequestParam String username,
            Authentication auth)
            throws AppUserNotFoundException, ForbiddenException {
        appUserService.delete(username);

        if (!username.equals(auth.getName())) {
            throw new ForbiddenException("You are not authorized to access this endpoint.");
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
