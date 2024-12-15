package com.upt.upt.repository;

import com.upt.upt.entity.RoomUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for RoomUnit entity.
 * Provides CRUD operations and query capabilities.
 * 
 * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
 */
@Repository
public interface RoomUnitRepository extends JpaRepository<RoomUnit, Long> {

    List<RoomUnit> findByDesignation(String designation);
    List<RoomUnit> findByBuilding(String building);
    List<RoomUnit> findByMaterialType(String materialType);
    List<RoomUnit> findAllById(Iterable<Long> ids);
    Optional<RoomUnit> findByRoomNumber(String roomNumber);
}
