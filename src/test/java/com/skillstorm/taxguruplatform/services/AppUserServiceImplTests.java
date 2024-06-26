package com.skillstorm.taxguruplatform.services;

import com.skillstorm.taxguruplatform.domain.dtos.AppUserDto;
import com.skillstorm.taxguruplatform.domain.entities.AppUser;
import com.skillstorm.taxguruplatform.exceptions.AppUserAlreadyExistsException;
import com.skillstorm.taxguruplatform.exceptions.AppUserNotFoundException;
import com.skillstorm.taxguruplatform.repositories.AppUserRepository;
import com.skillstorm.taxguruplatform.utils.mappers.AppUserMapperImpl;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
class AppUserServiceImplTests {

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private AppUserMapperImpl appUserMapper;

    @InjectMocks
    private AppUserServiceImpl appUserService;

    @Test
    void createFailAlreadyExistsEx() {
        AppUser appUser = AppUser.builder()
                .username("TestUser")
                .build();

        when(appUserRepository.existsByUsername(ArgumentMatchers.any(String.class))).thenReturn(true);

        assertThrows(AppUserAlreadyExistsException.class, () ->
                appUserService.create(appUser)
        );
    }

    @Test
    void createSuccess() throws AppUserAlreadyExistsException {
        AppUser inputAppUser = AppUser.builder()
                .username("TestUser")
                .build();

        AppUser createdAppUser = AppUser.builder()
                .username("TestUser")
                .build();

        AppUserDto createdAppUserDto = AppUserDto.builder()
                .username("TestUser")
                .build();

        when(appUserRepository.existsByUsername(ArgumentMatchers.any(String.class))).thenReturn(false);
        when(appUserMapper.mapFrom(ArgumentMatchers.any(AppUserDto.class))).thenReturn(inputAppUser);
        when(appUserRepository.save(ArgumentMatchers.any(AppUser.class))).thenReturn(createdAppUser);
        when(appUserMapper.mapTo(ArgumentMatchers.any(AppUser.class))).thenReturn(createdAppUserDto);

        assertEquals("TestUser", appUserService.create(inputAppUser).getUsername());
    }

    @Test
    void findByUsernameFailNotFoundEx() {
        String inputUsername = "TestUser";

        when(appUserRepository.findByUsername(ArgumentMatchers.any(String.class))).thenReturn(Optional.empty());

        assertThrows(AppUserNotFoundException.class, () ->
                appUserService.findByUsername(inputUsername)
        );
    }

    @Test
    void findByUsernameSuccess() throws AppUserNotFoundException {
        String inputUsername = "TestUser";

        AppUser foundAppUser = AppUser.builder()
                .username("TestUser")
                .build();

        AppUserDto foundAppUserDto = AppUserDto.builder()
                .username("TestUser")
                .build();

        when(appUserRepository.findByUsername(ArgumentMatchers.any(String.class))).thenReturn(Optional.of(foundAppUser));
        when(appUserMapper.mapTo(ArgumentMatchers.any(AppUser.class))).thenReturn(foundAppUserDto);

        assertEquals("TestUser", appUserService.findByUsername(inputUsername).getUsername());
    }

    @Test
    void fullUpdateFailNotFoundEx() {
        AppUserDto inputAppUserDto = AppUserDto.builder()
                .username("TestUser")
                .build();

        when(appUserRepository.existsByUsername(ArgumentMatchers.any(String.class))).thenReturn(false);

        assertThrows(AppUserNotFoundException.class, () ->
                appUserService.fullUpdate(inputAppUserDto)
        );
    }

    @Test
    void fullUpdateSuccess() throws AppUserNotFoundException {
        AppUserDto inputAppUserDto = AppUserDto.builder()
                .username("TestUser")
                .email("address@email.com")
                .build();

        AppUser inputAppUser = AppUser.builder()
                .username("TestUser")
                .email("address@email.com")
                .build();

        AppUser updatedAppUser = AppUser.builder()
                .username("TestUser")
                .email("address@email.com")
                .build();

        AppUserDto updatedAppUserDto = AppUserDto.builder()
                .username("TestUser")
                .email("address@email.com")
                .build();

        when(appUserRepository.findByUsername(ArgumentMatchers.any(String.class))).thenReturn(Optional.of(updatedAppUser));
        when(appUserMapper.mapFrom(ArgumentMatchers.any(AppUserDto.class))).thenReturn(inputAppUser);
        when(appUserRepository.save(ArgumentMatchers.any(AppUser.class))).thenReturn(updatedAppUser);
        when(appUserMapper.mapTo(ArgumentMatchers.any(AppUser.class))).thenReturn(updatedAppUserDto);

        assertEquals("TestUser", appUserService.fullUpdate(inputAppUserDto).getUsername());
        assertEquals("address@email.com", appUserService.fullUpdate(inputAppUserDto).getEmail());
    }

    @Test
    void deleteFailNotFoundEx() {
        AppUser nonExistingAppUser = AppUser.builder()
                .username("TestUser")
                .build();

        when(appUserRepository.existsByUsername(ArgumentMatchers.any(String.class))).thenReturn(false);
        verify(appUserRepository, times(0)).deleteById(nonExistingAppUser.getUsername());

        assertThrows(AppUserNotFoundException.class, () ->
                appUserService.delete(nonExistingAppUser.getUsername())
        );
    }

    @Test
    void deleteSuccess() throws AppUserNotFoundException {
        AppUser existingAppUser = AppUser.builder()
                .username("TestUser")
                .build();

        when(appUserRepository.existsByUsername(ArgumentMatchers.any(String.class))).thenReturn(true);
        appUserService.delete(existingAppUser.getUsername());
        verify(appUserRepository, times(1)).deleteById(existingAppUser.getUsername());
    }

    @Test
    void loadUserByUsernameFailNotFoundEx() {
        String inputUsername = "TestUser";

        when(appUserRepository.findByUsername(ArgumentMatchers.any(String.class))).thenReturn(Optional.empty());

        Exception exception = assertThrows(UsernameNotFoundException.class, () ->
                appUserService.loadUserByUsername(inputUsername)
        );
        assertEquals("Username not found.", exception.getMessage());
    }

}