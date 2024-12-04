package com.upt.upt.repository;

import com.upt.upt.entity.RoomUnit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for accessing RoomUnit data.
 */
public interface RoomUnitRepository extends JpaRepository<RoomUnit, Long> {

    // Find rooms by designation
    List<RoomUnit> findByDesignation(String designation);

    // Find rooms by building
    List<RoomUnit> findByBuilding(String building);

    // Find rooms by material type
    List<RoomUnit> findByMaterialType(String materialType);
}
