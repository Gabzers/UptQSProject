package com.upt.upt.service;

import com.upt.upt.entity.RoomUnit;
import com.upt.upt.entity.AssessmentUnit;
import com.upt.upt.repository.RoomUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
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
    public RoomUnit saveRoom(RoomUnit roomUnit) {
        return roomUnitRepository.save(roomUnit);
    }

    /**
     * Create a new RoomUnit from parameters.
     *
     * @param params the parameters to create the RoomUnit
     * @return the created RoomUnit
     */
    public RoomUnit createRoom(Map<String, String> params) {
        RoomUnit newRoom = new RoomUnit();
        newRoom.setRoomNumber(params.get("room-number"));
        newRoom.setDesignation(params.get("room-designation"));
        newRoom.setMaterialType(params.get("room-material-type"));
        newRoom.setSeatsCount(Integer.parseInt(params.get("room-seats-count")));
        newRoom.setBuilding(params.get("room-building"));
        return newRoom;
    }

    /**
     * Update an existing RoomUnit from parameters.
     *
     * @param params the parameters to update the RoomUnit
     * @return the updated RoomUnit
     */
    public RoomUnit updateRoom(Map<String, String> params) {
        Long id = Long.parseLong(params.get("room-id"));
        RoomUnit roomUnit = getRoomById(id);
        if (roomUnit != null) {
            roomUnit.setRoomNumber(params.get("room-number"));
            roomUnit.setDesignation(params.get("room-designation"));
            roomUnit.setMaterialType(params.get("room-material-type"));
            roomUnit.setSeatsCount(Integer.parseInt(params.get("room-seats-count")));
            roomUnit.setBuilding(params.get("room-building"));
        }
        return roomUnit;
    }

    /**
     * Delete a RoomUnit by ID along with its associated assessments.
     *
     * @param id the ID of the RoomUnit to delete
     */
    public void deleteRoomWithAssessments(Long id) {
        RoomUnit room = getRoomById(id);
        if (room != null) {
            List<AssessmentUnit> assessments = room.getAssessments();
            for (AssessmentUnit assessment : assessments) {
                assessment.setRoom(null);
            }
            roomUnitRepository.delete(room);
        }
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
