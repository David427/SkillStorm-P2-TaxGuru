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
@Table(name = "form_1099_data")
public class Form1099 {

    @Id
    @SequenceGenerator(
            name = "form_1099_data_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "form_1099_data_id_seq")
    private Long id;

    private String accountNum;
    private BigDecimal income;
    private BigDecimal fedTaxWithheld;
    private String payerName;
    private String payerState;
    private String payerZipCode;

}
