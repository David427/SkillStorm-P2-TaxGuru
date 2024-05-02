package com.skillstorm.taxguruplatform.domain.dtos;

import com.skillstorm.taxguruplatform.domain.entities.AdjustmentData;
import com.skillstorm.taxguruplatform.domain.entities.AppUser;
import com.skillstorm.taxguruplatform.domain.entities.Form1099;
import com.skillstorm.taxguruplatform.domain.entities.FormW2;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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

    private long id;
    private String filingStatus;
    private boolean dependent;
    private short claimedDependents;
    private BigDecimal totalIncome;
    private BigDecimal totalTaxWithheld;
    private BigDecimal spouseTotalIncome;
    private BigDecimal spouseTotalTaxWithheld;
    private BigDecimal returnResult;
    private FormW2 formW2;
    private Form1099 form1099;
    private AdjustmentData adjustmentData;

}
