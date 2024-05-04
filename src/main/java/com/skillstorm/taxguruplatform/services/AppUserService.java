package com.skillstorm.taxguruplatform.services;

import com.skillstorm.taxguruplatform.domain.dtos.AppUserDto;
import com.skillstorm.taxguruplatform.domain.entities.AppUser;
import com.skillstorm.taxguruplatform.exceptions.AppUserAlreadyExistsException;
import com.skillstorm.taxguruplatform.exceptions.AppUserNotFoundException;

import java.util.List;

public interface AppUserService {

    AppUserDto create(AppUser newUser) throws AppUserAlreadyExistsException;

    List<AppUserDto> findAll();

    AppUserDto findByUsername(String username) throws AppUserNotFoundException;

    AppUserDto fullUpdate(AppUserDto appUserDto) throws AppUserNotFoundException;

    void delete(String username) throws AppUserNotFoundException;

    boolean isExisting(String username);

}
