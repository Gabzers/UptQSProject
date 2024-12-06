package com.upt.upt.repository;

import com.upt.upt.entity.CoordinatorUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for CoordinatorUnit entity.
 * Provides CRUD operations and query capabilities.
 */
@Repository
public interface CoordinatorUnitRepository extends JpaRepository<CoordinatorUnit, Long> {
    CoordinatorUnit findByUsernameAndPassword(String username, String password);
    CoordinatorUnit findByUsername(String username); // Adicione este método
}