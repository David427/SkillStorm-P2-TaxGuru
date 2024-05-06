package com.skillstorm.taxguruplatform.services;

import com.skillstorm.taxguruplatform.domain.dtos.AppUserDto;
import com.skillstorm.taxguruplatform.domain.entities.AppUser;
import com.skillstorm.taxguruplatform.exceptions.AppUserAlreadyExistsException;
import com.skillstorm.taxguruplatform.exceptions.AppUserNotFoundException;
import com.skillstorm.taxguruplatform.repositories.AppUserRepository;
import com.skillstorm.taxguruplatform.utils.mappers.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppUserServiceImpl implements AppUserService, UserDetailsService {

    private final AppUserRepository appUserRepository;
    private final Mapper<AppUser, AppUserDto> appUserMapper;

    @Autowired
    public AppUserServiceImpl(AppUserRepository appUserRepository, Mapper<AppUser, AppUserDto> appUserMapper) {
        this.appUserRepository = appUserRepository;
        this.appUserMapper = appUserMapper;
    }

    @Override
    public AppUserDto create(AppUser newUser) throws AppUserAlreadyExistsException {
        if (isExisting(newUser.getUsername())) {
            throw new AppUserAlreadyExistsException("User already exists.");
        }

        AppUser createdAppUser = appUserRepository.save(newUser);
        return appUserMapper.mapTo(createdAppUser);
    }

    @Override
    public AppUserDto findByUsername(String username) throws AppUserNotFoundException {
        Optional<AppUser> foundAppUser = appUserRepository.findByUsername(username);

        if (foundAppUser.isPresent()) {
            return appUserMapper.mapTo(foundAppUser.get());
        } else {
            throw new AppUserNotFoundException("User not found.");
        }

    }

    @Override
    public AppUserDto fullUpdate(AppUserDto appUserDto) throws AppUserNotFoundException {
        AppUser receivedAppUser = appUserMapper.mapFrom(appUserDto);
        AppUser existingAppUser = appUserRepository.findByUsername(appUserDto.getUsername())
                .orElseThrow(() -> new AppUserNotFoundException("User not found."));

        // Prevent a lack of role/password in the DTO (null values) from being saved as null in the db
        receivedAppUser.setUserRole(existingAppUser.getUserRole());
        receivedAppUser.setPassword(existingAppUser.getPassword());

        AppUser updatedAppUser = appUserRepository.save(receivedAppUser);
        return appUserMapper.mapTo(updatedAppUser);
    }

    @Override
    public void delete(String username) throws AppUserNotFoundException {
        if (isExisting(username)) {
            appUserRepository.deleteById(username);
        } else {
            throw new AppUserNotFoundException("User not found.");
        }
    }

    @Override
    public boolean isExisting(String username) {
        return appUserRepository.existsByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return appUserRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("Username not found.")
        );
    }

}
