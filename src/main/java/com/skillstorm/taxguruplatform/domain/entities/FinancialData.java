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
@Table(name = "financial_data")
public class FinancialData {

    @Id
    @SequenceGenerator(
            name = "financial_data_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "financial_data_id_seq")
    private int id;

    private double grossIncome;
    private boolean stdDeduction;
    private double result;

    @OneToOne(mappedBy = "financialData")
    private AppUser appUser;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "form_w2_id", referencedColumnName = "id")
    private FormW2 formW2;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "form_1099_id", referencedColumnName = "id")
    private Form1099 form1099;

}
