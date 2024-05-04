package com.skillstorm.taxguruplatform.services;

import com.skillstorm.taxguruplatform.domain.dtos.TaxReturnDto;
import com.skillstorm.taxguruplatform.domain.entities.TaxReturn;
import com.skillstorm.taxguruplatform.exceptions.ResultCalculationException;
import com.skillstorm.taxguruplatform.exceptions.TaxReturnAlreadyExistsException;
import com.skillstorm.taxguruplatform.exceptions.TaxReturnNotFoundException;
import com.skillstorm.taxguruplatform.repositories.TaxReturnRepository;
import com.skillstorm.taxguruplatform.utils.enums.SsMedicareTaxRate;
import com.skillstorm.taxguruplatform.utils.enums.StdDeduction;
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
        Optional<TaxReturn> foundTaxReturn = taxReturnRepository.findById(id);

        if (foundTaxReturn.isEmpty()) {
            throw new TaxReturnNotFoundException("Tax return not found.");
        }

        if (foundTaxReturn.get().getFilingStatus() == null) {
            throw new ResultCalculationException("Filing status not found.");
        }

        TaxReturn taxReturn = foundTaxReturn.get();

        BigDecimal totalIncome = calculateTotalIncome(taxReturn);
        BigDecimal totalTaxWithheld = calculateTotalTaxWithheld(taxReturn);
        BigDecimal taxableIncome = calculateTaxableIncome(taxReturn, totalIncome);
        BigDecimal totalTaxOwed = calculateTotalTaxOwed(taxReturn, taxableIncome);
        BigDecimal result = totalTaxOwed.subtract(totalTaxWithheld);

        taxReturn.setTotalIncome(totalIncome);
        taxReturn.setTotalTaxWithheld(totalTaxWithheld);
        taxReturn.setTaxableIncome(taxableIncome);
        taxReturn.setTotalTaxOwed(totalTaxOwed);
        taxReturn.setReturnResult(result);

        taxReturnRepository.save(taxReturn);
        return taxReturnMapper.mapTo(taxReturn);
    }

    public BigDecimal calculateTotalIncome(TaxReturn taxReturn) throws ResultCalculationException {
        BigDecimal independentSsTaxRate = SsMedicareTaxRate.SS_TAX_RATE.getTaxRate();
        BigDecimal independentMediTaxRate = SsMedicareTaxRate.MEDICARE_TAX_RATE.getTaxRate();

        if (taxReturn.getFormW2() == null && taxReturn.getForm1099() == null) {
            throw new ResultCalculationException("No income found.");
        }

        /*
         *  - For a 1099, pay the full rate for SS (12.4%) and Medicare (2.9%) tax.
         *  - For W2s, the employer pays half of the rate.
         *  - If someone has a W2 and a 1099, they only pay half of the rate.
         */
        if (taxReturn.getFormW2() != null && taxReturn.getForm1099() == null) {
            return taxReturn.getFormW2().getIncome()
                    .subtract(taxReturn.getFormW2().getSsTaxWithheld())
                    .subtract(taxReturn.getFormW2().getMediTaxWithheld());
        }

        if (taxReturn.getFormW2() == null) {
            BigDecimal form1099Income = taxReturn.getForm1099().getIncome();
            return taxReturn.getForm1099().getIncome()
                    .subtract(form1099Income.multiply(independentSsTaxRate))
                    .subtract(form1099Income.multiply(independentMediTaxRate));
        }

        return taxReturn.getFormW2().getIncome()
                .add(taxReturn.getForm1099().getIncome())
                .subtract(taxReturn.getFormW2().getSsTaxWithheld())
                .subtract(taxReturn.getFormW2().getMediTaxWithheld());

    }

    public BigDecimal calculateTotalTaxWithheld(TaxReturn taxReturn) throws ResultCalculationException {
        if (taxReturn.getFormW2() == null && taxReturn.getForm1099() == null) {
            throw new ResultCalculationException("No income found.");
        }

        if (taxReturn.getFormW2() != null && taxReturn.getForm1099() == null) {
            return taxReturn.getFormW2().getFedTaxWithheld();
        }

        if (taxReturn.getFormW2() == null) {
            return taxReturn.getForm1099().getFedTaxWithheld();
        }

        return taxReturn.getFormW2().getFedTaxWithheld()
                .add(taxReturn.getForm1099().getFedTaxWithheld());
    }

    public BigDecimal calculateTaxableIncome(TaxReturn taxReturn, BigDecimal totalIncome) throws ResultCalculationException {
        String filingStatus = taxReturn.getFilingStatus();
        BigDecimal deductionSingle2023 = StdDeduction.SINGLE_2023.getDeduction();
        BigDecimal deductionHoh2023 = StdDeduction.HOH_2023.getDeduction();

        if (taxReturn.getAdjustment() == null) {
            throw new ResultCalculationException("Adjustment data not found.");
        }

        if (filingStatus.equals("Single") || filingStatus.equals("Married")) {
            if (taxReturn.getAdjustment().isStdDeduction()) {
                return totalIncome.subtract(deductionSingle2023);
            }
        } else if (filingStatus.equals("Head of Household")) {
            if (taxReturn.getAdjustment().isStdDeduction()) {
                return totalIncome.subtract(deductionHoh2023);
            }
        }

        throw new ResultCalculationException("Invalid filing status.");
    }

    public BigDecimal calculateTotalTaxOwed(TaxReturn taxReturn, BigDecimal taxableIncome) {
        String filingStatus = taxReturn.getFilingStatus();
        BigDecimal bracket1 = TaxBracket.BRACKET_LOWEST.getRatePercent();
        BigDecimal bracket2 = TaxBracket.BRACKET_LOW.getRatePercent();
        BigDecimal bracket3 = TaxBracket.BRACKET_MED_LOW.getRatePercent();
        BigDecimal bracket4 = TaxBracket.BRACKET_MED.getRatePercent();
        BigDecimal bracket5 = TaxBracket.BRACKET_MED_HIGH.getRatePercent();
        BigDecimal bracket6 = TaxBracket.BRACKET_HIGH.getRatePercent();
        BigDecimal bracket7 = TaxBracket.BRACKET_HIGHEST.getRatePercent();
        BigDecimal bracketAmount;
        BigDecimal taxedAmount = new BigDecimal("0.0");
        BigDecimal remainingAmount = taxableIncome;

        /*
         *  - Calculate from highest bracket to lowest!
         *  - To get the taxed amount in a certain bracket, subtract the highest amount of the NEXT bracket
         *    from the current (remaining) amount.
         */
        switch (filingStatus) {
            case "Single" -> {
                if (taxableIncome.doubleValue() >= 578126.00) {
                    bracketAmount = taxableIncome.subtract(new BigDecimal("578125.99"));
                    taxedAmount = taxedAmount.add(bracketAmount.multiply(bracket7));
                    remainingAmount = taxableIncome.subtract(bracketAmount);
                }

                if (remainingAmount.doubleValue() <= 578125.99 && remainingAmount.doubleValue() >= 231251.00) {
                    bracketAmount = remainingAmount.subtract(new BigDecimal("231250.99"));
                    taxedAmount = taxedAmount.add(bracketAmount.multiply(bracket6));
                    remainingAmount = remainingAmount.subtract(bracketAmount);
                }

                if (remainingAmount.doubleValue() <= 231250.99 && remainingAmount.doubleValue() >= 182101.00) {
                    bracketAmount = remainingAmount.subtract(new BigDecimal("182100.99"));
                    taxedAmount = taxedAmount.add(bracketAmount.multiply(bracket5));
                    remainingAmount = remainingAmount.subtract(bracketAmount);
                }

                if (remainingAmount.doubleValue() <= 182100.99 && remainingAmount.doubleValue() >= 95376.00) {
                    bracketAmount = remainingAmount.subtract(new BigDecimal("95375.99"));
                    taxedAmount = taxedAmount.add(bracketAmount.multiply(bracket4));
                    remainingAmount = remainingAmount.subtract(bracketAmount);
                }

                if (remainingAmount.doubleValue() <= 95375.99 && remainingAmount.doubleValue() >= 44726.00) {
                    bracketAmount = remainingAmount.subtract(new BigDecimal("44725.99"));
                    taxedAmount = taxedAmount.add(bracketAmount.multiply(bracket3));
                    remainingAmount = remainingAmount.subtract(bracketAmount);
                }

                if (remainingAmount.doubleValue() <= 44725.99 && remainingAmount.doubleValue() >= 11001.00) {
                    bracketAmount = remainingAmount.subtract(new BigDecimal("11000.99"));
                    taxedAmount = taxedAmount.add(bracketAmount.multiply(bracket2));
                    remainingAmount = remainingAmount.subtract(bracketAmount);
                }

                if (remainingAmount.doubleValue() <= 11000.99) {
                    bracketAmount = remainingAmount;
                    taxedAmount = taxedAmount.add(bracketAmount.multiply(bracket1));
                }
            }
            case "Married" -> {
                if (taxableIncome.doubleValue() >= 346876.00) {
                    bracketAmount = taxableIncome.subtract(new BigDecimal("346875.99"));
                    taxedAmount = taxedAmount.add(bracketAmount.multiply(bracket7));
                    remainingAmount = taxableIncome.subtract(bracketAmount);
                }

                if (remainingAmount.doubleValue() <= 346875.99 && remainingAmount.doubleValue() >= 231251.00) {
                    bracketAmount = remainingAmount.subtract(new BigDecimal("231250.99"));
                    taxedAmount = taxedAmount.add(bracketAmount.multiply(bracket6));
                    remainingAmount = remainingAmount.subtract(bracketAmount);
                }

                if (remainingAmount.doubleValue() <= 231250.99 && remainingAmount.doubleValue() >= 182101.00) {
                    bracketAmount = remainingAmount.subtract(new BigDecimal("182100.99"));
                    taxedAmount = taxedAmount.add(bracketAmount.multiply(bracket5));
                    remainingAmount = remainingAmount.subtract(bracketAmount);
                }

                if (remainingAmount.doubleValue() <= 182100.99 && remainingAmount.doubleValue() >= 95376.00) {
                    bracketAmount = remainingAmount.subtract(new BigDecimal("95375.99"));
                    taxedAmount = taxedAmount.add(bracketAmount.multiply(bracket4));
                    remainingAmount = remainingAmount.subtract(bracketAmount);
                }

                if (remainingAmount.doubleValue() <= 95375.99 && remainingAmount.doubleValue() >= 44726.00) {
                    bracketAmount = remainingAmount.subtract(new BigDecimal("44725.99"));
                    taxedAmount = taxedAmount.add(bracketAmount.multiply(bracket3));
                    remainingAmount = remainingAmount.subtract(bracketAmount);
                }

                if (remainingAmount.doubleValue() <= 44725.99 && remainingAmount.doubleValue() >= 11001.00) {
                    bracketAmount = remainingAmount.subtract(new BigDecimal("11000.99"));
                    taxedAmount = taxedAmount.add(bracketAmount.multiply(bracket2));
                    remainingAmount = remainingAmount.subtract(bracketAmount);
                }

                if (remainingAmount.doubleValue() <= 11000.99) {
                    bracketAmount = remainingAmount;
                    taxedAmount = taxedAmount.add(bracketAmount.multiply(bracket1));
                }
            }
            case "Head of Household" -> {
                if (taxableIncome.doubleValue() >= 578101.00) {
                    bracketAmount = taxableIncome.subtract(new BigDecimal("578100.99"));
                    taxedAmount = taxedAmount.add(bracketAmount.multiply(bracket7));
                    remainingAmount = taxableIncome.subtract(bracketAmount);
                }

                if (remainingAmount.doubleValue() <= 578100.99 && remainingAmount.doubleValue() >= 231251.00) {
                    bracketAmount = remainingAmount.subtract(new BigDecimal("231250.99"));
                    taxedAmount = taxedAmount.add(bracketAmount.multiply(bracket6));
                    remainingAmount = remainingAmount.subtract(bracketAmount);
                }

                if (remainingAmount.doubleValue() <= 231250.99 && remainingAmount.doubleValue() >= 182101.00) {
                    bracketAmount = remainingAmount.subtract(new BigDecimal("182100.99"));
                    taxedAmount = taxedAmount.add(bracketAmount.multiply(bracket5));
                    remainingAmount = remainingAmount.subtract(bracketAmount);
                }

                if (remainingAmount.doubleValue() <= 182100.99 && remainingAmount.doubleValue() >= 95351.00) {
                    bracketAmount = remainingAmount.subtract(new BigDecimal("95350.99"));
                    taxedAmount = taxedAmount.add(bracketAmount.multiply(bracket4));
                    remainingAmount = remainingAmount.subtract(bracketAmount);
                }

                if (remainingAmount.doubleValue() <= 95350.99 && remainingAmount.doubleValue() >= 59851.00) {
                    bracketAmount = remainingAmount.subtract(new BigDecimal("59850.99"));
                    taxedAmount = taxedAmount.add(bracketAmount.multiply(bracket3));
                    remainingAmount = remainingAmount.subtract(bracketAmount);
                }

                if (remainingAmount.doubleValue() <= 59850.99 && remainingAmount.doubleValue() >= 15701.00) {
                    bracketAmount = remainingAmount.subtract(new BigDecimal("15700.99"));
                    taxedAmount = taxedAmount.add(bracketAmount.multiply(bracket2));
                    remainingAmount = remainingAmount.subtract(bracketAmount);
                }

                if (remainingAmount.doubleValue() <= 15700.99) {
                    bracketAmount = remainingAmount;
                    taxedAmount = taxedAmount.add(bracketAmount.multiply(bracket1));
                }
            }
        }

        return taxedAmount;
    }

}
