package com.skillstorm.taxguruplatform.controllers;

import com.skillstorm.taxguruplatform.domain.dtos.AppUserDto;
import com.skillstorm.taxguruplatform.exceptions.AppUserAlreadyExistsException;
import com.skillstorm.taxguruplatform.exceptions.AppUserNotFoundException;
import com.skillstorm.taxguruplatform.services.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class AppUserController {

    private final AppUserService appUserService;

    @Autowired
    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @PostMapping("/register")
    public ResponseEntity<AppUserDto> createAppUser(@RequestBody AppUserDto appUserDto) throws AppUserAlreadyExistsException {
        return new ResponseEntity<>(appUserService.create(appUserDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AppUserDto>> getAllAppUsers() {
        List<AppUserDto> foundAppUsers = appUserService.findAll();

        if (!foundAppUsers.isEmpty()) {
            return new ResponseEntity<>(foundAppUsers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{username}")
    public ResponseEntity<AppUserDto> getAppUser(@PathVariable("username") String username) throws AppUserNotFoundException {
        return new ResponseEntity<>(appUserService.findByUsername(username), HttpStatus.OK);
    }

    @PutMapping("/{username}")
    public ResponseEntity<AppUserDto> fullUpdateAppUser(@PathVariable("username") String username, @RequestBody AppUserDto appUserDto) throws AppUserNotFoundException {
        appUserDto.setUsername(username);
        return new ResponseEntity<>(appUserService.fullUpdate(appUserDto), HttpStatus.OK);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Object> deleteAppUser(@PathVariable("username") String username) throws AppUserNotFoundException {
        appUserService.delete(username);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
