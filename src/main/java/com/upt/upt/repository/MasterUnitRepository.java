package com.upt.upt.repository;

import com.upt.upt.entity.MasterUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MasterUnitRepository extends JpaRepository<MasterUnit, Long> {
}
