package com.upt.upt.repository;

import com.upt.upt.entity.MasterUnit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MasterUnitRepository extends JpaRepository<MasterUnit, Long> {
    MasterUnit findByUsernameAndPassword(String username, String password);
    Optional<MasterUnit> findByUsername(String username); // Update to return Optional
}
