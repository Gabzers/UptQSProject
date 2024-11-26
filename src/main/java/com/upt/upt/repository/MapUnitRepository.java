package com.upt.upt.repository;

import com.upt.upt.entity.MapUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for MapUnit entity.
 */
@Repository
public interface MapUnitRepository extends JpaRepository<MapUnit, Long> {
    // Aqui você pode adicionar consultas personalizadas se necessário
}
