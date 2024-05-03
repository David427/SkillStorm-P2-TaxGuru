package com.skillstorm.taxguruplatform.services;

import com.skillstorm.taxguruplatform.domain.dtos.TaxReturnDto;
import com.skillstorm.taxguruplatform.domain.entities.TaxReturn;
import com.skillstorm.taxguruplatform.exceptions.ResultCalculationException;
import com.skillstorm.taxguruplatform.exceptions.TaxReturnAlreadyExistsException;
import com.skillstorm.taxguruplatform.exceptions.TaxReturnNotFoundException;
import com.skillstorm.taxguruplatform.repositories.TaxReturnRepository;
import com.skillstorm.taxguruplatform.utils.enums.TaxBracket;
import com.skillstorm.taxguruplatform.utils.mappers.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class TaxReturnServiceImpl implements TaxReturnService {

    private final TaxReturnRepository taxReturnRepository;
    private final Mapper<TaxReturn, TaxReturnDto> taxReturnMapper;

    @Autowired
    public TaxReturnServiceImpl(TaxReturnRepository taxReturnRepository, Mapper<TaxReturn, TaxReturnDto> taxReturnMapper) {
        this.taxReturnRepository = taxReturnRepository;
        this.taxReturnMapper = taxReturnMapper;
    }

    @Override
    public TaxReturnDto create(TaxReturnDto taxReturnDto) throws TaxReturnAlreadyExistsException {
        if (isExisting(taxReturnDto.getId())) {
            throw new TaxReturnAlreadyExistsException("Tax return already exists.");
        }

        TaxReturn createdTaxReturn = taxReturnRepository.save(taxReturnMapper.mapFrom(taxReturnDto));
        return taxReturnMapper.mapTo(createdTaxReturn);
    }

    @Override
    public TaxReturnDto fullUpdate(TaxReturnDto taxReturnDto) throws TaxReturnNotFoundException {
        if (isExisting(taxReturnDto.getId())) {
            TaxReturn updatedTaxReturn = taxReturnRepository.save(taxReturnMapper.mapFrom(taxReturnDto));
            return taxReturnMapper.mapTo(updatedTaxReturn);
        } else {
            throw new TaxReturnNotFoundException("Tax return not found.");
        }
    }

    @Override
    public void delete(long id) throws TaxReturnNotFoundException {
        if (isExisting(id)) {
            taxReturnRepository.deleteById(id);
        } else {
            throw new TaxReturnNotFoundException("Tax return not found.");
        }
    }

    @Override
    public boolean isExisting(long id) {
        return taxReturnRepository.existsById(id);
    }

    @Override
    public TaxReturnDto calculateResult(long id) throws TaxReturnNotFoundException, ResultCalculationException {

        /*
         *  ORDER OF OPERATIONS
         *  1. calculateTotalIncome()
         *      - Gathered from spouse income  + related W2 and/or 1099 info
         *  2. getTaxBracket()
         *      - Use total income and filing status
         *  3. calculateTotalTaxWithheld()
         *      - Gathered from spouse tax withheld + related W2 and/or 1099 info
         *  4. calculateTaxableIncome()
         *      - Determines taxable income based on credits and deductions, including standard deduction
         *  5. Calculate the result: compare taxable income with adjustments (credits, deductions) applied
         *    to total tax withheld.
         *      - If taxable income is higher, result is negative and they owe taxes; if it's lower, then the
         *        result is positive and they get a refund.
         *  6. Store the result in the db & return a DTO with it.
         */

        Optional<TaxReturn> foundTaxReturn = taxReturnRepository.findById(id);

        if (foundTaxReturn.isEmpty()) {
            throw new TaxReturnNotFoundException("Tax return not found.");
        }

        TaxReturn taxReturn = foundTaxReturn.get();

        BigDecimal totalIncome = calculateTotalIncome(taxReturn);
        BigDecimal totalTaxWithheld = calculateTotalTaxWithheld(taxReturn);
        BigDecimal taxableIncome = calculateTaxableIncome(taxReturn, totalIncome);
        BigDecimal taxedIncome = calculateTaxedIncome(taxReturn, taxableIncome);
        BigDecimal result = taxedIncome.subtract(totalTaxWithheld);

        taxReturn.setTotalIncome(totalIncome);
        taxReturn.setTotalTaxWithheld(totalTaxWithheld);
        taxReturn.setReturnResult(result);

        // taxReturnRepository.save(taxReturn);
        return taxReturnMapper.mapTo(taxReturn);
    }

    public BigDecimal calculateTotalIncome(TaxReturn taxReturn) throws ResultCalculationException {
        if (taxReturn.getFilingStatus().equals("Married")) {
            if (taxReturn.getFormW2() != null && taxReturn.getForm1099() == null) {
                return taxReturn.getSpouseTotalIncome().add(taxReturn.getFormW2().getIncome());
            } else if (taxReturn.getFormW2() == null && taxReturn.getForm1099() != null) {
                return taxReturn.getSpouseTotalIncome().add(taxReturn.getForm1099().getIncome());
            } else if (taxReturn.getFormW2() != null) {
                return taxReturn.getSpouseTotalIncome()
                        .add(taxReturn.getFormW2().getIncome())
                        .add(taxReturn.getForm1099().getIncome());
            }
        } else if (taxReturn.getFilingStatus().equals("Single") || taxReturn.getFilingStatus().equals("Head of Household")) {
            if (taxReturn.getFormW2() != null && taxReturn.getForm1099() == null) {
                return taxReturn.getFormW2().getIncome();
            } else if (taxReturn.getFormW2() == null && taxReturn.getForm1099() != null) {
                return taxReturn.getForm1099().getIncome();
            } else if (taxReturn.getFormW2() != null) {
                return taxReturn.getFormW2().getIncome().add(taxReturn.getForm1099().getIncome());
            }
        }

        throw new ResultCalculationException("Invalid filing status.");
    }

    public BigDecimal calculateTotalTaxWithheld(TaxReturn taxReturn) throws ResultCalculationException {
        if (taxReturn.getFilingStatus().equals("Married")) {
            if (taxReturn.getFormW2() != null && taxReturn.getForm1099() == null) {
                return taxReturn.getSpouseTotalTaxWithheld()
                        .add(taxReturn.getFormW2().getFedTaxWithheld())
                        .add(taxReturn.getFormW2().getSsTaxWithheld())
                        .add(taxReturn.getFormW2().getMediTaxWithheld());
            } else if (taxReturn.getFormW2() == null && taxReturn.getForm1099() != null) {
                return taxReturn.getSpouseTotalIncome()
                        .add(taxReturn.getForm1099().getFedTaxWithheld());
            } else if (taxReturn.getFormW2() != null) {
                return taxReturn.getSpouseTotalTaxWithheld()
                        .add(taxReturn.getFormW2().getFedTaxWithheld())
                        .add(taxReturn.getFormW2().getSsTaxWithheld())
                        .add(taxReturn.getFormW2().getMediTaxWithheld())
                        .add(taxReturn.getForm1099().getFedTaxWithheld());
            }
        } else if (taxReturn.getFilingStatus().equals("Single") || taxReturn.getFilingStatus().equals("Head of Household")) {
            if (taxReturn.getFormW2() != null && taxReturn.getForm1099() == null) {
                return taxReturn.getFormW2().getFedTaxWithheld()
                        .add(taxReturn.getFormW2().getSsTaxWithheld())
                        .add(taxReturn.getFormW2().getMediTaxWithheld());
            } else if (taxReturn.getFormW2() == null && taxReturn.getForm1099() != null) {
                return taxReturn.getForm1099().getFedTaxWithheld();
            } else if (taxReturn.getFormW2() != null) {
                return taxReturn.getFormW2().getFedTaxWithheld()
                        .add(taxReturn.getFormW2().getSsTaxWithheld())
                        .add(taxReturn.getFormW2().getMediTaxWithheld())
                        .add(taxReturn.getForm1099().getFedTaxWithheld());
            }
        }

        throw new ResultCalculationException("Invalid filing status.");
    }

    public BigDecimal calculateTaxableIncome(TaxReturn taxReturn, BigDecimal totalIncome) throws ResultCalculationException {
        if (taxReturn.getFilingStatus().equals("Single") || taxReturn.getFilingStatus().equals("Married")) {
            if (taxReturn.getAdjustment().isStdDeduction()) {
                return totalIncome.subtract(new BigDecimal("14600.00"));
            }
        } else if (taxReturn.getFilingStatus().equals("Head of Household")) {
            return totalIncome.subtract(new BigDecimal("21900.00"));
        }

        throw new ResultCalculationException("Invalid filing status.");
    }

    private BigDecimal calculateTaxedIncome(TaxReturn taxReturn, BigDecimal taxableIncome) {
        BigDecimal bracket1 = TaxBracket.TEN_PERCENT.getRatePercent();
        BigDecimal bracket2 = TaxBracket.TWELVE_PERCENT.getRatePercent();
        BigDecimal bracket3 = TaxBracket.TWENTY_TWO_PERCENT.getRatePercent();
        BigDecimal bracket4 = TaxBracket.TWENTY_FOUR_PERCENT.getRatePercent();
        BigDecimal bracket5 = TaxBracket.THIRTY_TWO_PERCENT.getRatePercent();
        BigDecimal bracket6 = TaxBracket.THIRTY_FIVE_PERCENT.getRatePercent();
        BigDecimal bracket7 = TaxBracket.THIRTY_SEVEN_PERCENT.getRatePercent();
        BigDecimal bracketAmount;
        BigDecimal remainingAmount = taxableIncome;
        BigDecimal taxedIncome = new BigDecimal("0.0");

        // Calculate from highest bracket to lowest!
        if (taxReturn.getFilingStatus().equals("Single")) {
            if (taxableIncome.doubleValue() >= 578126.00) {
                // Subtract the highest amount of the NEXT tax bracket
                bracketAmount = taxableIncome.subtract(new BigDecimal("578125.99"));
                taxedIncome = taxedIncome.add(bracketAmount.multiply(bracket7));
                remainingAmount = taxableIncome.subtract(bracketAmount);
            }

            if (remainingAmount.doubleValue() <= 578125.99 && remainingAmount.doubleValue() >= 231251.00) {
                bracketAmount = remainingAmount.subtract(new BigDecimal("231250.99"));
                taxedIncome = taxedIncome.add(bracketAmount.multiply(bracket6));
                remainingAmount = remainingAmount.subtract(bracketAmount);
            }

            if (remainingAmount.doubleValue() <= 231250.99 && remainingAmount.doubleValue() >= 182101.00) {
                bracketAmount = remainingAmount.subtract(new BigDecimal("182100.99"));
                taxedIncome = taxedIncome.add(bracketAmount.multiply(bracket5));
                remainingAmount = remainingAmount.subtract(bracketAmount);
            }

            if (remainingAmount.doubleValue() <= 182100.99 && remainingAmount.doubleValue() >= 95376.00) {
                bracketAmount = remainingAmount.subtract(new BigDecimal("95375.99"));
                taxedIncome = taxedIncome.add(bracketAmount.multiply(bracket4));
                remainingAmount = remainingAmount.subtract(bracketAmount);
            }

            if (remainingAmount.doubleValue() <= 95375.99 && remainingAmount.doubleValue() >= 44726.00) {
                bracketAmount = remainingAmount.subtract(new BigDecimal("44725.99"));
                taxedIncome = taxedIncome.add(bracketAmount.multiply(bracket3));
                remainingAmount = remainingAmount.subtract(bracketAmount);
            }

            if (remainingAmount.doubleValue() <= 44725.99 && remainingAmount.doubleValue() >= 11000.00) {
                bracketAmount = remainingAmount.subtract(new BigDecimal("10999.99"));
                taxedIncome = taxedIncome.add(bracketAmount.multiply(bracket2));
                remainingAmount = remainingAmount.subtract(bracketAmount);
            }

            if (remainingAmount.doubleValue() <= 10999.99) {
                bracketAmount = remainingAmount;
                taxedIncome = taxedIncome.add(bracketAmount.multiply(bracket1));
            }
        }

        return taxedIncome;
    }

}
