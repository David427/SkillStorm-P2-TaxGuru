package com.skillstorm.taxguruplatform.domain.entities;

import com.skillstorm.taxguruplatform.utils.enums.State;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class AppUser {

    @Id
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String suffix;
    private Date dateOfBirth;
    private String ssn;
    private String streetAddress;
    private String city;
    private State state;
    private String zipCode;
    private String phoneNumber;
    private String filingStatus;
    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "financial_data_id", referencedColumnName = "id")
    private FinancialData financialData;

}
