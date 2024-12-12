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
    // Método para buscar avaliações pela unidade curricular
    List<AssessmentUnit> findByCurricularUnitId(Long curricularUnitId);

    // Método para buscar avaliação por unidade curricular e id da avaliação
    Optional<AssessmentUnit> findByCurricularUnitIdAndId(Long curricularUnitId, Long id);

    // Add a repository method to fetch all assessments
    List<AssessmentUnit> findAll();

    // Método para buscar avaliações por coordenador
    List<AssessmentUnit> findByCurricularUnitCoordinatorId(Long coordinatorId);

    // Método para buscar avaliações por semestre
    List<AssessmentUnit> findByCurricularUnit_SemesterUnit_Id(Long semesterId);

    // Método para buscar avaliações por sala
    List<AssessmentUnit> findByRoomId(Long roomId);
}
