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
import com.skillstorm.taxguruplatform.utils.enums.Credits;
import com.skillstorm.taxguruplatform.utils.enums.SsMedicareTaxRate;
import com.skillstorm.taxguruplatform.utils.enums.StdDeduction;
import com.skillstorm.taxguruplatform.utils.enums.TaxBracket;
import com.skillstorm.taxguruplatform.utils.mappers.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

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
    public void delete(Long id) throws TaxReturnNotFoundException {
        if (isExisting(id)) {
            taxReturnRepository.deleteById(id);
        } else {
            throw new TaxReturnNotFoundException("Tax return not found.");
        }
    }

    @Override
    public boolean isExisting(Long id) {
        if (id != null) return taxReturnRepository.existsById(id);
        return false;
    }

    @Override
    public TaxReturnDto calculateResult(Long id) throws TaxReturnNotFoundException, ResultCalculationException {
        Optional<TaxReturn> foundTaxReturn = taxReturnRepository.findById(id);
        Set<String> validFilingStatuses = Set.of(
                "Single",
                "Married, Filing Separately",
                "Married, Filing Jointly",
                "Qualifying Surviving Spouse",
                "Head of Household"
        );

        if (foundTaxReturn.isEmpty()) {
            throw new TaxReturnNotFoundException("Tax return not found.");
        }
        if (foundTaxReturn.get().getFilingStatus() == null) {
            throw new ResultCalculationException("Filing status not found.");
        }

        if (!validFilingStatuses.contains(foundTaxReturn.get().getFilingStatus())) {
            throw new ResultCalculationException("Invalid filing status.");
        }

        if (foundTaxReturn.get().getAdjustment() == null) {
            throw new ResultCalculationException("Adjustment data not found.");
        }

        TaxReturn taxReturn = foundTaxReturn.get();

        BigDecimal adjGrossIncome = calculateAdjGrossIncome(taxReturn);
        BigDecimal taxWithheld = calculateTaxWithheld(taxReturn);

        // Credits reduce the amount of tax due; deductions reduce taxable income
        Adjustment calculatedAdj = calculateCredits(taxReturn, adjGrossIncome);
        taxReturn.setAdjustment(calculatedAdj);

        BigDecimal taxableIncome = calculateTaxableIncome(taxReturn, adjGrossIncome);
        BigDecimal taxLiability = calculateTaxLiability(taxReturn, taxableIncome);
        BigDecimal result = taxWithheld.subtract(taxLiability.abs());

        taxReturn.setAdjGrossIncome(adjGrossIncome);
        taxReturn.setTaxWithheld(taxWithheld);
        taxReturn.setTaxableIncome(taxableIncome);
        taxReturn.setTaxLiability(taxLiability);
        taxReturn.setReturnResult(result);

        taxReturnRepository.save(taxReturn);
        return taxReturnMapper.mapTo(taxReturn);
    }

    public BigDecimal calculateAdjGrossIncome(TaxReturn taxReturn) throws ResultCalculationException {
        Optional<FormW2> formW2 = Optional.ofNullable(taxReturn.getFormW2());
        Optional<Form1099> form1099 = Optional.ofNullable(taxReturn.getForm1099());
        Optional<BigDecimal> spouseAgi = Optional.ofNullable(taxReturn.getSpouseAgi());
        BigDecimal independentSsTaxRate = SsMedicareTaxRate.SS_TAX_RATE.getTaxRate();
        BigDecimal independentMediTaxRate = SsMedicareTaxRate.MEDICARE_TAX_RATE.getTaxRate();

        if (formW2.isEmpty() && form1099.isEmpty()) {
            throw new ResultCalculationException("No income found.");
        }

        /*
         *  - For a 1099, pay the full rate for SS (12.4%) and Medicare (2.9%) tax.
         *  - For W2s, the employer pays half of the rate.
         *  - If someone has a W2 and a 1099, they only pay half of the rate.
         */
        if (formW2.isPresent() && form1099.isEmpty()) {
            if (spouseAgi.isPresent()) {
                return formW2.get().getIncome()
                        .subtract(formW2.get().getSsTaxWithheld())
                        .subtract(formW2.get().getMediTaxWithheld())
                        .add(spouseAgi.get());
            } else {
                return formW2.get().getIncome()
                        .subtract(formW2.get().getSsTaxWithheld())
                        .subtract(formW2.get().getMediTaxWithheld());
            }
        } else if (formW2.isEmpty()) {
            if (spouseAgi.isPresent()) {
                return form1099.get().getIncome()
                        .subtract(form1099.get().getIncome().multiply(independentSsTaxRate))
                        .subtract(form1099.get().getIncome().multiply(independentMediTaxRate))
                        .add(spouseAgi.get());
            } else {
                return form1099.get().getIncome()
                        .subtract(form1099.get().getIncome().multiply(independentSsTaxRate))
                        .subtract(form1099.get().getIncome().multiply(independentMediTaxRate));
            }
        } else {
            if (spouseAgi.isPresent()) {
                return taxReturn.getFormW2().getIncome()
                        .add(taxReturn.getForm1099().getIncome())
                        .subtract(taxReturn.getFormW2().getSsTaxWithheld())
                        .subtract(taxReturn.getFormW2().getMediTaxWithheld())
                        .add(spouseAgi.get());
            } else {
                return taxReturn.getFormW2().getIncome()
                        .add(taxReturn.getForm1099().getIncome())
                        .subtract(taxReturn.getFormW2().getSsTaxWithheld())
                        .subtract(taxReturn.getFormW2().getMediTaxWithheld());
            }
        }
    }

    public BigDecimal calculateTaxWithheld(TaxReturn taxReturn) throws ResultCalculationException {
        Optional<FormW2> formW2 = Optional.ofNullable(taxReturn.getFormW2());
        Optional<Form1099> form1099 = Optional.ofNullable(taxReturn.getForm1099());
        Optional<BigDecimal> spouseTaxWithheld = Optional.ofNullable(taxReturn.getSpouseTaxWithheld());

        if (formW2.isEmpty() && form1099.isEmpty()) {
            throw new ResultCalculationException("No income found.");
        }

        if (formW2.isPresent() && form1099.isEmpty()) {
            if (spouseTaxWithheld.isPresent()) {
                return formW2.get().getFedTaxWithheld()
                        .add(spouseTaxWithheld.get());
            } else {
                return formW2.get().getFedTaxWithheld();
            }
        } else if (formW2.isEmpty()) {
            if (spouseTaxWithheld.isPresent()) {
                return form1099.get().getFedTaxWithheld()
                        .add(spouseTaxWithheld.get());
            } else {
                return form1099.get().getFedTaxWithheld();
            }
        } else {
            if (spouseTaxWithheld.isPresent()) {
                return formW2.get().getFedTaxWithheld()
                        .add(form1099.get().getFedTaxWithheld())
                        .add(spouseTaxWithheld.get());
            } else {
                return formW2.get().getFedTaxWithheld()
                        .add(form1099.get().getFedTaxWithheld());
            }
        }
    }

    public Adjustment calculateCredits(TaxReturn taxReturn, BigDecimal adjGrossIncome) throws ResultCalculationException {
        String filingStatus = taxReturn.getFilingStatus();
        Integer claimedDependents = taxReturn.getAdjustment().getClaimedDependents();
        Boolean retirementWorkPlan = taxReturn.getAdjustment().getRetirementWorkPlan();
        BigDecimal iraContribution = taxReturn.getAdjustment().getIraContribution();
        BigDecimal eitcAgiLimit0Deps = Credits.EITC_0DEP.getAgiLimit();
        BigDecimal eitcAgiLimit1Deps = Credits.EITC_1DEP.getAgiLimit();
        BigDecimal eitcAgiLimit2Deps = Credits.EITC_2DEP.getAgiLimit();
        BigDecimal eitcAgiLimit3Deps = Credits.EITC_3DEP.getAgiLimit();
        BigDecimal eitcAgiLimitMfj0Deps = Credits.EITC_MFJ_0DEP.getAgiLimit();
        BigDecimal eitcAgiLimitMfj1Deps = Credits.EITC_MFJ_1DEP.getAgiLimit();
        BigDecimal eitcAgiLimitMfj2Deps = Credits.EITC_MFJ_2DEP.getAgiLimit();
        BigDecimal eitcAgiLimitMfj3Deps = Credits.EITC_MFJ_3DEP.getAgiLimit();
        BigDecimal eitcCredit0Deps = Credits.EITC_0DEP.getCreditAmount();
        BigDecimal eitcCredit1Deps = Credits.EITC_1DEP.getCreditAmount();
        BigDecimal eitcCredit2Deps = Credits.EITC_2DEP.getCreditAmount();
        BigDecimal eitcCredit3Deps = Credits.EITC_3DEP.getCreditAmount();
        BigDecimal childCreditAgiLimit = Credits.CHILD_CREDIT.getAgiLimit();
        BigDecimal childCreditAgiLimitMfj = Credits.CHILD_CREDIT_MFJ.getAgiLimit();
        BigDecimal childCreditAmount = Credits.CHILD_CREDIT.getCreditAmount();
        BigDecimal iraWorkPlanAgiLimit = Credits.IRA_WORK_PLAN.getAgiLimit();
        BigDecimal iraWorkPlanAgiLimitMfj = Credits.IRA_WORK_PLAN_MFJ.getAgiLimit();
        BigDecimal iraContributionLimit = Credits.IRA_WORK_PLAN_MFJ.getMaxCredit();
        Adjustment calculatedAdj = taxReturn.getAdjustment();

        if (claimedDependents == null) {
            throw new ResultCalculationException("Invalid number of claimed dependents.");
        }

        if (retirementWorkPlan == null) {
            throw new ResultCalculationException("Invalid retirement work plan status.");
        }

        if (iraContribution == null) {
            throw new ResultCalculationException("Invalid IRA contribution amount.");
        }

        // EITC
        switch (filingStatus) {
            case "Married, Filing Jointly" -> {
                if (claimedDependents == 0 && adjGrossIncome.doubleValue() <= eitcAgiLimitMfj0Deps.doubleValue()) {
                    calculatedAdj.setEitcAmount(eitcCredit0Deps);
                    calculatedAdj.setEarnedIncomeCredit(true);
                } else if (adjGrossIncome.doubleValue() >= eitcAgiLimitMfj0Deps.doubleValue()) {
                    calculatedAdj.setEitcAmount(new BigDecimal("0.00"));
                    calculatedAdj.setEarnedIncomeCredit(false);
                }

                if (claimedDependents == 1 && adjGrossIncome.doubleValue() <= eitcAgiLimitMfj1Deps.doubleValue()) {
                    calculatedAdj.setEitcAmount(eitcCredit1Deps);
                    calculatedAdj.setEarnedIncomeCredit(true);
                } else if (adjGrossIncome.doubleValue() >= eitcAgiLimitMfj1Deps.doubleValue()) {
                    calculatedAdj.setEitcAmount(new BigDecimal("0.00"));
                    calculatedAdj.setEarnedIncomeCredit(false);
                }

                if (claimedDependents == 2 && adjGrossIncome.doubleValue() <= eitcAgiLimitMfj2Deps.doubleValue()) {
                    calculatedAdj.setEitcAmount(eitcCredit2Deps);
                    calculatedAdj.setEarnedIncomeCredit(true);
                } else if (adjGrossIncome.doubleValue() >= eitcAgiLimitMfj2Deps.doubleValue()) {
                    calculatedAdj.setEitcAmount(new BigDecimal("0.00"));
                    calculatedAdj.setEarnedIncomeCredit(false);
                }

                if (claimedDependents >= 3 && adjGrossIncome.doubleValue() <= eitcAgiLimitMfj3Deps.doubleValue()) {
                    calculatedAdj.setEitcAmount(eitcCredit3Deps);
                    calculatedAdj.setEarnedIncomeCredit(true);
                } else if (adjGrossIncome.doubleValue() >= eitcAgiLimitMfj3Deps.doubleValue()) {
                    calculatedAdj.setEitcAmount(new BigDecimal("0.00"));
                    calculatedAdj.setEarnedIncomeCredit(false);
                }
            }
            case "Single", "Married, Filing Separately", "Head of Household", "Qualifying Surviving Spouse" -> {
                if (claimedDependents == 0 && adjGrossIncome.doubleValue() <= eitcAgiLimit0Deps.doubleValue()) {
                    calculatedAdj.setEitcAmount(eitcCredit0Deps);
                    calculatedAdj.setEarnedIncomeCredit(true);
                } else if (adjGrossIncome.doubleValue() >= eitcAgiLimit0Deps.doubleValue()) {
                    calculatedAdj.setEitcAmount(new BigDecimal("0.00"));
                    calculatedAdj.setEarnedIncomeCredit(false);
                }

                if (claimedDependents == 1 && adjGrossIncome.doubleValue() <= eitcAgiLimit1Deps.doubleValue()) {
                    calculatedAdj.setEitcAmount(eitcCredit1Deps);
                    calculatedAdj.setEarnedIncomeCredit(true);
                } else if (adjGrossIncome.doubleValue() >= eitcAgiLimit1Deps.doubleValue()) {
                    calculatedAdj.setEitcAmount(new BigDecimal("0.00"));
                    calculatedAdj.setEarnedIncomeCredit(false);
                }

                if (claimedDependents == 2 && adjGrossIncome.doubleValue() <= eitcAgiLimit2Deps.doubleValue()) {
                    calculatedAdj.setEitcAmount(eitcCredit2Deps);
                    calculatedAdj.setEarnedIncomeCredit(true);
                } else if (adjGrossIncome.doubleValue() >= eitcAgiLimit2Deps.doubleValue()) {
                    calculatedAdj.setEitcAmount(new BigDecimal("0.00"));
                    calculatedAdj.setEarnedIncomeCredit(false);
                }

                if (claimedDependents >= 3 && adjGrossIncome.doubleValue() <= eitcAgiLimit3Deps.doubleValue()) {
                    calculatedAdj.setEitcAmount(eitcCredit3Deps);
                    calculatedAdj.setEarnedIncomeCredit(true);
                } else if (adjGrossIncome.doubleValue() >= eitcAgiLimit3Deps.doubleValue()) {
                    calculatedAdj.setEitcAmount(new BigDecimal("0.00"));
                    calculatedAdj.setEarnedIncomeCredit(false);
                }
            }
        }

        // CHILD TAX CREDIT
        if (filingStatus.equals("Married, Filing Jointly")) {
            if (adjGrossIncome.doubleValue() <= childCreditAgiLimitMfj.doubleValue()) {
                calculatedAdj.setChildCreditAmount(childCreditAmount.multiply(BigDecimal.valueOf(claimedDependents)));
            }
        } else {
            if (adjGrossIncome.doubleValue() <= childCreditAgiLimit.doubleValue()) {
                calculatedAdj.setChildCreditAmount(childCreditAmount.multiply(BigDecimal.valueOf(claimedDependents)));
            }
        }

        // RETIREMENT CREDIT
        if (iraContribution.doubleValue() == 0) {
            calculatedAdj.setRetirementCreditAmount(new BigDecimal("0.00"));
        } else if (filingStatus.equals("Married, Filing Jointly") && retirementWorkPlan) {
            if (adjGrossIncome.doubleValue() <= iraWorkPlanAgiLimitMfj.doubleValue()) {
                if (iraContribution.doubleValue() <= iraContributionLimit.doubleValue()) {
                    calculatedAdj.setRetirementCreditAmount(iraContribution);
                } else {
                    throw new ResultCalculationException("IRA contribution exceeded " + iraContributionLimit.doubleValue() + ".");
                }
            } else {
                calculatedAdj.setRetirementCreditAmount(new BigDecimal("0.00"));
            }
        } else if (retirementWorkPlan) {
            if (adjGrossIncome.doubleValue() <= iraWorkPlanAgiLimit.doubleValue()) {
                if (iraContribution.doubleValue() <= iraContributionLimit.doubleValue()) {
                    calculatedAdj.setRetirementCreditAmount(iraContribution);
                } else {
                    throw new ResultCalculationException("IRA contribution exceeded " + iraContributionLimit.doubleValue() + ".");
                }
            } else {
                calculatedAdj.setRetirementCreditAmount(new BigDecimal("0.00"));
            }
        } else {
            if (adjGrossIncome.doubleValue() <= iraWorkPlanAgiLimit.doubleValue()) {
                if (iraContribution.doubleValue() <= iraContributionLimit.doubleValue()) {
                    calculatedAdj.setRetirementCreditAmount(iraContribution);
                } else {
                    throw new ResultCalculationException("IRA contribution exceeded " + iraContributionLimit.doubleValue() + ".");
                }
            } else {
                calculatedAdj.setRetirementCreditAmount(new BigDecimal("0.00"));
            }
        }

        return calculatedAdj;
    }

    public BigDecimal calculateTaxableIncome(TaxReturn taxReturn, BigDecimal totalIncome) throws ResultCalculationException {
        String filingStatus = taxReturn.getFilingStatus();
        BigDecimal deductionSingle2023 = StdDeduction.SINGLE_MFS_2023.getDeduction();
        BigDecimal deductionMarried2023 = StdDeduction.MARRIED_QSS_2023.getDeduction();
        BigDecimal deductionHoh2023 = StdDeduction.HOH_2023.getDeduction();

        if (taxReturn.getAdjustment() == null) {
            throw new ResultCalculationException("Adjustment data not found.");
        }

        switch (filingStatus) {
            case "Single", "Married, Filing Separately" -> {
                if (taxReturn.getAdjustment().getStdDeduction()) {
                    return totalIncome.subtract(deductionSingle2023);
                }
            }
            case "Married, Filing Jointly", "Qualifying Surviving Spouse" -> {
                if (taxReturn.getAdjustment().getStdDeduction()) {
                    return totalIncome.subtract(deductionMarried2023);
                }
            }
            case "Head of Household" -> {
                if (taxReturn.getAdjustment().getStdDeduction()) {
                    return totalIncome.subtract(deductionHoh2023);
                }
            }
        }

        throw new ResultCalculationException("Invalid filing status.");
    }

    public BigDecimal calculateTaxLiability(TaxReturn taxReturn, BigDecimal taxableIncome) {
        String filingStatus = taxReturn.getFilingStatus();
        Adjustment creditedAdjustment = taxReturn.getAdjustment();
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

                taxedAmount = taxedAmount
                        .subtract(creditedAdjustment.getEitcAmount())
                        .subtract(creditedAdjustment.getChildCreditAmount())
                        .subtract(creditedAdjustment.getRetirementCreditAmount());
            }
            case "Married, Filing Separately" -> {
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

                taxedAmount = taxedAmount
                        .subtract(creditedAdjustment.getEitcAmount())
                        .subtract(creditedAdjustment.getChildCreditAmount())
                        .subtract(creditedAdjustment.getRetirementCreditAmount());

            }
            case "Married, Filing Jointly", "Qualifying Surviving Spouse" -> {
                if (taxableIncome.doubleValue() >= 693751.00) {
                    bracketAmount = taxableIncome.subtract(new BigDecimal("693750.99"));
                    taxedAmount = taxedAmount.add(bracketAmount.multiply(bracket7));
                    remainingAmount = taxableIncome.subtract(bracketAmount);
                }

                if (remainingAmount.doubleValue() <= 693750.99 && remainingAmount.doubleValue() >= 462501.00) {
                    bracketAmount = remainingAmount.subtract(new BigDecimal("462500.99"));
                    taxedAmount = taxedAmount.add(bracketAmount.multiply(bracket6));
                    remainingAmount = remainingAmount.subtract(bracketAmount);
                }

                if (remainingAmount.doubleValue() <= 462500.99 && remainingAmount.doubleValue() >= 364201.00) {
                    bracketAmount = remainingAmount.subtract(new BigDecimal("364200.99"));
                    taxedAmount = taxedAmount.add(bracketAmount.multiply(bracket5));
                    remainingAmount = remainingAmount.subtract(bracketAmount);
                }

                if (remainingAmount.doubleValue() <= 364200.99 && remainingAmount.doubleValue() >= 190751.00) {
                    bracketAmount = remainingAmount.subtract(new BigDecimal("190750.99"));
                    taxedAmount = taxedAmount.add(bracketAmount.multiply(bracket4));
                    remainingAmount = remainingAmount.subtract(bracketAmount);
                }

                if (remainingAmount.doubleValue() <= 190750.99 && remainingAmount.doubleValue() >= 89451.00) {
                    bracketAmount = remainingAmount.subtract(new BigDecimal("89450.99"));
                    taxedAmount = taxedAmount.add(bracketAmount.multiply(bracket3));
                    remainingAmount = remainingAmount.subtract(bracketAmount);
                }

                if (remainingAmount.doubleValue() <= 89450.99 && remainingAmount.doubleValue() >= 22001.00) {
                    bracketAmount = remainingAmount.subtract(new BigDecimal("22000.99"));
                    taxedAmount = taxedAmount.add(bracketAmount.multiply(bracket2));
                    remainingAmount = remainingAmount.subtract(bracketAmount);
                }

                if (remainingAmount.doubleValue() <= 22000.99) {
                    bracketAmount = remainingAmount;
                    taxedAmount = taxedAmount.add(bracketAmount.multiply(bracket1));
                }

                taxedAmount = taxedAmount
                        .subtract(creditedAdjustment.getEitcAmount())
                        .subtract(creditedAdjustment.getChildCreditAmount())
                        .subtract(creditedAdjustment.getRetirementCreditAmount());
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

                taxedAmount = taxedAmount
                        .subtract(creditedAdjustment.getEitcAmount())
                        .subtract(creditedAdjustment.getChildCreditAmount())
                        .subtract(creditedAdjustment.getRetirementCreditAmount());
            }
        }

        return taxedAmount;
    }

}
