package com.skillstorm.taxguruplatform.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillstorm.taxguruplatform.domain.dtos.TaxReturnDto;
import com.skillstorm.taxguruplatform.exceptions.TaxReturnAlreadyExistsException;
import com.skillstorm.taxguruplatform.exceptions.TaxReturnNotFoundException;
import com.skillstorm.taxguruplatform.services.TaxReturnService;

@RestController
@RequestMapping("/return")
@CrossOrigin
public class TaxReturnController {

    private final TaxReturnService taxReturnService;

    @Autowired
    public TaxReturnController(TaxReturnService taxReturnService) {
        this.taxReturnService = taxReturnService;
    }

    @PostMapping
    public ResponseEntity<TaxReturnDto> createTaxReturn(@RequestBody TaxReturnDto taxReturnDto) throws TaxReturnAlreadyExistsException {
        return new ResponseEntity<>(taxReturnService.create(taxReturnDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaxReturnDto> fullUpdateTaxReturn(@PathVariable("id") long id, @RequestBody TaxReturnDto taxReturnDto) throws TaxReturnNotFoundException {
        taxReturnDto.setId(id);
        return new ResponseEntity<>(taxReturnService.fullUpdate(taxReturnDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTaxReturn(@PathVariable("id") long id) throws TaxReturnNotFoundException {
        taxReturnService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
