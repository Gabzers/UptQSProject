package com.upt.upt.repository;

import com.upt.upt.entity.DirectorUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for DirectorUnit entity.
 * Provides CRUD operations and query capabilities.
 */
@Repository
public interface DirectorUnitRepository extends JpaRepository<DirectorUnit, Long> {
    DirectorUnit findByUsernameAndPassword(String username, String password);
    Optional<DirectorUnit> findByUsername(String username); // Update to return Optional
}
