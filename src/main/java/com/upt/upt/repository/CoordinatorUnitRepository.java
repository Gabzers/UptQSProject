package com.upt.upt.repository;

import com.upt.upt.entity.CoordinatorUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for CoordinatorUnit entity.
 * Provides CRUD operations and query capabilities.
 * 
 * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
 */
@Repository
public interface CoordinatorUnitRepository extends JpaRepository<CoordinatorUnit, Long> {
    CoordinatorUnit findByUsernameAndPassword(String username, String password);
    Optional<CoordinatorUnit> findByUsername(String username);
}