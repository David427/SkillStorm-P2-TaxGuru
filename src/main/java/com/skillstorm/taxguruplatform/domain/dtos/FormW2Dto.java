package com.skillstorm.taxguruplatform.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FormW2Dto {

    private Long id;
    private String eid;
    private String empName;
    private String empStreetAddress;
    private String empCity;
    private String empState;
    private String empZipCode;
    private BigDecimal income;
    private BigDecimal fedTaxWithheld;
    private BigDecimal ssTaxWithheld;
    private BigDecimal mediTaxWithheld;

}
