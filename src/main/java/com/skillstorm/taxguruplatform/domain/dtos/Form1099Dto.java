package com.skillstorm.taxguruplatform.domain.dtos;

import com.skillstorm.taxguruplatform.domain.entities.TaxReturn;
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
public class Form1099Dto {

    private long id;
    private String accountNum;
    private BigDecimal income;
    private BigDecimal fedTaxWithheld;
    private String payerName;
    private String payerState;
    private String payerZipCode;

}
