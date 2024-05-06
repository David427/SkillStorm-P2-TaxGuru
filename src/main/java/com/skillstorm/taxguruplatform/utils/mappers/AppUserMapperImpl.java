package com.skillstorm.taxguruplatform.utils.mappers;

import com.skillstorm.taxguruplatform.domain.dtos.AppUserDto;
import com.skillstorm.taxguruplatform.domain.entities.AppUser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AppUserMapperImpl implements Mapper<AppUser, AppUserDto> {

    private final ModelMapper modelMapper;

    @Autowired
    public AppUserMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public AppUserDto mapTo(AppUser appUser) {
        return modelMapper.map(appUser, AppUserDto.class);
    }

    @Override
    public AppUser mapFrom(AppUserDto appUserDto) {
        return modelMapper.map(appUserDto, AppUser.class);
    }

}
