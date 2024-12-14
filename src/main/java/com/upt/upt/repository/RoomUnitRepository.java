package com.upt.upt.repository;

import com.upt.upt.entity.RoomUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for accessing RoomUnit data.
 */
@Repository
public interface RoomUnitRepository extends JpaRepository<RoomUnit, Long> {

    // Find rooms by designation
    List<RoomUnit> findByDesignation(String designation);

    // Find rooms by building
    List<RoomUnit> findByBuilding(String building);

    // Find rooms by material type
    List<RoomUnit> findByMaterialType(String materialType);

    // Find rooms by a list of IDs
    List<RoomUnit> findAllById(Iterable<Long> ids);
}
