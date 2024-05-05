package com.skillstorm.taxguruplatform.services;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.skillstorm.taxguruplatform.domain.dtos.Form1099Dto;
import com.skillstorm.taxguruplatform.domain.entities.Form1099;
import com.skillstorm.taxguruplatform.exceptions.Form1099AlreadyExistsException;
import com.skillstorm.taxguruplatform.exceptions.Form1099NotFoundException;
import com.skillstorm.taxguruplatform.repositories.Form1099Repository;
import com.skillstorm.taxguruplatform.utils.mappers.Form1099MapperImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

@SpringBootTest
class Form1099ServiceImplTests {

    @Mock
    private Form1099Repository form1099Repository;

    @Mock
    private Form1099MapperImpl form1099Mapper;

    @InjectMocks
    private Form1099ServiceImpl form1099Service;

    @Test
    void createFailAlreadyExistsEx() {
        Form1099Dto form1099Dto = Form1099Dto.builder()
                .id(1L)
                .build();

        when(form1099Repository.existsById(ArgumentMatchers.any(Long.class))).thenReturn(true);

        assertThrows(Form1099AlreadyExistsException.class, () ->
            form1099Service.create(form1099Dto)
        );
    }

    @Test
    void createSuccess() throws Form1099AlreadyExistsException {
        Form1099Dto inputForm1099Dto = Form1099Dto.builder()
                .build();

        Form1099 inputForm1099 = Form1099.builder()
                .build();

        Form1099 createdForm1099 = Form1099.builder()
                .id(1L)
                .build();

        Form1099Dto createdForm1099Dto = Form1099Dto.builder()
                .id(1L)
                .build();

        when(form1099Repository.existsById(ArgumentMatchers.any(Long.class))).thenReturn(false);
        when(form1099Mapper.mapFrom(ArgumentMatchers.any(Form1099Dto.class))).thenReturn(inputForm1099);
        when(form1099Repository.save(ArgumentMatchers.any(Form1099.class))).thenReturn(createdForm1099);
        when(form1099Mapper.mapTo(ArgumentMatchers.any(Form1099.class))).thenReturn(createdForm1099Dto);

        assertEquals(1, form1099Service.create(inputForm1099Dto).getId());
    }

    @Test
    void fullUpdateFailNotFoundEx() {
        Form1099Dto inputForm1099Dto = Form1099Dto.builder()
                .id(1L)
                .build();

        when(form1099Repository.existsById(ArgumentMatchers.any(Long.class))).thenReturn(false);

        assertThrows(Form1099NotFoundException.class, () ->
            form1099Service.fullUpdate(inputForm1099Dto)
        );
    }

    @Test
    void fullUpdateSuccess() throws Form1099NotFoundException {
        Form1099Dto inputForm1099Dto = Form1099Dto.builder()
                .id(1L)
                .income(new BigDecimal("50000.00"))
                .build();

        Form1099 inputForm1099 = Form1099.builder()
                .id(1L)
                .income(new BigDecimal("50000.00"))
                .build();

        Form1099 updatedForm1099 = Form1099.builder()
                .id(1L)
                .income(new BigDecimal("50000.00"))
                .build();

        Form1099Dto updatedForm1099Dto = Form1099Dto.builder()
                .id(1L)
                .income(new BigDecimal("50000.00"))
                .build();

        when(form1099Repository.existsById(ArgumentMatchers.any(Long.class))).thenReturn(true);
        when(form1099Mapper.mapFrom(ArgumentMatchers.any(Form1099Dto.class))).thenReturn(inputForm1099);
        when(form1099Repository.save(ArgumentMatchers.any(Form1099.class))).thenReturn(updatedForm1099);
        when(form1099Mapper.mapTo(ArgumentMatchers.any(Form1099.class))).thenReturn(updatedForm1099Dto);

        assertEquals(1, form1099Service.fullUpdate(inputForm1099Dto).getId());
        assertEquals(new BigDecimal("50000.00"), form1099Service.fullUpdate(inputForm1099Dto).getIncome());
    }

    @Test
    void deleteFailNotFoundEx() {
        Form1099 nonExistingForm1099 = Form1099.builder()
                .id(1L)
                .build();

        when(form1099Repository.existsById(ArgumentMatchers.any(Long.class))).thenReturn(false);
        verify(form1099Repository, times(0)).deleteById(nonExistingForm1099.getId());

        assertThrows(Form1099NotFoundException.class, () ->
            form1099Service.delete(nonExistingForm1099.getId())
        );
    }

    @Test
    void deleteSuccess() throws Form1099NotFoundException {
        Form1099 existingForm1099 = Form1099.builder()
                .id(1L)
                .build();

        when(form1099Repository.existsById(ArgumentMatchers.any(Long.class))).thenReturn(true);
        form1099Service.delete(existingForm1099.getId());
        verify(form1099Repository, times(1)).deleteById(existingForm1099.getId());
    }

}