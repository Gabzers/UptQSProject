package com.upt.upt.repository;

import com.upt.upt.entity.CurricularUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for CurricularUnit entity.
 * Provides CRUD operations and query capabilities.
 */

@Repository
public interface CurricularUnitRepository extends JpaRepository<CurricularUnit, Long> {
}
