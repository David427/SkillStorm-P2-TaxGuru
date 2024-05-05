package com.skillstorm.taxguruplatform.services;

import com.skillstorm.taxguruplatform.domain.dtos.UserAuthDto;
import com.skillstorm.taxguruplatform.domain.entities.AppUser;
import com.skillstorm.taxguruplatform.exceptions.AppUserAlreadyExistsException;
import com.skillstorm.taxguruplatform.exceptions.AppUserAuthException;
import com.skillstorm.taxguruplatform.repositories.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final AppUserRepository appUserRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;

    @Autowired
    public AuthServiceImpl(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, TokenService tokenService) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @Override
    public UserAuthDto registerUser(String username, String password) throws AppUserAlreadyExistsException {
        if (appUserRepository.existsByUsername(username)) {
            throw new AppUserAlreadyExistsException("That username is not available.");
        }

        String encodedPassword = passwordEncoder.encode(password);
        AppUser newUser = AppUser.builder()
                .username(username)
                .password(encodedPassword)
                .userRole("USER")
                .build();
        appUserRepository.save(newUser);

        return UserAuthDto.builder()
                .username(newUser.getUsername())
                .password("Hashed and saved in db")
                .jwt("Login to generate")
                .build();
    }

    /*
     *  - Find & authenticate a username & password using the AuthenticationManager
     *  - Generate a token to send to TokenService
     *  - TokenService generates the JWT
     *  - Send JWT to frontend so it can store it and be logged in
     */
    @Override
    public UserAuthDto loginUser(String username, String password) throws AppUserAuthException {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            String token = tokenService.generateJwt(auth);

            return UserAuthDto.builder()
                    .username(username)
                    .password("Hashed and saved in db")
                    .jwt(token)
                    .build();
        } catch (AuthenticationException e) {
            throw new AppUserAuthException("User not found or bad credentials.");
        }
    }

}
