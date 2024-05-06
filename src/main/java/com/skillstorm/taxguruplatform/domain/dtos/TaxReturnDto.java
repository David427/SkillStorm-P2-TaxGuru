package com.skillstorm.taxguruplatform.domain.dtos;

import com.skillstorm.taxguruplatform.domain.entities.Adjustment;
import com.skillstorm.taxguruplatform.domain.entities.Form1099;
import com.skillstorm.taxguruplatform.domain.entities.FormW2;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxReturnDto {

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
    private FormW2 formW2;
    private Form1099 form1099;
    private Adjustment adjustment;

}
