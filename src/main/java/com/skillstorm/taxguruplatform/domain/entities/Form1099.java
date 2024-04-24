package com.skillstorm.taxguruplatform.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private int id;

    private String accountNum;
    private double income;
    private double taxWithheld;
    private String payerName;
    private String payerState;
    private String payerZipCode;

    @OneToOne(mappedBy = "form1099")
    private FinancialData financialData;

}
