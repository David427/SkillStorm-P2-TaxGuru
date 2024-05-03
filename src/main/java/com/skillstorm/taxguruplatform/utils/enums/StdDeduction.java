package com.skillstorm.taxguruplatform.utils.enums;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public enum StdDeduction {

    SINGLE_2023(new BigDecimal("13850.00")),
    HOH_2023(new BigDecimal("20800.00")),
    SINGLE_2022(new BigDecimal("12950.00")),
    HOH_2022(new BigDecimal("19400.00"));

    private final BigDecimal deduction;

    StdDeduction(BigDecimal deduction) {
        this.deduction = deduction;
    }

}
