package com.upt.upt.repository;

import com.upt.upt.entity.YearUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for YearUnit entity.
 * Provides CRUD operations and query capabilities.
 * 
 * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
 */
@Repository
public interface YearUnitRepository extends JpaRepository<YearUnit, Long> {
    Optional<YearUnit> findTopByOrderByFirstSemesterStartDateDesc();
    Optional<YearUnit> findTopByOrderByIdDesc();
    Optional<YearUnit> findTopByDirectorUnitIdOrderByIdDesc(Long directorId);
    List<YearUnit> findByDirectorUnitId(Long directorId);
}