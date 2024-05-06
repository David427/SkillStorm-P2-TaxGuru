package com.skillstorm.taxguruplatform.controllers;

import com.skillstorm.taxguruplatform.domain.dtos.FormW2Dto;
import com.skillstorm.taxguruplatform.exceptions.ForbiddenException;
import com.skillstorm.taxguruplatform.exceptions.FormW2AlreadyExistsException;
import com.skillstorm.taxguruplatform.exceptions.FormW2NotFoundException;
import com.skillstorm.taxguruplatform.services.FormW2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/w2")
@CrossOrigin
public class FormW2Controller {

    private final FormW2Service formW2Service;

    @Autowired
    public FormW2Controller(FormW2Service formW2Service) {
        this.formW2Service = formW2Service;
    }

    @PostMapping
    public ResponseEntity<FormW2Dto> createFormW2(
            @RequestBody FormW2Dto formW2Dto,
            @RequestParam String username,
            Authentication auth)
            throws FormW2AlreadyExistsException, ForbiddenException {
        if (!username.equals(auth.getName())) {
            throw new ForbiddenException("You are not authorized to access this endpoint.");
        }

        return new ResponseEntity<>(formW2Service.create(formW2Dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FormW2Dto> fullUpdateFormW2(
            @PathVariable("id") Long id,
            @RequestParam String username,
            @RequestBody FormW2Dto formW2Dto,
            Authentication auth)
            throws FormW2NotFoundException, ForbiddenException {
        if (!username.equals(auth.getName())) {
            throw new ForbiddenException("You are not authorized to access this endpoint.");
        }

        formW2Dto.setId(id);
        return new ResponseEntity<>(formW2Service.fullUpdate(formW2Dto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteFormW2(
            @PathVariable("id") Long id,
            @RequestParam String username,
            Authentication auth) throws FormW2NotFoundException, ForbiddenException {
        if (!username.equals(auth.getName())) {
            throw new ForbiddenException("You are not authorized to access this endpoint.");
        }

        formW2Service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
