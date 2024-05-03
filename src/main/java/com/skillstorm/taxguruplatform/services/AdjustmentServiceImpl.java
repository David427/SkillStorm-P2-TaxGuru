package com.skillstorm.taxguruplatform.services;

import com.skillstorm.taxguruplatform.domain.dtos.AdjustmentDto;
import com.skillstorm.taxguruplatform.domain.entities.Adjustment;
import com.skillstorm.taxguruplatform.exceptions.AdjustmentAlreadyExistsException;
import com.skillstorm.taxguruplatform.exceptions.AdjustmentNotFoundException;
import com.skillstorm.taxguruplatform.repositories.AdjustmentRepository;
import com.skillstorm.taxguruplatform.utils.mappers.Mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdjustmentServiceImpl implements AdjustmentService {

    private final AdjustmentRepository adjustmentRepository;
    private final Mapper<Adjustment, AdjustmentDto> adjusmentMapper;

    @Autowired
    public AdjustmentServiceImpl(AdjustmentRepository adjustmentRepository, Mapper<Adjustment, AdjustmentDto> adjusmentMapper) {
        this.adjustmentRepository = adjustmentRepository;
        this.adjusmentMapper = adjusmentMapper;
    }

    @Override
    public AdjustmentDto create(AdjustmentDto adjustmentDto) throws AdjustmentAlreadyExistsException {
        if (isExisting(adjustmentDto.getId())) {
            throw new AdjustmentAlreadyExistsException("Tax return already exists.");
        }

        Adjustment createdAdjustment = adjustmentRepository.save(adjusmentMapper.mapFrom(adjustmentDto));
        return adjusmentMapper.mapTo(createdAdjustment);
    }

    @Override
    public AdjustmentDto fullUpdate(AdjustmentDto adjustmentDto) throws AdjustmentNotFoundException {
        if (isExisting(adjustmentDto.getId())) {
            Adjustment updatedAdjustment = adjustmentRepository.save(adjusmentMapper.mapFrom(adjustmentDto));
            return adjusmentMapper.mapTo(updatedAdjustment);
        } else {
            throw new AdjustmentNotFoundException("Tax return not found.");
        }
    }

    @Override
    public void delete(long id) throws AdjustmentNotFoundException {
        if (isExisting(id)) {
            adjustmentRepository.deleteById(id);
        } else {
            throw new AdjustmentNotFoundException("Tax return not found.");
        }
    }

    @Override
    public boolean isExisting(long id) {
        return false;
    }

}
