package com.skillstorm.taxguruplatform.utils.mappers;

import com.skillstorm.taxguruplatform.domain.dtos.AdjustmentDto;
import com.skillstorm.taxguruplatform.domain.dtos.AdjustmentDto;
import com.skillstorm.taxguruplatform.domain.entities.Adjustment;
import com.skillstorm.taxguruplatform.domain.entities.Adjustment;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdjustmentMapperImpl implements Mapper<Adjustment, AdjustmentDto> {

    private final ModelMapper modelMapper;

    @Autowired
    public AdjustmentMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public AdjustmentDto mapTo(Adjustment adjustment) {
        return modelMapper.map(adjustment, AdjustmentDto.class);
    }

    @Override
    public Adjustment mapFrom(AdjustmentDto adjustmentDto) {
        return modelMapper.map(adjustmentDto, Adjustment.class);
    }

}
