package com.skillstorm.taxguruplatform.domain.dtos;

import com.skillstorm.taxguruplatform.domain.entities.FinancialData;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Form1099Dto {

    private int id;
    private String accountNum;
    private float income;
    private float taxWithheld;
    private String payerName;
    private String payerState;
    private String payerZipCode;
    private FinancialData financialData;

}
