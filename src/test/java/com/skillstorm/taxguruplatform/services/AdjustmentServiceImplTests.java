package com.skillstorm.taxguruplatform.services;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.skillstorm.taxguruplatform.domain.dtos.AdjustmentDto;
import com.skillstorm.taxguruplatform.domain.entities.Adjustment;
import com.skillstorm.taxguruplatform.exceptions.AdjustmentAlreadyExistsException;
import com.skillstorm.taxguruplatform.exceptions.AdjustmentNotFoundException;
import com.skillstorm.taxguruplatform.repositories.AdjustmentRepository;
import com.skillstorm.taxguruplatform.utils.mappers.AdjustmentMapperImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class AdjustmentServiceImplTests {

    @Mock
    private AdjustmentRepository adjustmentRepository;

    @Mock
    private AdjustmentMapperImpl adjustmentMapper;

    @InjectMocks
    private AdjustmentServiceImpl adjustmentService;

    @Test
    void createFailAlreadyExistsEx() {
        AdjustmentDto adjustmentDto = AdjustmentDto.builder()
                .build();

        when(adjustmentRepository.existsById(ArgumentMatchers.any(Long.class))).thenReturn(true);

        assertThrows(AdjustmentAlreadyExistsException.class, () ->
            adjustmentService.create(adjustmentDto)
        );
    }

    @Test
    void createSuccess() throws AdjustmentAlreadyExistsException {
        AdjustmentDto inputAdjustmentDto = AdjustmentDto.builder()
                .build();

        Adjustment inputAdjustment = Adjustment.builder()
                .build();

        Adjustment createdAdjustment = Adjustment.builder()
                .id(1)
                .build();

        AdjustmentDto createdAdjustmentDto = AdjustmentDto.builder()
                .id(1)
                .build();

        when(adjustmentRepository.existsById(ArgumentMatchers.any(Long.class))).thenReturn(false);
        when(adjustmentMapper.mapFrom(ArgumentMatchers.any(AdjustmentDto.class))).thenReturn(inputAdjustment);
        when(adjustmentRepository.save(ArgumentMatchers.any(Adjustment.class))).thenReturn(createdAdjustment);
        when(adjustmentMapper.mapTo(ArgumentMatchers.any(Adjustment.class))).thenReturn(createdAdjustmentDto);

        assertEquals(1, adjustmentService.create(inputAdjustmentDto).getId());
    }

    @Test
    void fullUpdateFailNotFoundEx() {
        AdjustmentDto inputAdjustmentDto = AdjustmentDto.builder()
                .id(1)
                .build();

        when(adjustmentRepository.existsById(ArgumentMatchers.any(Long.class))).thenReturn(false);

        assertThrows(AdjustmentNotFoundException.class, () ->
            adjustmentService.fullUpdate(inputAdjustmentDto)
        );
    }

    @Test
    void fullUpdateSuccess() throws AdjustmentNotFoundException {
        AdjustmentDto inputAdjustmentDto = AdjustmentDto.builder()
                .id(1)
                .stdDeduction(true)
                .build();

        Adjustment inputAdjustment = Adjustment.builder()
                .id(1)
                .stdDeduction(true)
                .build();

        Adjustment updatedAdjustment = Adjustment.builder()
                .id(1)
                .stdDeduction(true)
                .build();

        AdjustmentDto updatedAdjustmentDto = AdjustmentDto.builder()
                .id(1)
                .stdDeduction(true)
                .build();

        when(adjustmentRepository.existsById(ArgumentMatchers.any(Long.class))).thenReturn(true);
        when(adjustmentMapper.mapFrom(ArgumentMatchers.any(AdjustmentDto.class))).thenReturn(inputAdjustment);
        when(adjustmentRepository.save(ArgumentMatchers.any(Adjustment.class))).thenReturn(updatedAdjustment);
        when(adjustmentMapper.mapTo(ArgumentMatchers.any(Adjustment.class))).thenReturn(updatedAdjustmentDto);

        assertEquals(1, adjustmentService.fullUpdate(inputAdjustmentDto).getId());
        assertTrue(adjustmentService.fullUpdate(inputAdjustmentDto).isStdDeduction());
    }

    @Test
    void deleteFailNotFoundEx() {
        Adjustment nonExistingAdjustment = Adjustment.builder()
                .id(1)
                .build();

        when(adjustmentRepository.existsById(ArgumentMatchers.any(Long.class))).thenReturn(false);
        verify(adjustmentRepository, times(0)).deleteById(nonExistingAdjustment.getId());

        assertThrows(AdjustmentNotFoundException.class, () ->
            adjustmentService.delete(nonExistingAdjustment.getId())
        );
    }

    @Test
    void deleteSuccess() throws AdjustmentNotFoundException {
        Adjustment existingAdjustment = Adjustment.builder()
                .id(1)
                .build();

        when(adjustmentRepository.existsById(ArgumentMatchers.any(Long.class))).thenReturn(true);
        adjustmentService.delete(existingAdjustment.getId());
        verify(adjustmentRepository, times(1)).deleteById(existingAdjustment.getId());
    }

}