package com.skillstorm.taxguruplatform.services;

import com.skillstorm.taxguruplatform.domain.dtos.AdjustmentDto;
import com.skillstorm.taxguruplatform.exceptions.AdjustmentAlreadyExistsException;
import com.skillstorm.taxguruplatform.exceptions.AdjustmentNotFoundException;

public interface AdjustmentService {

    AdjustmentDto create(AdjustmentDto adjustmentDto) throws AdjustmentAlreadyExistsException;

    AdjustmentDto fullUpdate(AdjustmentDto adjustmentDto) throws AdjustmentNotFoundException;

    void delete(Long id) throws AdjustmentNotFoundException;

    boolean isExisting(Long id);

}
