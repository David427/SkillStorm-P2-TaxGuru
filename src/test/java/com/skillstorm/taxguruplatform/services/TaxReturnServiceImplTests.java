package com.skillstorm.taxguruplatform.services;

import com.skillstorm.taxguruplatform.domain.dtos.TaxReturnDto;
import com.skillstorm.taxguruplatform.domain.entities.Adjustment;
import com.skillstorm.taxguruplatform.domain.entities.Form1099;
import com.skillstorm.taxguruplatform.domain.entities.FormW2;
import com.skillstorm.taxguruplatform.domain.entities.TaxReturn;
import com.skillstorm.taxguruplatform.exceptions.ResultCalculationException;
import com.skillstorm.taxguruplatform.exceptions.TaxReturnAlreadyExistsException;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
class TaxReturnServiceImplTests {

    @Mock
    private TaxReturnRepository taxReturnRepository;

    @Mock
    private TaxReturnMapperImpl taxReturnMapper;

    @InjectMocks
    private TaxReturnServiceImpl taxReturnService;

    @Test
    void createFailAlreadyExistsEx() {
        TaxReturnDto inputTaxReturnDto = TaxReturnDto.builder()
                .id(1L)
                .build();

        when(taxReturnRepository.existsById(ArgumentMatchers.any(Long.class))).thenReturn(true);

        assertThrows(TaxReturnAlreadyExistsException.class, () ->
                taxReturnService.create(inputTaxReturnDto)
        );
    }

    @Test
    void createSuccess() throws TaxReturnAlreadyExistsException {
        TaxReturnDto inputTaxReturnDto = TaxReturnDto.builder()
                .build();

        TaxReturn inputTaxReturn = TaxReturn.builder()
                .build();

        TaxReturn createdTaxReturn = TaxReturn.builder()
                .id(1L)
                .build();

        TaxReturnDto createdTaxReturnDto = TaxReturnDto.builder()
                .id(1L)
                .build();

        when(taxReturnRepository.existsById(ArgumentMatchers.any(Long.class))).thenReturn(false);
        when(taxReturnMapper.mapFrom(ArgumentMatchers.any(TaxReturnDto.class))).thenReturn(inputTaxReturn);
        when(taxReturnRepository.save(ArgumentMatchers.any(TaxReturn.class))).thenReturn(createdTaxReturn);
        when(taxReturnMapper.mapTo(ArgumentMatchers.any(TaxReturn.class))).thenReturn(createdTaxReturnDto);

        assertEquals(1, taxReturnService.create(inputTaxReturnDto).getId());
    }

    @Test
    void fullUpdateFailNotFoundEx() {
        TaxReturnDto inputTaxReturnDto = TaxReturnDto.builder()
                .id(1L)
                .build();

        when(taxReturnRepository.existsById(ArgumentMatchers.any(Long.class))).thenReturn(false);

        assertThrows(TaxReturnNotFoundException.class, () ->
                taxReturnService.fullUpdate(inputTaxReturnDto)
        );
    }

    @Test
    void fullUpdateSuccess() throws TaxReturnNotFoundException {
        TaxReturnDto inputTaxReturnDto = TaxReturnDto.builder()
                .id(1L)
                .filingStatus("Head of Household")
                .build();

        TaxReturn inputTaxReturn = TaxReturn.builder()
                .id(1L)
                .filingStatus("Head of Household")
                .build();

        TaxReturn updatedTaxReturn = TaxReturn.builder()
                .id(1L)
                .filingStatus("Head of Household")
                .build();

        TaxReturnDto updatedTaxReturnDto = TaxReturnDto.builder()
                .id(1L)
                .filingStatus("Head of Household")
                .build();

        when(taxReturnRepository.existsById(ArgumentMatchers.any(Long.class))).thenReturn(true);
        when(taxReturnMapper.mapFrom(ArgumentMatchers.any(TaxReturnDto.class))).thenReturn(inputTaxReturn);
        when(taxReturnRepository.save(ArgumentMatchers.any(TaxReturn.class))).thenReturn(updatedTaxReturn);
        when(taxReturnMapper.mapTo(ArgumentMatchers.any(TaxReturn.class))).thenReturn(updatedTaxReturnDto);

        assertEquals(1, taxReturnService.fullUpdate(inputTaxReturnDto).getId());
        assertEquals("Head of Household", taxReturnService.fullUpdate(inputTaxReturnDto).getFilingStatus());
    }

    @Test
    void deleteSuccess() throws TaxReturnNotFoundException {
        TaxReturn existingTaxReturn = TaxReturn.builder()
                .id(1L)
                .build();

        when(taxReturnRepository.existsById(ArgumentMatchers.any(Long.class))).thenReturn(true);
        taxReturnService.delete(existingTaxReturn.getId());
        verify(taxReturnRepository, times(1)).deleteById(existingTaxReturn.getId());
    }

    @Test
    void deleteFailNotFoundEx() {
        TaxReturn nonExistingTaxReturn = TaxReturn.builder()
                .id(1L)
                .build();

        when(taxReturnRepository.existsById(ArgumentMatchers.any(Long.class))).thenReturn(false);
        verify(taxReturnRepository, times(0)).deleteById(nonExistingTaxReturn.getId());

        assertThrows(TaxReturnNotFoundException.class, () ->
                taxReturnService.delete(nonExistingTaxReturn.getId())
        );
    }

    @Test
    void calculateResultFailNotFoundEx() {
        long id = 1L;

        when(taxReturnRepository.findById(ArgumentMatchers.any(Long.class))).thenReturn(Optional.empty());

        assertThrows(TaxReturnNotFoundException.class, () ->
                taxReturnService.calculateResult(id)
        );
    }

    @Test
    void calculateResultFailNoFilingStatusEx() {
        TaxReturn taxReturn = TaxReturn.builder()
                .id(1L)
                .build();

        when(taxReturnRepository.findById(ArgumentMatchers.any(Long.class))).thenReturn(Optional.of(taxReturn));

        Exception exception = assertThrows(ResultCalculationException.class, () ->
                taxReturnService.calculateResult(taxReturn.getId())
        );
        assertEquals("Filing status not found.", exception.getMessage());
    }

    @Test
    void calculateAdjGrossIncomeFailNoIncomeFoundEx() {
        TaxReturn taxReturn = TaxReturn.builder()
                .build();

        Exception exception = assertThrows(ResultCalculationException.class, () ->
                taxReturnService.calculateAdjGrossIncome(taxReturn)
        );
        assertEquals("No income found.", exception.getMessage());
    }

    @Test
    void calculateAdjGrossIncomeSingleW2Only() throws ResultCalculationException {
        FormW2 formW2 = FormW2.builder()
                .income(new BigDecimal("72600.00"))
                .ssTaxWithheld(new BigDecimal("4501.20"))
                .mediTaxWithheld(new BigDecimal("1052.70"))
                .build();

        TaxReturn taxReturn = TaxReturn.builder()
                .formW2(formW2)
                .build();

        assertEquals(new BigDecimal("67046.10"), taxReturnService.calculateAdjGrossIncome(taxReturn));
    }

    @Test
    void calculateAdjGrossIncomeSingle1099Only() throws ResultCalculationException {
        Form1099 form1099 = Form1099.builder()
                .income(new BigDecimal("72600.00"))
                .build();

        Adjustment adjustment = Adjustment.builder()
                .stdDeduction(true)
                .build();

        TaxReturn taxReturn = TaxReturn.builder()
                .form1099(form1099)
                .adjustment(adjustment)
                .build();

        assertEquals(new BigDecimal("61492.20000"), taxReturnService.calculateAdjGrossIncome(taxReturn));
    }

    @Test
    void calculateAdjGrossIncomeSingleW2And1099() throws ResultCalculationException {
        Form1099 form1099 = Form1099.builder()
                .income(new BigDecimal("5800.00"))
                .build();

        FormW2 formW2 = FormW2.builder()
                .income(new BigDecimal("72600.00"))
                .ssTaxWithheld(new BigDecimal("4501.20"))
                .mediTaxWithheld(new BigDecimal("1052.70"))
                .build();

        Adjustment adjustment = Adjustment.builder()
                .stdDeduction(true)
                .build();

        TaxReturn taxReturn = TaxReturn.builder()
                .form1099(form1099)
                .formW2(formW2)
                .adjustment(adjustment)
                .build();

        assertEquals(new BigDecimal("72846.10"), taxReturnService.calculateAdjGrossIncome(taxReturn));
    }

    @Test
    void calculateAdjGrossIncomeMarriedW2Only() throws ResultCalculationException {
        FormW2 formW2 = FormW2.builder()
                .income(new BigDecimal("72600.00"))
                .ssTaxWithheld(new BigDecimal("4501.20"))
                .mediTaxWithheld(new BigDecimal("1052.70"))
                .build();

        TaxReturn taxReturn = TaxReturn.builder()
                .spouseAgi(new BigDecimal("20000.00"))
                .formW2(formW2)
                .build();

        assertEquals(new BigDecimal("87046.10"), taxReturnService.calculateAdjGrossIncome(taxReturn));
    }

    @Test
    void calculateAdjGrossIncomeMarried1099Only() throws ResultCalculationException {
        Form1099 form1099 = Form1099.builder()
                .income(new BigDecimal("72600.00"))
                .build();

        Adjustment adjustment = Adjustment.builder()
                .stdDeduction(true)
                .build();

        TaxReturn taxReturn = TaxReturn.builder()
                .spouseAgi(new BigDecimal("20000.00"))
                .form1099(form1099)
                .adjustment(adjustment)
                .build();

        assertEquals(new BigDecimal("81492.20000"), taxReturnService.calculateAdjGrossIncome(taxReturn));
    }

    @Test
    void calculateAdjGrossIncomeMarriedW2And1099() throws ResultCalculationException {
        Form1099 form1099 = Form1099.builder()
                .income(new BigDecimal("5800.00"))
                .build();

        FormW2 formW2 = FormW2.builder()
                .income(new BigDecimal("72600.00"))
                .ssTaxWithheld(new BigDecimal("4501.20"))
                .mediTaxWithheld(new BigDecimal("1052.70"))
                .build();

        Adjustment adjustment = Adjustment.builder()
                .stdDeduction(true)
                .build();

        TaxReturn taxReturn = TaxReturn.builder()
                .spouseAgi(new BigDecimal("20000.00"))
                .form1099(form1099)
                .formW2(formW2)
                .adjustment(adjustment)
                .build();

        assertEquals(new BigDecimal("92846.10"), taxReturnService.calculateAdjGrossIncome(taxReturn));
    }

    @Test
    void calculateTaxWithheldFailNoIncomeFoundEx() {
        TaxReturn taxReturn = TaxReturn.builder()
                .build();

        Exception exception = assertThrows(ResultCalculationException.class, () ->
                taxReturnService.calculateTaxWithheld(taxReturn)
        );
        assertEquals("No income found.", exception.getMessage());
    }

    @Test
    void calculateTaxWithheldSingleW2Only() throws ResultCalculationException {
        FormW2 formW2 = FormW2.builder()
                .fedTaxWithheld(new BigDecimal("5000.00"))
                .build();

        TaxReturn taxReturn = TaxReturn.builder()
                .id(1L)
                .formW2(formW2)
                .build();

        assertEquals(new BigDecimal("5000.00"), taxReturnService.calculateTaxWithheld(taxReturn));
    }

    @Test
    void calculateTaxWithheldSingle1099Only() throws ResultCalculationException {
        Form1099 form1099 = Form1099.builder()
                .fedTaxWithheld(new BigDecimal("5000.00"))
                .build();

        TaxReturn taxReturn = TaxReturn.builder()
                .id(1L)
                .form1099(form1099)
                .build();

        assertEquals(new BigDecimal("5000.00"), taxReturnService.calculateTaxWithheld(taxReturn));
    }

    @Test
    void calculateTaxWithheldSingleW2And1099() throws ResultCalculationException {
        Form1099 form1099 = Form1099.builder()
                .fedTaxWithheld(new BigDecimal("5000.00"))
                .build();

        FormW2 formW2 = FormW2.builder()
                .fedTaxWithheld(new BigDecimal("2000.00"))
                .build();

        TaxReturn taxReturn = TaxReturn.builder()
                .form1099(form1099)
                .formW2(formW2)
                .build();

        assertEquals(new BigDecimal("7000.00"), taxReturnService.calculateTaxWithheld(taxReturn));
    }

    @Test
    void calculateTaxWithheldMarriedW2Only() throws ResultCalculationException {
        FormW2 formW2 = FormW2.builder()
                .fedTaxWithheld(new BigDecimal("5000.00"))
                .build();

        TaxReturn taxReturn = TaxReturn.builder()
                .spouseTaxWithheld(new BigDecimal("2500.00"))
                .formW2(formW2)
                .build();

        assertEquals(new BigDecimal("7500.00"), taxReturnService.calculateTaxWithheld(taxReturn));
    }

    @Test
    void calculateTaxWithheldMarried1099Only() throws ResultCalculationException {
        Form1099 form1099 = Form1099.builder()
                .fedTaxWithheld(new BigDecimal("5000.00"))
                .build();

        TaxReturn taxReturn = TaxReturn.builder()
                .spouseTaxWithheld(new BigDecimal("2500.00"))
                .form1099(form1099)
                .build();

        assertEquals(new BigDecimal("7500.00"), taxReturnService.calculateTaxWithheld(taxReturn));
    }

    @Test
    void calculateTaxWithheldMarriedW2And1099() throws ResultCalculationException {
        Form1099 form1099 = Form1099.builder()
                .fedTaxWithheld(new BigDecimal("5000.00"))
                .build();

        FormW2 formW2 = FormW2.builder()
                .fedTaxWithheld(new BigDecimal("2000.00"))
                .build();

        TaxReturn taxReturn = TaxReturn.builder()
                .spouseTaxWithheld(new BigDecimal("2500.00"))
                .form1099(form1099)
                .formW2(formW2)
                .build();

        assertEquals(new BigDecimal("9500.00"), taxReturnService.calculateTaxWithheld(taxReturn));
    }

    @Test
    void calculateTaxableIncomeFailNoAdjustmentEx() {
        BigDecimal totalIncome = new BigDecimal("10000.00");

        TaxReturn taxReturn = TaxReturn.builder()
                .filingStatus("Single")
                .build();

        Exception exception = assertThrows(ResultCalculationException.class, () ->
                taxReturnService.calculateTaxableIncome(taxReturn, totalIncome)
        );
        assertEquals("Adjustment data not found.", exception.getMessage());
    }

    @Test
    void calculateTaxableIncomeFailInvalidFilingStatusEx() {
        BigDecimal totalIncome = new BigDecimal("10000.00");

        Adjustment adjustment = Adjustment.builder()
                .stdDeduction(true)
                .build();

        TaxReturn taxReturn = TaxReturn.builder()
                .filingStatus("Married With Children")
                .adjustment(adjustment)
                .build();

        Exception exception = assertThrows(ResultCalculationException.class, () ->
                taxReturnService.calculateTaxableIncome(taxReturn, totalIncome)
        );
        assertEquals("Invalid filing status.", exception.getMessage());
    }


    @Test
    void calculateTaxableIncomeSingleOrMfs() throws ResultCalculationException {
        BigDecimal totalIncome = new BigDecimal("50000.00");

        Adjustment adjustment = Adjustment.builder()
                .stdDeduction(true)
                .build();

        TaxReturn taxReturnSingle = TaxReturn.builder()
                .filingStatus("Single")
                .adjustment(adjustment)
                .build();

        TaxReturn taxReturnMfs = TaxReturn.builder()
                .filingStatus("Married, Filing Separately")
                .adjustment(adjustment)
                .build();

        assertEquals(new BigDecimal("36150.00"), taxReturnService.calculateTaxableIncome(taxReturnSingle, totalIncome));
        assertEquals(new BigDecimal("36150.00"), taxReturnService.calculateTaxableIncome(taxReturnMfs, totalIncome));
    }

    @Test
    void calculateTaxableIncomeMarriedOrQss() throws ResultCalculationException {
        BigDecimal totalIncome = new BigDecimal("80000.00");

        Adjustment adjustment = Adjustment.builder()
                .stdDeduction(true)
                .build();

        TaxReturn taxReturnMarried = TaxReturn.builder()
                .filingStatus("Married, Filing Jointly")
                .adjustment(adjustment)
                .build();

        TaxReturn taxReturnQss = TaxReturn.builder()
                .filingStatus("Qualifying Surviving Spouse")
                .adjustment(adjustment)
                .build();

        assertEquals(new BigDecimal("52300.00"), taxReturnService.calculateTaxableIncome(taxReturnMarried, totalIncome));
        assertEquals(new BigDecimal("52300.00"), taxReturnService.calculateTaxableIncome(taxReturnQss, totalIncome));
    }

    @Test
    void calculateTaxableIncomeHeadofHousehold() throws ResultCalculationException {
        BigDecimal totalIncome = new BigDecimal("50000.00");

        Adjustment adjustment = Adjustment.builder()
                .stdDeduction(true)
                .build();

        TaxReturn taxReturn = TaxReturn.builder()
                .filingStatus("Head of Household")
                .adjustment(adjustment)
                .build();

        assertEquals(new BigDecimal("29200.00"), taxReturnService.calculateTaxableIncome(taxReturn, totalIncome));
    }

    @Test
    void calculateTaxLiabilitySingle() {
        BigDecimal taxableIncome = new BigDecimal("600000.00");

        TaxReturn taxReturn = TaxReturn.builder()
                .filingStatus("Single")
                .build();

        assertEquals(new BigDecimal("182331.7327"), taxReturnService.calculateTaxLiability(taxReturn, taxableIncome));
    }

    @Test
    void calculateTaxLiabilityMarriedFilingSeparately() {
        BigDecimal taxableIncome = new BigDecimal("350000.00");

        TaxReturn taxReturn = TaxReturn.builder()
                .filingStatus("Married, Filing Separately")
                .build();

        assertEquals(new BigDecimal("94456.7327"), taxReturnService.calculateTaxLiability(taxReturn, taxableIncome));
    }

    @Test
    void calculateTaxLiabilityMarriedFilingJointlyOrQss() {
        BigDecimal taxableIncome = new BigDecimal("700000.00");

        TaxReturn taxReturnMarried = TaxReturn.builder()
                .filingStatus("Married, Filing Jointly")
                .build();

        TaxReturn taxReturnQss = TaxReturn.builder()
                .filingStatus("Qualifying Surviving Spouse")
                .build();

        assertEquals(new BigDecimal("188913.7327"), taxReturnService.calculateTaxLiability(taxReturnMarried, taxableIncome));
        assertEquals(new BigDecimal("188913.7327"), taxReturnService.calculateTaxLiability(taxReturnQss, taxableIncome));
    }

    @Test
    void calculateTaxLiabilityHeadOfHousehold() {
        BigDecimal taxableIncome = new BigDecimal("580000.00");

        TaxReturn taxReturn = TaxReturn.builder()
                .filingStatus("Head of Household")
                .build();

        assertEquals(new BigDecimal("173326.2327"), taxReturnService.calculateTaxLiability(taxReturn, taxableIncome));
    }

}