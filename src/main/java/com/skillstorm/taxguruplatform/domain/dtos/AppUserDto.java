package com.skillstorm.taxguruplatform.domain.dtos;

import com.skillstorm.taxguruplatform.domain.entities.TaxReturn;
import com.skillstorm.taxguruplatform.utils.enums.State;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUserDto {

    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String suffix;
    private LocalDate dateOfBirth;
    private String ssn;
    private String streetAddress;
    private String city;
    private String userState;
    private String zipCode;
    private String phoneNumber;
    private TaxReturn taxReturn;

}
