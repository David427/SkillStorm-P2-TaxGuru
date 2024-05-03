package com.skillstorm.taxguruplatform.services;

import com.skillstorm.taxguruplatform.domain.dtos.Form1099Dto;
import com.skillstorm.taxguruplatform.exceptions.AdjustmentNotFoundException;
import com.skillstorm.taxguruplatform.exceptions.Form1099AlreadyExistsException;
import com.skillstorm.taxguruplatform.exceptions.Form1099NotFoundException;

public interface Form1099Service {

    Form1099Dto create(Form1099Dto form1099Dto) throws Form1099AlreadyExistsException;

    Form1099Dto fullUpdate(Form1099Dto form1099Dto) throws Form1099NotFoundException;

    void delete(long id) throws Form1099NotFoundException;

    boolean isExisting(long id);

}
