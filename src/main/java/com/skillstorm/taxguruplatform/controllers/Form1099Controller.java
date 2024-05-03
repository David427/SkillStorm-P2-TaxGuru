package com.skillstorm.taxguruplatform.controllers;

import com.skillstorm.taxguruplatform.exceptions.Form1099AlreadyExistsException;
import com.skillstorm.taxguruplatform.exceptions.Form1099NotFoundException;
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

import com.skillstorm.taxguruplatform.domain.dtos.Form1099Dto;
import com.skillstorm.taxguruplatform.services.Form1099Service;

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
    public ResponseEntity<Form1099Dto> createForm1099(@RequestBody Form1099Dto form1099Dto) throws Form1099AlreadyExistsException {
        return new ResponseEntity<>(form1099Service.create(form1099Dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Form1099Dto> fullUpdateForm1099(@PathVariable("id") long id, @RequestBody Form1099Dto form1099Dto) throws Form1099NotFoundException {
        form1099Dto.setId(id);
        return new ResponseEntity<>(form1099Service.fullUpdate(form1099Dto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteForm1099(@PathVariable("id") long id) throws Form1099NotFoundException {
        form1099Service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
