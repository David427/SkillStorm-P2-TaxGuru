package com.skillstorm.taxguruplatform.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "adjustments")
public class Adjustment {

    @Id
    @SequenceGenerator(
            name = "adjustments_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "adjustments_id_seq")
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
