package com.upt.upt.service;

import com.upt.upt.entity.RoomUnit;
import com.upt.upt.repository.RoomUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing RoomUnit operations.
 */
@Service
public class RoomUnitService {

    private final RoomUnitRepository roomUnitRepository;

    @Autowired
    public RoomUnitService(RoomUnitRepository roomUnitRepository) {
        this.roomUnitRepository = roomUnitRepository;
    }

    /**
     * Save a new RoomUnit.
     *
     * @param roomUnit the RoomUnit entity to save
     * @return the saved RoomUnit
     */
    public RoomUnit createRoom(RoomUnit roomUnit) {
        return roomUnitRepository.save(roomUnit);
    }

    /**
     * Update an existing RoomUnit.
     *
     * @param id       the ID of the RoomUnit to update
     * @param roomUnit the updated RoomUnit entity
     * @return the updated RoomUnit if found, or null if not found
     */
    public RoomUnit updateRoom(Long id, RoomUnit roomUnit) {
        Optional<RoomUnit> existingRoom = roomUnitRepository.findById(id);
        if (existingRoom.isPresent()) {
            RoomUnit updatedRoom = existingRoom.get();
            updatedRoom.setRoomNumber(roomUnit.getRoomNumber());
            updatedRoom.setDesignation(roomUnit.getDesignation());
            updatedRoom.setMaterialType(roomUnit.getMaterialType());
            updatedRoom.setSeatsCount(roomUnit.getSeatsCount());
            updatedRoom.setBuilding(roomUnit.getBuilding());
            return roomUnitRepository.save(updatedRoom);
        }
        return null;
    }

    /**
     * Delete a RoomUnit by ID.
     *
     * @param id the ID of the RoomUnit to delete
     */
    public void deleteRoom(Long id) {
        roomUnitRepository.deleteById(id);
    }

    /**
     * Get all RoomUnits.
     *
     * @return a list of all RoomUnits
     */
    public List<RoomUnit> getAllRooms() {
        return roomUnitRepository.findAll();
    }

    /**
     * Get a RoomUnit by ID.
     *
     * @param id the ID of the RoomUnit
     * @return the RoomUnit if found, or null if not found
     */
    public RoomUnit getRoomById(Long id) {
        return roomUnitRepository.findById(id).orElse(null);
    }

    /**
     * Get a list of RoomUnits by designation.
     *
     * @param designation the designation of the RoomUnit
     * @return a list of RoomUnits that match the designation
     */
    public List<RoomUnit> getRoomsByDesignation(String designation) {
        return roomUnitRepository.findByDesignation(designation);
    }

    /**
     * Get a list of RoomUnits by building.
     *
     * @param building the building where the RoomUnit is located
     * @return a list of RoomUnits located in the specified building
     */
    public List<RoomUnit> getRoomsByBuilding(String building) {
        return roomUnitRepository.findByBuilding(building);
    }

    /**
     * Get a list of RoomUnits by material type.
     *
     * @param materialType the material type in the RoomUnit
     * @return a list of RoomUnits that match the material type
     */
    public List<RoomUnit> getRoomsByMaterialType(String materialType) {
        return roomUnitRepository.findByMaterialType(materialType);
    }
}
