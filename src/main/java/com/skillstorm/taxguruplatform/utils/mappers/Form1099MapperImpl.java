package com.skillstorm.taxguruplatform.utils.mappers;

import com.skillstorm.taxguruplatform.domain.dtos.FinancialDataDto;
import com.skillstorm.taxguruplatform.domain.dtos.Form1099Dto;
import com.skillstorm.taxguruplatform.domain.entities.FinancialData;
import com.skillstorm.taxguruplatform.domain.entities.Form1099;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class Form1099MapperImpl implements Mapper<Form1099, Form1099Dto> {

    private final ModelMapper modelMapper;

    @Autowired
    public Form1099MapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public Form1099Dto mapTo(Form1099 form1099) {
        return modelMapper.map(form1099, Form1099Dto.class);
    }

    @Override
    public Form1099 mapFrom(Form1099Dto form1099Dto) {
        return modelMapper.map(form1099Dto, Form1099.class);
    }

}
