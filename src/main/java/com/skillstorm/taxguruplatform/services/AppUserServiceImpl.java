package com.skillstorm.taxguruplatform.services;

import com.skillstorm.taxguruplatform.domain.dtos.AppUserDto;
import com.skillstorm.taxguruplatform.domain.entities.AppUser;
import com.skillstorm.taxguruplatform.exceptions.AppUserAlreadyExistsException;
import com.skillstorm.taxguruplatform.exceptions.AppUserNotFoundException;
import com.skillstorm.taxguruplatform.repositories.AppUserRepository;
import com.skillstorm.taxguruplatform.utils.mappers.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
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
    public AppUserDto create(AppUserDto appUserDto) throws AppUserAlreadyExistsException {
        if (isExisting(appUserDto.getUsername())) {
            throw new AppUserAlreadyExistsException("User already exists.");
        }

        AppUser createdAppUser = appUserRepository.save(appUserMapper.mapFrom(appUserDto));
        return appUserMapper.mapTo(createdAppUser);
    }

    @Override
    public List<AppUserDto> findAll() {
        List<AppUserDto> appUserDtos = new LinkedList<>();
        List<AppUser> foundAppUsers = appUserRepository.findAll();

        for (AppUser appUser : foundAppUsers) {
            appUserDtos.add(appUserMapper.mapTo(appUser));
        }

        return appUserDtos;
    }

    @Override
    public AppUserDto findByUsername(String username) throws AppUserNotFoundException {
        Optional<AppUser> foundAppUser = appUserRepository.findById(username);

        if (foundAppUser.isPresent()) {
            return appUserMapper.mapTo(foundAppUser.get());
        } else {
            throw new AppUserNotFoundException("User not found.");
        }

    }

    @Override
    public AppUserDto fullUpdate(AppUserDto appUserDto) throws AppUserNotFoundException {
        if (isExisting(appUserDto.getUsername())) {
            AppUser updatedAppUser = appUserRepository.save(appUserMapper.mapFrom(appUserDto));
            return appUserMapper.mapTo(updatedAppUser);
        } else {
            throw new AppUserNotFoundException("User not found.");
        }
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
        Optional<AppUser> foundUser = appUserRepository.findById(username);

        if (foundUser.isEmpty()) {
            throw new UsernameNotFoundException(username + "not found.");
        }

        AppUser appUser = foundUser.get();
        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;

        return new User(
                appUser.getUsername(),
                appUser.getPassword(),
                enabled, accountNonExpired,
                credentialsNonExpired,
                accountNonLocked,
                appUser.getAuthorities());
    }

}
