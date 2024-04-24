package com.skillstorm.taxguruplatform.utils.mappers;

import com.skillstorm.taxguruplatform.domain.dtos.AppUserDto;
import com.skillstorm.taxguruplatform.domain.dtos.FinancialDataDto;
import com.skillstorm.taxguruplatform.domain.entities.AppUser;
import com.skillstorm.taxguruplatform.domain.entities.FinancialData;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FinancialDataMapperImpl implements Mapper<FinancialData, FinancialDataDto> {

    private final ModelMapper modelMapper;

    @Autowired
    public FinancialDataMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public FinancialDataDto mapTo(FinancialData financialData) {
        return modelMapper.map(financialData, FinancialDataDto.class);
    }

    @Override
    public FinancialData mapFrom(FinancialDataDto financialDataDto) {
        return modelMapper.map(financialDataDto, FinancialData.class);
    }

}
