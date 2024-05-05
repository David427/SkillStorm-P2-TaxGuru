package com.skillstorm.taxguruplatform.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAuthDto {

    private String username;
    private String password;
    private String jwt;

}
