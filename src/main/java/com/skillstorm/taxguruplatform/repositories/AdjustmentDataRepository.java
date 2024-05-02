package com.skillstorm.taxguruplatform.repositories;

import com.skillstorm.taxguruplatform.domain.entities.AdjustmentData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdjustmentDataRepository extends JpaRepository<AdjustmentData, Integer> {

}
