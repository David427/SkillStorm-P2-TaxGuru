package com.skillstorm.taxguruplatform.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class AppUser implements UserDetails {

    @Id
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
    private String userRole;

    @Column(name = "user_password")
    private String password;

    @OneToOne
    @JoinColumn(name = "tax_return_id", referencedColumnName = "id")
    private TaxReturn taxReturn;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(userRole));
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

}
