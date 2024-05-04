package com.skillstorm.taxguruplatform.services;

import com.skillstorm.taxguruplatform.domain.dtos.FormW2Dto;
import com.skillstorm.taxguruplatform.domain.entities.FormW2;
import com.skillstorm.taxguruplatform.exceptions.FormW2AlreadyExistsException;
import com.skillstorm.taxguruplatform.exceptions.FormW2NotFoundException;
import com.skillstorm.taxguruplatform.repositories.FormW2Repository;
import com.skillstorm.taxguruplatform.utils.mappers.FormW2MapperImpl;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class FormW2ServiceImplTests {

    @Mock
    private FormW2Repository formW2Repository;

    @Mock
    private FormW2MapperImpl formW2Mapper;

    @InjectMocks
    private FormW2ServiceImpl formW2Service;

    @Test
    void createFailAlreadyExistsEx() {
        FormW2Dto formW2Dto = FormW2Dto.builder()
                .build();

        when(formW2Repository.existsById(ArgumentMatchers.any(Long.class))).thenReturn(true);

        assertThrows(FormW2AlreadyExistsException.class, () ->
            formW2Service.create(formW2Dto)
        );
    }

    @Test
    void createSuccess() throws FormW2AlreadyExistsException {
        FormW2Dto inputFormW2Dto = FormW2Dto.builder()
                .build();

        FormW2 inputFormW2 = FormW2.builder()
                .build();

        FormW2 createdFormW2 = FormW2.builder()
                .id(1)
                .build();

        FormW2Dto createdFormW2Dto = FormW2Dto.builder()
                .id(1)
                .build();

        when(formW2Repository.existsById(ArgumentMatchers.any(Long.class))).thenReturn(false);
        when(formW2Mapper.mapFrom(ArgumentMatchers.any(FormW2Dto.class))).thenReturn(inputFormW2);
        when(formW2Repository.save(ArgumentMatchers.any(FormW2.class))).thenReturn(createdFormW2);
        when(formW2Mapper.mapTo(ArgumentMatchers.any(FormW2.class))).thenReturn(createdFormW2Dto);

        assertEquals(1, formW2Service.create(inputFormW2Dto).getId());
    }

    @Test
    void fullUpdateFailNotFoundEx() {
        FormW2Dto inputFormW2Dto = FormW2Dto.builder()
                .id(1)
                .build();

        when(formW2Repository.existsById(ArgumentMatchers.any(Long.class))).thenReturn(false);

        assertThrows(FormW2NotFoundException.class, () ->
            formW2Service.fullUpdate(inputFormW2Dto)
        );
    }

    @Test
    void fullUpdateSuccess() throws FormW2NotFoundException {
        FormW2Dto inputFormW2Dto = FormW2Dto.builder()
                .id(1)
                .income(new BigDecimal("50000.00"))
                .build();

        FormW2 inputFormW2 = FormW2.builder()
                .id(1)
                .income(new BigDecimal("50000.00"))
                .build();

        FormW2 updatedFormW2 = FormW2.builder()
                .id(1)
                .income(new BigDecimal("50000.00"))
                .build();

        FormW2Dto updatedFormW2Dto = FormW2Dto.builder()
                .id(1)
                .income(new BigDecimal("50000.00"))
                .build();

        when(formW2Repository.existsById(ArgumentMatchers.any(Long.class))).thenReturn(true);
        when(formW2Mapper.mapFrom(ArgumentMatchers.any(FormW2Dto.class))).thenReturn(inputFormW2);
        when(formW2Repository.save(ArgumentMatchers.any(FormW2.class))).thenReturn(updatedFormW2);
        when(formW2Mapper.mapTo(ArgumentMatchers.any(FormW2.class))).thenReturn(updatedFormW2Dto);

        assertEquals(1, formW2Service.fullUpdate(inputFormW2Dto).getId());
        assertEquals(new BigDecimal("50000.00"), formW2Service.fullUpdate(inputFormW2Dto).getIncome());
    }

    @Test
    void deleteFailNotFoundEx() {
        FormW2 nonExistingFormW2 = FormW2.builder()
                .id(1)
                .build();

        when(formW2Repository.existsById(ArgumentMatchers.any(Long.class))).thenReturn(false);
        verify(formW2Repository, times(0)).deleteById(nonExistingFormW2.getId());

        assertThrows(FormW2NotFoundException.class, () ->
            formW2Service.delete(nonExistingFormW2.getId())
        );
    }

    @Test
    void deleteSuccess() throws FormW2NotFoundException {
        FormW2 existingFormW2 = FormW2.builder()
                .id(1)
                .build();

        when(formW2Repository.existsById(ArgumentMatchers.any(Long.class))).thenReturn(true);
        formW2Service.delete(existingFormW2.getId());
        verify(formW2Repository, times(1)).deleteById(existingFormW2.getId());
    }

}