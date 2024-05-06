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
    private Long id;

    private String taxYear;
    private String filingStatus;
    private Boolean dependent;
    private BigDecimal spouseAgi;
    private BigDecimal spouseTaxWithheld;
    private BigDecimal adjGrossIncome;
    private BigDecimal taxWithheld;
    private BigDecimal taxableIncome;
    private BigDecimal taxLiability;
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
