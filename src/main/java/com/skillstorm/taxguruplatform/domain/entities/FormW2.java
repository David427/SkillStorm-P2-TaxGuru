package com.skillstorm.taxguruplatform.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "form_w2_data")
public class FormW2 {

    @Id
    @SequenceGenerator(
            name = "form_w2_data_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "form_w2_data_id_seq")
    private long id;

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
