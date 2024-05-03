package com.skillstorm.taxguruplatform.repositories;

import com.skillstorm.taxguruplatform.domain.entities.FormW2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FormW2Repository extends JpaRepository<FormW2, Long> {

}
