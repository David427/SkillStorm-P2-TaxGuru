package com.skillstorm.taxguruplatform.utils.enums;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public enum Credits {

    EITC_0DEP(new BigDecimal("17640.00"), new BigDecimal("600.00"), new BigDecimal("0.00")),
    EITC_1DEP(new BigDecimal("46560.00"), new BigDecimal("3995.00"), new BigDecimal("0.00")),
    EITC_2DEP(new BigDecimal("52918.00"), new BigDecimal("6604.00"), new BigDecimal("0.00")),
    EITC_3DEP(new BigDecimal("56838.00"), new BigDecimal("7430.00"), new BigDecimal("0.00")),
    EITC_MFJ_0DEP(new BigDecimal("24210.00"), new BigDecimal("600.00"), new BigDecimal("0.00")),
    EITC_MFJ_1DEP(new BigDecimal("53120.00"), new BigDecimal("3995.00"), new BigDecimal("0.00")),
    EITC_MFJ_2DEP(new BigDecimal("59478.00"), new BigDecimal("6604.00"), new BigDecimal("0.00")),
    EITC_MFJ_3DEP(new BigDecimal("63398.00"), new BigDecimal("7430.00"), new BigDecimal("0.00")),
    CHILD_CREDIT(new BigDecimal("200000.00"), new BigDecimal("2000.00"), new BigDecimal("0.00")),
    CHILD_CREDIT_MFJ(new BigDecimal("400000.00"), new BigDecimal("2000.00"), new BigDecimal("0.00")),
    IRA_WORK_PLAN(new BigDecimal("73000.00"), new BigDecimal("0.00"), new BigDecimal("6500.00")),
    IRA_WORK_PLAN_MFJ(new BigDecimal("116000.00"), new BigDecimal("0.00"), new BigDecimal("6500.00")),
    IRA_NO_PLAN(new BigDecimal("0.00"), new BigDecimal("0.00"), new BigDecimal("6500.00"));

    private final BigDecimal agiLimit;
    private final BigDecimal creditAmount;
    private final BigDecimal maxCredit;

    Credits(BigDecimal agiLimit, BigDecimal creditAmount, BigDecimal maxCredit) {
        this.agiLimit = agiLimit;
        this.creditAmount = creditAmount;
        this.maxCredit = maxCredit;
    }

}
