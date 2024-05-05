package com.skillstorm.taxguruplatform.controllers;

import com.skillstorm.taxguruplatform.domain.dtos.AppUserDto;
import com.skillstorm.taxguruplatform.domain.dtos.UserCredentialsDto;
import com.skillstorm.taxguruplatform.domain.entities.AppUser;
import com.skillstorm.taxguruplatform.exceptions.AppUserAlreadyExistsException;
import com.skillstorm.taxguruplatform.exceptions.AppUserNotFoundException;
import com.skillstorm.taxguruplatform.services.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class AppUserController {

    private final AppUserService appUserService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AppUserController(AppUserService appUserService, PasswordEncoder passwordEncoder) {
        this.appUserService = appUserService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<AppUserDto> createAppUser(@RequestBody UserCredentialsDto userCredentialsDto) throws AppUserAlreadyExistsException {
        AppUser newUser = AppUser.builder()
                .username(userCredentialsDto.getUsername())
                .password(passwordEncoder.encode(userCredentialsDto.getPassword()))
                .userRole("USER")
                .build();
        AppUserDto newUserDto = appUserService.create(newUser);
        return new ResponseEntity<>(newUserDto, HttpStatus.CREATED);
    }

    @PostMapping("/login")

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
