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
    public void testThatResultCalculationSingleW2Succeeds() throws ResultCalculationException, TaxReturnNotFoundException {
        FormW2 formW2 = FormW2.builder()
                // Taxable income = income + standard deduction (14600.00)
                .income(new BigDecimal("72600.00"))
                .fedTaxWithheld(new BigDecimal("1.00"))
                .ssTaxWithheld(new BigDecimal("1.00"))
                .mediTaxWithheld(new BigDecimal("1.00"))
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
        BigDecimal result = taxReturnService.calculateResult(taxReturn.getId()).getReturnResult();

        assertEquals(result, new BigDecimal("8092.39"));
    }

}
