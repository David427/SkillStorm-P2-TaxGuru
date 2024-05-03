package com.skillstorm.taxguruplatform.services;

import com.skillstorm.taxguruplatform.domain.dtos.TaxReturnDto;
import com.skillstorm.taxguruplatform.exceptions.ResultCalculationException;
import com.skillstorm.taxguruplatform.exceptions.TaxReturnAlreadyExistsException;
import com.skillstorm.taxguruplatform.exceptions.TaxReturnNotFoundException;

public interface TaxReturnService {

    TaxReturnDto create(TaxReturnDto taxReturnDto) throws TaxReturnAlreadyExistsException;

    TaxReturnDto fullUpdate(TaxReturnDto taxReturnDto) throws TaxReturnNotFoundException;

    void delete(long id) throws TaxReturnNotFoundException;

    boolean isExisting(long id);

    TaxReturnDto calculateResult(long id) throws TaxReturnNotFoundException, ResultCalculationException;

}
