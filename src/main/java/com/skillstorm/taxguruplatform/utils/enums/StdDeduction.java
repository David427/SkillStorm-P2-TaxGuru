package com.skillstorm.taxguruplatform.utils.enums;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public enum StdDeduction {

    SINGLE_MFS_2023(new BigDecimal("13850.00")),
    MARRIED_QSS_2023(new BigDecimal("27700")),
    HOH_2023(new BigDecimal("20800.00"));

    private final BigDecimal deduction;

    StdDeduction(BigDecimal deduction) {
        this.deduction = deduction;
    }

}
