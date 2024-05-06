package com.skillstorm.taxguruplatform.services;

import com.skillstorm.taxguruplatform.domain.dtos.UserAuthDto;
import com.skillstorm.taxguruplatform.exceptions.AppUserAlreadyExistsException;
import com.skillstorm.taxguruplatform.exceptions.AppUserAuthException;
import com.skillstorm.taxguruplatform.exceptions.AppUserNotFoundException;

public interface AuthService {

    UserAuthDto registerUser(String username, String password) throws AppUserAlreadyExistsException;

    /*
     *  - Find & authenticate a username & password using the AuthenticationManager
     *  - Generate a token to send to TokenService
     *  - TokenService generates the JWT
     */
    UserAuthDto loginUser(String username, String password) throws AppUserNotFoundException, AppUserAuthException;

}
