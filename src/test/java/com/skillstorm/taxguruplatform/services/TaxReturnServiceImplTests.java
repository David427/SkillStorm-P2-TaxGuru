package com.skillstorm.taxguruplatform.services;

import com.skillstorm.taxguruplatform.domain.dtos.TaxReturnDto;
import com.skillstorm.taxguruplatform.domain.entities.Adjustment;
import com.skillstorm.taxguruplatform.domain.entities.FormW2;
import com.skillstorm.taxguruplatform.domain.entities.TaxReturn;
import com.skillstorm.taxguruplatform.exceptions.ResultCalculationException;
import com.skillstorm.taxguruplatform.exceptions.TaxReturnNotFoundException;
import com.skillstorm.taxguruplatform.repositories.TaxReturnRepository;
import com.skillstorm.taxguruplatform.utils.mappers.TaxReturnMapperImpl;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TaxReturnServiceImplTests {

    @Mock
    private TaxReturnRepository taxReturnRepository;

    @Mock
    private TaxReturnMapperImpl taxReturnMapper;

    @InjectMocks
    private TaxReturnServiceImpl taxReturnService;

    @Test
    void testThatSingleW2ResultCalcSucceeds() throws ResultCalculationException, TaxReturnNotFoundException {
        FormW2 formW2 = FormW2.builder()
                .income(new BigDecimal("72600.00"))
                // This user paid 15% of their gross income to Federal tax
                .fedTaxWithheld(new BigDecimal("10890.00"))
                .ssTaxWithheld(new BigDecimal("4501.20"))
                .mediTaxWithheld(new BigDecimal("1052.70"))
                .build();

        Adjustment adjustment = Adjustment.builder()
                .stdDeduction(true)
                .build();

        TaxReturn inputTaxReturn = TaxReturn.builder()
                .filingStatus("Single")
                .formW2(formW2)
                .adjustment(adjustment)
                .build();

        TaxReturn outputTaxReturn = TaxReturn.builder()
                .id(1)
                .filingStatus("Single")
                .formW2(formW2)
                .adjustment(adjustment)
                .totalIncome(new BigDecimal("67046.10"))
                .totalTaxWithheld(new BigDecimal("10890.00"))
                .totalTaxOwed(new BigDecimal("6870.53"))
                .build();

        TaxReturnDto taxReturnDto = TaxReturnDto.builder()
                .build();

        assert inputTaxReturn != null;
        when(taxReturnRepository.findById(ArgumentMatchers.any(Long.class))).thenReturn(Optional.of(inputTaxReturn));
        when(taxReturnRepository.save(ArgumentMatchers.any(TaxReturn.class))).thenReturn(outputTaxReturn);
        when(taxReturnMapper.mapTo(ArgumentMatchers.any(TaxReturn.class))).thenReturn(taxReturnDto);

        BigDecimal totalIncome = taxReturnService.calculateResult(outputTaxReturn.getId()).getTotalIncome();
        BigDecimal totalTaxWithheld = taxReturnService.calculateResult(outputTaxReturn.getId()).getTotalTaxWithheld();
        BigDecimal totalTaxOwed = taxReturnService.calculateResult(outputTaxReturn.getId()).getTotalIncome();

        // Total income: 72600.00 - SS tax - Medicare tax = 67046.10
        assertEquals(totalIncome, new BigDecimal("67046.10"));
        assertEquals(totalTaxWithheld, new BigDecimal("10890.00"));
        assertEquals(totalTaxOwed, new BigDecimal("6870.53"));
    }

    @Test
    void testThatSingle1099ResultCalcSucceeds() throws ResultCalculationException, TaxReturnNotFoundException {
        FormW2 formW2 = FormW2.builder()
                .income(new BigDecimal("72600.00"))
                .fedTaxWithheld(new BigDecimal("4501.20"))
                .ssTaxWithheld(new BigDecimal("4501.20"))
                .mediTaxWithheld(new BigDecimal("1052.70"))
                .build();

        Adjustment adjustment = Adjustment.builder()
                .stdDeduction(true)
                .build();

        TaxReturn taxReturn = TaxReturn.builder()
                .id(1)
                .filingStatus("Single")
                .formW2(formW2)
                .adjustment(adjustment)
                .build();

        TaxReturnDto taxReturnDto = TaxReturnDto.builder()
                // Expected value
                .returnResult(new BigDecimal("8092.39"))
                .build();

        when(taxReturnRepository.findById(ArgumentMatchers.any(Long.class))).thenReturn(Optional.ofNullable(taxReturn));
        when(taxReturnMapper.mapTo(ArgumentMatchers.any(TaxReturn.class))).thenReturn(taxReturnDto);

        assert taxReturn != null;
        BigDecimal totalTaxOwed = taxReturnService.calculateResult(taxReturn.getId()).getTotalTaxOwed();

        assertEquals(totalTaxOwed, new BigDecimal("8092.39"));
    }

}
