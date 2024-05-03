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
@Table(name = "tax_returns")
public class TaxReturn {

    @Id
    @SequenceGenerator(
            name = "tax_returns_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tax_returns_id_seq")
    private long id;

    private String filingStatus;
    private boolean dependent;
    private short claimedDependents;
    private BigDecimal totalIncome;
    private BigDecimal totalTaxWithheld;
    private BigDecimal spouseTotalIncome;
    private BigDecimal spouseTotalTaxWithheld;
    private BigDecimal totalTaxOwed;
    private BigDecimal returnResult;

    @OneToOne
    @JoinColumn(name = "form_w2_id", referencedColumnName = "id")
    private FormW2 formW2;

    @OneToOne
    @JoinColumn(name = "form_1099_id", referencedColumnName = "id")
    private Form1099 form1099;

    @OneToOne
    @JoinColumn(name = "adjustments_id", referencedColumnName = "id")
    private Adjustment adjustment;

}
