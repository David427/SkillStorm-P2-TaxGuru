package com.skillstorm.taxguruplatform.controllers;

import com.skillstorm.taxguruplatform.domain.dtos.Form1099Dto;
import com.skillstorm.taxguruplatform.exceptions.ForbiddenException;
import com.skillstorm.taxguruplatform.exceptions.Form1099AlreadyExistsException;
import com.skillstorm.taxguruplatform.exceptions.Form1099NotFoundException;
import com.skillstorm.taxguruplatform.services.Form1099Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/1099")
@CrossOrigin
public class Form1099Controller {

    private final Form1099Service form1099Service;

    @Autowired
    public Form1099Controller(Form1099Service form1099Service) {
        this.form1099Service = form1099Service;
    }

    @PostMapping
    public ResponseEntity<Form1099Dto> createForm1099(
            @RequestBody Form1099Dto form1099Dto,
            @RequestParam String username,
            Authentication auth)
            throws Form1099AlreadyExistsException, ForbiddenException {
        if (!username.equals(auth.getName())) {
            throw new ForbiddenException("You are not authorized to access this endpoint.");
        }

        return new ResponseEntity<>(form1099Service.create(form1099Dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Form1099Dto> fullUpdateForm1099(
            @PathVariable("id") Long id,
            @RequestParam String username,
            @RequestBody Form1099Dto form1099Dto,
            Authentication auth)
            throws Form1099NotFoundException, ForbiddenException {
        if (!username.equals(auth.getName())) {
            throw new ForbiddenException("You are not authorized to access this endpoint.");
        }

        form1099Dto.setId(id);
        return new ResponseEntity<>(form1099Service.fullUpdate(form1099Dto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteForm1099(
            @PathVariable("id") Long id,
            @RequestParam String username,
            Authentication auth)
            throws Form1099NotFoundException, ForbiddenException {
        if (!username.equals(auth.getName())) {
            throw new ForbiddenException("You are not authorized to access this endpoint.");
        }

        form1099Service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
