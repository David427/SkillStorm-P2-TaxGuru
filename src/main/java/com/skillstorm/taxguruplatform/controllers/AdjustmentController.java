package com.skillstorm.taxguruplatform.controllers;

import com.skillstorm.taxguruplatform.exceptions.AdjustmentAlreadyExistsException;
import com.skillstorm.taxguruplatform.exceptions.AdjustmentNotFoundException;
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

import com.skillstorm.taxguruplatform.domain.dtos.AdjustmentDto;
import com.skillstorm.taxguruplatform.services.AdjustmentService;

@RestController
@RequestMapping("/adjustments")
@CrossOrigin
public class AdjustmentController {

    private final AdjustmentService adjustmentService;

    @Autowired
    public AdjustmentController(AdjustmentService adjustmentService) {
        this.adjustmentService = adjustmentService;
    }

    @PostMapping
    public ResponseEntity<AdjustmentDto> createAdjustmentData(@RequestBody AdjustmentDto adjustmentDto) throws AdjustmentAlreadyExistsException {
        return new ResponseEntity<>(adjustmentService.create(adjustmentDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdjustmentDto> fullUpdateAdjustmentData(@PathVariable("id") long id, @RequestBody AdjustmentDto adjustmentDto) throws AdjustmentNotFoundException {
        adjustmentDto.setId(id);
        return new ResponseEntity<>(adjustmentService.fullUpdate(adjustmentDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAdjustmentData(@PathVariable("id") long id) throws AdjustmentNotFoundException {
        adjustmentService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
