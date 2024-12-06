package com.upt.upt.repository;

import com.upt.upt.entity.SemesterUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SemesterUnitRepository extends JpaRepository<SemesterUnit, Long> {
}