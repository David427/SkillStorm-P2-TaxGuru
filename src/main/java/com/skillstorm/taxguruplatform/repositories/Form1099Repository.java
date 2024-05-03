package com.skillstorm.taxguruplatform.repositories;

import com.skillstorm.taxguruplatform.domain.entities.Form1099;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Form1099Repository extends JpaRepository<Form1099, Long> {

}
