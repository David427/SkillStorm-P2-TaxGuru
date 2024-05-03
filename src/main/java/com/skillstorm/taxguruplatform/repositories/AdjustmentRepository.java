package com.skillstorm.taxguruplatform.repositories;

import com.skillstorm.taxguruplatform.domain.entities.Adjustment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdjustmentRepository extends JpaRepository<Adjustment, Long> {

}
