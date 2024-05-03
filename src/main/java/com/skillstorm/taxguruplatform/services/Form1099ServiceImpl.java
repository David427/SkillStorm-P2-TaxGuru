package com.skillstorm.taxguruplatform.services;

import com.skillstorm.taxguruplatform.domain.dtos.Form1099Dto;
import com.skillstorm.taxguruplatform.domain.dtos.Form1099Dto;
import com.skillstorm.taxguruplatform.domain.entities.Form1099;
import com.skillstorm.taxguruplatform.exceptions.Form1099AlreadyExistsException;
import com.skillstorm.taxguruplatform.exceptions.Form1099NotFoundException;
import com.skillstorm.taxguruplatform.repositories.Form1099Repository;
import com.skillstorm.taxguruplatform.utils.mappers.Mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Form1099ServiceImpl implements Form1099Service {

    private final Form1099Repository form1099Repository;
    private final Mapper<Form1099, Form1099Dto> form1099Mapper;

    @Autowired
    public Form1099ServiceImpl(Form1099Repository form1099Repository, Mapper<Form1099, Form1099Dto> form1099Mapper) {
        this.form1099Repository = form1099Repository;
        this.form1099Mapper = form1099Mapper;
    }

    @Override
    public Form1099Dto create(Form1099Dto form1099Dto) throws Form1099AlreadyExistsException {
        if (isExisting(form1099Dto.getId())) {
            throw new Form1099AlreadyExistsException("Tax return already exists.");
        }

        Form1099 createdForm1099 = form1099Repository.save(form1099Mapper.mapFrom(form1099Dto));
        return form1099Mapper.mapTo(createdForm1099);
    }

    @Override
    public Form1099Dto fullUpdate(Form1099Dto form1099Dto) throws Form1099NotFoundException {
        if (isExisting(form1099Dto.getId())) {
            Form1099 updatedForm1099 = form1099Repository.save(form1099Mapper.mapFrom(form1099Dto));
            return form1099Mapper.mapTo(updatedForm1099);
        } else {
            throw new Form1099NotFoundException("Tax return not found.");
        }
    }

    @Override
    public void delete(long id) throws Form1099NotFoundException {
        if (isExisting(id)) {
            form1099Repository.deleteById(id);
        } else {
            throw new Form1099NotFoundException("Tax return not found.");
        }
    }

    @Override
    public boolean isExisting(long id) {
        return false;
    }

}
