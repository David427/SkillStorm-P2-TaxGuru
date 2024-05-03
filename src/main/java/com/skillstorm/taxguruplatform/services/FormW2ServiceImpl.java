package com.skillstorm.taxguruplatform.services;

import com.skillstorm.taxguruplatform.domain.dtos.FormW2Dto;
import com.skillstorm.taxguruplatform.domain.dtos.FormW2Dto;
import com.skillstorm.taxguruplatform.domain.entities.FormW2;
import com.skillstorm.taxguruplatform.exceptions.FormW2AlreadyExistsException;
import com.skillstorm.taxguruplatform.exceptions.FormW2NotFoundException;
import com.skillstorm.taxguruplatform.repositories.FormW2Repository;
import com.skillstorm.taxguruplatform.utils.mappers.Mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FormW2ServiceImpl implements FormW2Service {

    private final FormW2Repository formW2Repository;
    private final Mapper<FormW2, FormW2Dto> formW2Mapper;

    @Autowired
    public FormW2ServiceImpl(FormW2Repository formW2Repository, Mapper<FormW2, FormW2Dto> formW2Mapper) {
        this.formW2Repository = formW2Repository;
        this.formW2Mapper = formW2Mapper;
    }

    @Override
    public FormW2Dto create(FormW2Dto formW2Dto) throws FormW2AlreadyExistsException {
        if (isExisting(formW2Dto.getId())) {
            throw new FormW2AlreadyExistsException("Tax return already exists.");
        }

        FormW2 createdFormW2 = formW2Repository.save(formW2Mapper.mapFrom(formW2Dto));
        return formW2Mapper.mapTo(createdFormW2);
    }

    @Override
    public FormW2Dto fullUpdate(FormW2Dto formW2Dto) throws FormW2NotFoundException {
        if (isExisting(formW2Dto.getId())) {
            FormW2 updatedFormW2 = formW2Repository.save(formW2Mapper.mapFrom(formW2Dto));
            return formW2Mapper.mapTo(updatedFormW2);
        } else {
            throw new FormW2NotFoundException("Tax return not found.");
        }
    }

    @Override
    public void delete(long id) throws FormW2NotFoundException {
        if (isExisting(id)) {
            formW2Repository.deleteById(id);
        } else {
            throw new FormW2NotFoundException("Tax return not found.");
        }
    }

    @Override
    public boolean isExisting(long id) {
        return false;
    }

}
