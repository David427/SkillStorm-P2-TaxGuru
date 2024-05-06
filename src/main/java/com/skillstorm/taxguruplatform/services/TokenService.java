package com.skillstorm.taxguruplatform.services;

import org.springframework.security.core.Authentication;

public interface TokenService {

    String generateJwt(Authentication auth);

}
