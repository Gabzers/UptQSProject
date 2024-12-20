package com.upt.upt.repository;

import com.upt.upt.entity.SemesterUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for SemesterUnit entity.
 * Provides CRUD operations and query capabilities.
 * 
 * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
 */
@Repository
public interface SemesterUnitRepository extends JpaRepository<SemesterUnit, Long> {
}