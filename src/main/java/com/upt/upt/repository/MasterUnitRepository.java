package com.upt.upt.repository;

import com.upt.upt.entity.MasterUnit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for MasterUnit entity.
 * Provides CRUD operations and query capabilities.
 * 
 * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
 */
public interface MasterUnitRepository extends JpaRepository<MasterUnit, Long> {
    MasterUnit findByUsernameAndPassword(String username, String password);
    Optional<MasterUnit> findByUsername(String username);
}
