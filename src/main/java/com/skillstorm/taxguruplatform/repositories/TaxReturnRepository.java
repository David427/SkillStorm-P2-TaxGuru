package com.skillstorm.taxguruplatform.repositories;

import com.skillstorm.taxguruplatform.domain.entities.TaxReturn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaxReturnRepository extends JpaRepository<TaxReturn, Long> {

}
