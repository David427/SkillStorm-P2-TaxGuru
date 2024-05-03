package com.skillstorm.taxguruplatform.utils.mappers;

import com.skillstorm.taxguruplatform.domain.dtos.FormW2Dto;
import com.skillstorm.taxguruplatform.domain.entities.FormW2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FormW2MapperImpl implements Mapper<FormW2, FormW2Dto> {

    private final ModelMapper modelMapper;

    @Autowired
    public FormW2MapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public FormW2Dto mapTo(FormW2 formW2) {
        return modelMapper.map(formW2, FormW2Dto.class);
    }

    @Override
    public FormW2 mapFrom(FormW2Dto formW2Dto) {
        return modelMapper.map(formW2Dto, FormW2.class);
    }

}
