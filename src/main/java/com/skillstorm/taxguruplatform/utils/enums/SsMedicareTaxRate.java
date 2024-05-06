package com.skillstorm.taxguruplatform.utils.enums;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public enum SsMedicareTaxRate {

    SS_TAX_RATE(new BigDecimal("0.124")),
    MEDICARE_TAX_RATE(new BigDecimal("0.029"));

    private final BigDecimal taxRate;

    SsMedicareTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

}
