package com.skillstorm.taxguruplatform.utils.enums;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public enum TaxBracket {

    TEN_PERCENT(new BigDecimal("0.10")),
    TWELVE_PERCENT(new BigDecimal("0.12")),
    TWENTY_TWO_PERCENT(new BigDecimal("0.22")),
    TWENTY_FOUR_PERCENT(new BigDecimal("0.24")),
    THIRTY_TWO_PERCENT(new BigDecimal("0.32")),
    THIRTY_FIVE_PERCENT(new BigDecimal("0.35")),
    THIRTY_SEVEN_PERCENT(new BigDecimal("0.37"));

    private final BigDecimal ratePercent;

    TaxBracket(BigDecimal ratePercent) {
        this.ratePercent = ratePercent;
    }

}
