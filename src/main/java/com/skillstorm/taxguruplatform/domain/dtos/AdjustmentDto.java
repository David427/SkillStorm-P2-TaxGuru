package com.skillstorm.taxguruplatform.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdjustmentDto {

    private Long id;
    private Boolean stdDeduction;
    private Integer claimedDependents;
    private Boolean earnedIncomeCredit;
    private BigDecimal eitcAmount;
    private BigDecimal childCreditAmount;
    private Boolean retirementWorkPlan;
    private BigDecimal iraContribution;
    private BigDecimal retirementCreditAmount;

}
