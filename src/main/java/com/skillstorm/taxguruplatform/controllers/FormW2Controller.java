package com.skillstorm.taxguruplatform.controllers;

import com.skillstorm.taxguruplatform.exceptions.FormW2AlreadyExistsException;
import com.skillstorm.taxguruplatform.exceptions.FormW2NotFoundException;
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

import com.skillstorm.taxguruplatform.domain.dtos.FormW2Dto;
import com.skillstorm.taxguruplatform.services.FormW2Service;

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
    public ResponseEntity<FormW2Dto> createFormW2(@RequestBody FormW2Dto formW2Dto) throws FormW2AlreadyExistsException {
        return new ResponseEntity<>(formW2Service.create(formW2Dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FormW2Dto> fullUpdateFormW2(@PathVariable("id") long id, @RequestBody FormW2Dto formW2Dto) throws FormW2NotFoundException {
        formW2Dto.setId(id);
        return new ResponseEntity<>(formW2Service.fullUpdate(formW2Dto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteFormW2(@PathVariable("id") long id) throws FormW2NotFoundException {
        formW2Service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
