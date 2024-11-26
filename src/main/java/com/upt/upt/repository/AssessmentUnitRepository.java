package com.upt.upt.repository;

import com.upt.upt.entity.AssessmentUnit;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for CurricularUnit entity.
 * Provides CRUD operations and query capabilities.
 */

@Repository
public interface AssessmentUnitRepository extends JpaRepository<AssessmentUnit, Long> {
        // Adiciona o método para buscar avaliações pela unidade curricular
        List<AssessmentUnit> findByCurricularUnitId(Long curricularUnitId);
        Optional<AssessmentUnit> findByCurricularUnitIdAndId(Long curricularUnitId, Long id);
}
