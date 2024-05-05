package com.skillstorm.taxguruplatform.controllers;

import com.skillstorm.taxguruplatform.domain.dtos.AdjustmentDto;
import com.skillstorm.taxguruplatform.exceptions.AdjustmentAlreadyExistsException;
import com.skillstorm.taxguruplatform.exceptions.AdjustmentNotFoundException;
import com.skillstorm.taxguruplatform.exceptions.ForbiddenException;
import com.skillstorm.taxguruplatform.services.AdjustmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<AdjustmentDto> createAdjustmentData(
            @RequestBody AdjustmentDto adjustmentDto,
            @RequestParam String username,
            Authentication auth)
            throws AdjustmentAlreadyExistsException, ForbiddenException {
        if (!username.equals(auth.getName())) {
            throw new ForbiddenException("You are not authorized to access this endpoint.");
        }

        return new ResponseEntity<>(adjustmentService.create(adjustmentDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdjustmentDto> fullUpdateAdjustmentData(
            @PathVariable("id") Long id,
            @RequestParam String username,
            @RequestBody AdjustmentDto adjustmentDto,
            Authentication auth) throws AdjustmentNotFoundException, ForbiddenException {
        if (!username.equals(auth.getName())) {
            throw new ForbiddenException("You are not authorized to access this endpoint.");
        }

        adjustmentDto.setId(id);
        return new ResponseEntity<>(adjustmentService.fullUpdate(adjustmentDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAdjustmentData(
            @PathVariable("id") Long id,
            @RequestParam String username,
            Authentication auth)
            throws AdjustmentNotFoundException, ForbiddenException {
        if (!username.equals(auth.getName())) {
            throw new ForbiddenException("You are not authorized to access this endpoint.");
        }

        adjustmentService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
