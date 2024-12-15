package com.upt.upt.service;

import com.upt.upt.entity.RoomUnit;
import com.upt.upt.entity.AssessmentUnit;
import com.upt.upt.repository.RoomUnitRepository;
import com.upt.upt.repository.AssessmentUnitRepository;
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
    private AssessmentUnitRepository assessmentUnitRepository;

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
        List<AssessmentUnit> assessments = assessmentUnitRepository.findByRooms_Id(roomId);
        for (AssessmentUnit assessment : assessments) {
            if (startTime.isBefore(assessment.getEndTime()) && endTime.isAfter(assessment.getStartTime())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if a list of rooms is available during the specified time.
     *
     * @param roomIds the IDs of the rooms
     * @param startTime the start time of the availability
     * @param endTime the end time of the availability
     * @return true if all rooms are available, false otherwise
     */
    public boolean areRoomsAvailable(List<Long> roomIds, LocalDateTime startTime, LocalDateTime endTime) {
        for (Long roomId : roomIds) {
            if (!isRoomAvailable(roomId, startTime, endTime)) {
                return false;
            }
        }
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
                assessment.setRooms(null);
            }
            roomUnitRepository.delete(room);
        }
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
        // Fetch and filter rooms based on criteria
        List<RoomUnit> availableRooms = roomUnitRepository.findAll().stream()
                .filter(room -> room.getSeatsCount() > 0) // Ignorar salas com capacidade zero
                .filter(room -> computerRequired
                        ? room.getMaterialType().equals("Computers") // Só salas com computadores se necessário
                        : !room.getMaterialType().equals("Computers")) // Excluir salas com computadores caso não seja necessário
                .filter(room -> isRoomAvailable(room.getId(), startTime, endTime)) // Verificar disponibilidade da sala
                .collect(Collectors.toList());

        List<RoomUnit> selectedRooms = new ArrayList<>();
        int remainingStudents = numStudents;

        // Continuar alocando salas até que todos os alunos sejam alocados
        while (remainingStudents > 0 && !availableRooms.isEmpty()) {
            final int students = remainingStudents; // Use a final variable within the lambda expression
            // Ordenar as salas de acordo com a proximidade da capacidade ao número de alunos restantes
            availableRooms.sort((r1, r2) -> {
                int diff1 = Math.abs(r1.getSeatsCount() - students);
                int diff2 = Math.abs(r2.getSeatsCount() - students);
                return Integer.compare(diff1, diff2);
            });

            RoomUnit bestRoom = availableRooms.get(0);
            if (isRoomAvailable(bestRoom.getId(), startTime, endTime)) {
                selectedRooms.add(bestRoom);
                remainingStudents -= bestRoom.getSeatsCount();
            }

            // Remover a sala alocada da lista de disponíveis
            availableRooms.remove(bestRoom);
        }

        // Retornar as salas alocadas, mesmo que não sejam suficientes
        return selectedRooms;
    }

    public boolean roomNumberExists(String roomNumber) {
        return roomUnitRepository.findByRoomNumber(roomNumber).isPresent();
    }

    public boolean roomNumberExists(String roomNumber, Long roomId) {
        Optional<RoomUnit> existingRoom = roomUnitRepository.findByRoomNumber(roomNumber);
        return existingRoom.isPresent() && !existingRoom.get().getId().equals(roomId);
    }
    
    /**
     * Get or create the "Via Online" room.
     *
     * @return the "Via Online" RoomUnit
     */
    public RoomUnit getOrCreateOnlineRoom() {
        Optional<RoomUnit> onlineRoom = roomUnitRepository.findByRoomNumber("99999");
        if (onlineRoom.isPresent()) {
            return onlineRoom.get();
        } else {
            RoomUnit newOnlineRoom = new RoomUnit();
            newOnlineRoom.setRoomNumber("99999");
            newOnlineRoom.setDesignation("Online Submission");
            newOnlineRoom.setMaterialType("Moodle");
            newOnlineRoom.setSeatsCount(0);
            newOnlineRoom.setBuilding("Online Submission");
            return roomUnitRepository.save(newOnlineRoom);
        }
    }

    /**
     * Get or create the "Usual Class Time Room".
     *
     * @return the "Usual Class Time Room" RoomUnit
     */
    public RoomUnit getOrCreateClassTimeRoom() {
        Optional<RoomUnit> classTimeRoom = roomUnitRepository.findByRoomNumber("99998");
        if (classTimeRoom.isPresent()) {
            return classTimeRoom.get();
        } else {
            RoomUnit newClassTimeRoom = new RoomUnit();
            newClassTimeRoom.setRoomNumber("99998");
            newClassTimeRoom.setDesignation("Usual Class Time Room");
            newClassTimeRoom.setMaterialType("Class Time");
            newClassTimeRoom.setSeatsCount(0);
            newClassTimeRoom.setBuilding("Class Time");
            return roomUnitRepository.save(newClassTimeRoom);
        }
    }

    /**
     * Find available rooms within the same day if the initially requested time slot is not available.
     *
     * @param numStudents the number of students
     * @param computerRequired whether a computer is required
     * @param startTime the start time of the availability
     * @param endTime the end time of the availability
     * @return a list of available rooms within the same day and the new time slot if found
     */
    public Optional<Map.Entry<LocalDateTime, LocalDateTime>> findAvailableRoomsWithinSameDay(int numStudents, boolean computerRequired, LocalDateTime startTime, LocalDateTime endTime) {
        LocalDateTime currentStartTime = startTime.withHour(8).withMinute(0);
        LocalDateTime currentEndTime = currentStartTime.plusMinutes(java.time.Duration.between(startTime, endTime).toMinutes());

        while (currentEndTime.isBefore(startTime.withHour(23).withMinute(59))) {
            List<RoomUnit> availableRooms = getAvailableRooms(numStudents, computerRequired, currentStartTime, currentEndTime);
            if (!availableRooms.isEmpty() && availableRooms.stream().mapToInt(RoomUnit::getSeatsCount).sum() >= numStudents) {
                return Optional.of(Map.entry(currentStartTime, currentEndTime));
            }
            currentStartTime = currentStartTime.plusMinutes(30);
            currentEndTime = currentStartTime.plusMinutes(java.time.Duration.between(startTime, endTime).toMinutes());
        }

        return Optional.empty();
    }
}
