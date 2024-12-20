package com.upt.upt.repository;

import com.upt.upt.entity.AssessmentUnit;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for AssessmentUnit entity.
 * Provides CRUD operations and query capabilities.
 * 
 * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
 */
@Repository
public interface AssessmentUnitRepository extends JpaRepository<AssessmentUnit, Long> {
    List<AssessmentUnit> findByCurricularUnitId(Long curricularUnitId);
    Optional<AssessmentUnit> findByCurricularUnitIdAndId(Long curricularUnitId, Long id);
    List<AssessmentUnit> findAll();
    List<AssessmentUnit> findByCurricularUnitCoordinatorId(Long coordinatorId);
    List<AssessmentUnit> findByCurricularUnit_SemesterUnit_Id(Long semesterId);
    List<AssessmentUnit> findByRooms_Id(Long roomId);
    List<AssessmentUnit> findByCurricularUnit_Year(Integer year);
    List<AssessmentUnit> findByCurricularUnit_Semester(Integer semester);
    List<AssessmentUnit> findByCurricularUnit_YearAndCurricularUnit_SemesterAndCurricularUnit_CoordinatorId(int year, int semester, Long coordinatorId);
    List<AssessmentUnit> findByCurricularUnit_SemesterAndCurricularUnit_CoordinatorIdAndCurricularUnit_YearNot(int semester, Long coordinatorId, int year);
}
