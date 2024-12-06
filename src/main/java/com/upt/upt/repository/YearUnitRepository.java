
package com.upt.upt.repository;

import com.upt.upt.entity.YearUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface YearUnitRepository extends JpaRepository<YearUnit, Long> {
}