package com.skillstorm.taxguruplatform.services;

import com.skillstorm.taxguruplatform.domain.dtos.TaxReturnDto;
import com.skillstorm.taxguruplatform.domain.entities.TaxReturn;
import com.skillstorm.taxguruplatform.exceptions.TaxReturnAlreadyExistsException;
import com.skillstorm.taxguruplatform.exceptions.TaxReturnNotFoundException;
import com.skillstorm.taxguruplatform.repositories.TaxReturnRepository;
import com.skillstorm.taxguruplatform.utils.mappers.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaxReturnServiceImpl implements TaxReturnService {

    private final TaxReturnRepository taxReturnRepository;
    private final Mapper<TaxReturn, TaxReturnDto> taxReturnMapper;

    @Autowired
    public TaxReturnServiceImpl(TaxReturnRepository taxReturnRepository, Mapper<TaxReturn, TaxReturnDto> taxReturnMapper) {
        this.taxReturnRepository = taxReturnRepository;
        this.taxReturnMapper = taxReturnMapper;
    }

    @Override
    public TaxReturnDto create(TaxReturnDto taxReturnDto) throws TaxReturnAlreadyExistsException {
        if (isExisting(taxReturnDto.getId())) {
            throw new TaxReturnAlreadyExistsException("Tax return already exists.");
        }

        TaxReturn createdTaxReturn = taxReturnRepository.save(taxReturnMapper.mapFrom(taxReturnDto));
        return taxReturnMapper.mapTo(createdTaxReturn);
    }

    @Override
    public TaxReturnDto fullUpdate(TaxReturnDto taxReturnDto) throws TaxReturnNotFoundException {
        if (isExisting(taxReturnDto.getId())) {
            TaxReturn updatedTaxReturn = taxReturnRepository.save(taxReturnMapper.mapFrom(taxReturnDto));
            return taxReturnMapper.mapTo(updatedTaxReturn);
        } else {
            throw new TaxReturnNotFoundException("Tax return not found.");
        }
    }

    @Override
    public void delete(long id) throws TaxReturnNotFoundException {
        if (isExisting(id)) {
            taxReturnRepository.deleteById(id);
        } else {
            throw new TaxReturnNotFoundException("Tax return not found.");
        }
    }

    @Override
    public boolean isExisting(long id) {
        return false;
    }

}
