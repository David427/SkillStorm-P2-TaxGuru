package com.skillstorm.taxguruplatform.utils.enums;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public enum TaxBracket {

    BRACKET_LOWEST(new BigDecimal("0.10")),
    BRACKET_LOW(new BigDecimal("0.12")),
    BRACKET_MED_LOW(new BigDecimal("0.22")),
    BRACKET_MED(new BigDecimal("0.24")),
    BRACKET_MED_HIGH(new BigDecimal("0.32")),
    BRACKET_HIGH(new BigDecimal("0.35")),
    BRACKET_HIGHEST(new BigDecimal("0.37"));

    private final BigDecimal ratePercent;

    TaxBracket(BigDecimal ratePercent) {
        this.ratePercent = ratePercent;
    }

}
