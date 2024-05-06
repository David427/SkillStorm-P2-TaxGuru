package com.skillstorm.taxguruplatform.controllers;

import com.skillstorm.taxguruplatform.domain.dtos.TaxReturnDto;
import com.skillstorm.taxguruplatform.exceptions.ForbiddenException;
import com.skillstorm.taxguruplatform.exceptions.ResultCalculationException;
import com.skillstorm.taxguruplatform.exceptions.TaxReturnAlreadyExistsException;
import com.skillstorm.taxguruplatform.exceptions.TaxReturnNotFoundException;
import com.skillstorm.taxguruplatform.services.TaxReturnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<TaxReturnDto> createTaxReturn(
            @RequestBody TaxReturnDto taxReturnDto,
            @RequestParam String username,
            Authentication auth)
            throws TaxReturnAlreadyExistsException, ForbiddenException {
        if (!username.equals(auth.getName())) {
            throw new ForbiddenException("You are not authorized to access this endpoint.");
        }

        return new ResponseEntity<>(taxReturnService.create(taxReturnDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}/result")
    public ResponseEntity<TaxReturnDto> calculateResult(
            @PathVariable("id") Long id,
            @RequestParam String username,
            Authentication auth)
            throws TaxReturnNotFoundException, ResultCalculationException, ForbiddenException {
        if (!username.equals(auth.getName())) {
            throw new ForbiddenException("You are not authorized to access this endpoint.");
        }

        return new ResponseEntity<>(taxReturnService.calculateResult(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaxReturnDto> fullUpdateTaxReturn(
            @PathVariable("id") Long id,
            @RequestParam String username,
            @RequestBody TaxReturnDto taxReturnDto,
            Authentication auth)
            throws TaxReturnNotFoundException, ForbiddenException {
        if (!username.equals(auth.getName())) {
            throw new ForbiddenException("You are not authorized to access this endpoint.");
        }

        taxReturnDto.setId(id);
        return new ResponseEntity<>(taxReturnService.fullUpdate(taxReturnDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTaxReturn(
            @PathVariable("id") Long id,
            @RequestParam String username,
            Authentication auth)
            throws TaxReturnNotFoundException, ForbiddenException {
        if (!username.equals(auth.getName())) {
            throw new ForbiddenException("You are not authorized to access this endpoint.");
        }

        taxReturnService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
