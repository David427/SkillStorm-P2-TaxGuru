package com.skillstorm.taxguruplatform.services;

import com.skillstorm.taxguruplatform.domain.dtos.FormW2Dto;
import com.skillstorm.taxguruplatform.exceptions.FormW2AlreadyExistsException;
import com.skillstorm.taxguruplatform.exceptions.FormW2NotFoundException;

public interface FormW2Service {

    FormW2Dto create(FormW2Dto formW2Dto) throws FormW2AlreadyExistsException;

    FormW2Dto fullUpdate(FormW2Dto formW2Dto) throws FormW2NotFoundException;

    void delete(Long id) throws FormW2NotFoundException;

    boolean isExisting(Long id);

}
