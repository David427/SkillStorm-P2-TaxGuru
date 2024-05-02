package com.skillstorm.taxguruplatform.utils.mappers;

import com.skillstorm.taxguruplatform.domain.dtos.TaxReturnDto;
import com.skillstorm.taxguruplatform.domain.entities.TaxReturn;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaxReturnMapperImpl implements Mapper<TaxReturn, TaxReturnDto> {

    private final ModelMapper modelMapper;

    @Autowired
    public TaxReturnMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public TaxReturnDto mapTo(TaxReturn taxReturn) {
        return modelMapper.map(taxReturn, TaxReturnDto.class);
    }

    @Override
    public TaxReturn mapFrom(TaxReturnDto taxReturnDto) {
        return modelMapper.map(taxReturnDto, TaxReturn.class);
    }

}
