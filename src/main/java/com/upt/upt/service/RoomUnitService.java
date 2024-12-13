package com.upt.upt.service;

import com.upt.upt.entity.RoomUnit;
import com.upt.upt.entity.AssessmentUnit;
import com.upt.upt.repository.RoomUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

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
     * Check if a room is available during the specified time.
     *
     * @param roomId the ID of the room
     * @param startTime the start time of the availability
     * @param endTime the end time of the availability
     * @return true if the room is available, false otherwise
     */
    private boolean isRoomAvailable(Long roomId, LocalDateTime startTime, LocalDateTime endTime) {
        // Implement the logic to check room availability
        // This is a placeholder implementation
        return true;
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

    /**
     * Get available rooms based on the number of students and computer requirement.
     *
     * @param numStudents the number of students
     * @param computerRequired whether a computer is required
     * @param startTime the start time of the availability
     * @param endTime the end time of the availability
     * @return a list of available rooms
     */
    public List<RoomUnit> getAvailableRooms(int numStudents, boolean computerRequired, LocalDateTime startTime, LocalDateTime endTime) {
        String requiredMaterialType = computerRequired ? "Computers" : "Desks";
        
        // Fetch all rooms that meet the criteria
        List<RoomUnit> rooms = roomUnitRepository.findAll().stream()
                .filter(room -> room.getSeatsCount() >= numStudents && (room.getMaterialType().equals(requiredMaterialType) || room.getMaterialType().equals("Amphitheater")))
                .collect(Collectors.toList());

        // Filter out rooms that are not available during the specified time
        List<RoomUnit> availableRooms = rooms.stream()
                .filter(room -> isRoomAvailable(room.getId(), startTime, endTime))
                .collect(Collectors.toList());

        // Find the room with the closest capacity to the number of students
        RoomUnit bestFitRoom = availableRooms.stream()
                .min((room1, room2) -> Integer.compare(Math.abs(room1.getSeatsCount() - numStudents), Math.abs(room2.getSeatsCount() - numStudents)))
                .orElse(null);

        return bestFitRoom != null ? List.of(bestFitRoom) : List.of();
    }
}
