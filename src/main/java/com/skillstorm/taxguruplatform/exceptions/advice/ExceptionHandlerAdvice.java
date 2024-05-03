package com.skillstorm.taxguruplatform.exceptions.advice;

import com.skillstorm.taxguruplatform.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(AppUserAlreadyExistsException.class)
    public ResponseEntity<String> handleEntityAlreadyExists(AppUserAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(AppUserNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFound(AppUserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(TaxReturnAlreadyExistsException.class)
    public ResponseEntity<String> handleEntityAlreadyExists(TaxReturnAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(TaxReturnNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFound(TaxReturnNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(FormW2AlreadyExistsException.class)
    public ResponseEntity<String> handleEntityAlreadyExists(FormW2AlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(FormW2NotFoundException.class)
    public ResponseEntity<String> handleEntityNotFound(FormW2NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(Form1099AlreadyExistsException.class)
    public ResponseEntity<String> handleEntityAlreadyExists(Form1099AlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(Form1099NotFoundException.class)
    public ResponseEntity<String> handleEntityNotFound(Form1099NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(AdjustmentAlreadyExistsException.class)
    public ResponseEntity<String> handleEntityAlreadyExists(AdjustmentAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(AdjustmentNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFound(AdjustmentNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

}
