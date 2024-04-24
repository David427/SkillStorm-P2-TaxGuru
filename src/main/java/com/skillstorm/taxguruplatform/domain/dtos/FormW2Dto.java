package com.skillstorm.taxguruplatform.domain.dtos;

import com.skillstorm.taxguruplatform.domain.entities.FinancialData;
import com.skillstorm.taxguruplatform.utils.enums.State;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FormW2Dto {

    private int id;
    private String eid;
    private String companyName;
    private String streetAddress;
    private String city;
    private State state;
    private String zipCode;
    private float wages;
    private float taxWithheld;
    private FinancialData financialData;

}
