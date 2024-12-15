package com.upt.upt.service;

import com.upt.upt.entity.RoomUnit;
import com.upt.upt.entity.AssessmentUnit;
import com.upt.upt.repository.RoomUnitRepository;
import com.upt.upt.repository.AssessmentUnitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


/**
 * Test class for RoomUnitServiceTest.
 * 
 * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
 */
@ExtendWith(MockitoExtension.class)
public class RoomUnitServiceTest {

    @Mock
    private RoomUnitRepository roomUnitRepository;

    @Mock
    private AssessmentUnitRepository assessmentUnitRepository;

    @InjectMocks
    private RoomUnitService roomUnitService;

    @Captor
    private ArgumentCaptor<RoomUnit> roomUnitCaptor;

    private RoomUnit roomUnit;
    private AssessmentUnit assessmentUnit;

    /**
     * Sets up the test environment before each test.
     * Initializes mocks and creates test instances of RoomUnit and AssessmentUnit.
     * 
     * 
     */
    @BeforeEach
    void setUp() {
        roomUnit = new RoomUnit();
        roomUnit.setId(1L);
        roomUnit.setRoomNumber("101");
        roomUnit.setDesignation("Room 101");
        roomUnit.setMaterialType("Chairs");
        roomUnit.setSeatsCount(20);
        roomUnit.setBuilding("Main Building");

        assessmentUnit = new AssessmentUnit();
        assessmentUnit.setId(1L);
        assessmentUnit.setStartTime(LocalDateTime.of(2024, 12, 15, 10, 0));
        assessmentUnit.setEndTime(LocalDateTime.of(2024, 12, 15, 12, 0));
    }

    /**
     * Tests if a room is available for a given time period when it is available.
     * 
     * 
     */
    @Test
    void testIsRoomAvailable_whenRoomIsAvailable() {
        when(assessmentUnitRepository.findByRooms_Id(1L)).thenReturn(Collections.singletonList(assessmentUnit));

        boolean isAvailable = roomUnitService.isRoomAvailable(1L, LocalDateTime.of(2024, 12, 15, 13, 0), LocalDateTime.of(2024, 12, 15, 14, 0));

        assertTrue(isAvailable);
    }

    /**
     * Tests if a room is available for a given time period when it is not available.
     * 
     * 
     */
    @Test
    void testIsRoomAvailable_whenRoomIsNotAvailable() {
        when(assessmentUnitRepository.findByRooms_Id(1L)).thenReturn(Collections.singletonList(assessmentUnit));

        boolean isAvailable = roomUnitService.isRoomAvailable(1L, LocalDateTime.of(2024, 12, 15, 11, 0), LocalDateTime.of(2024, 12, 15, 13, 0));

        assertFalse(isAvailable);
    }

    /**
     * Tests if all rooms are available for a given time period when they are available.
     * 
     * 
     */
    @Test
    void testAreRoomsAvailable_whenAllRoomsAreAvailable() {
        List<Long> roomIds = Arrays.asList(1L, 2L);
        when(roomUnitRepository.findById(2L)).thenReturn(Optional.of(roomUnit));
        when(assessmentUnitRepository.findByRooms_Id(1L)).thenReturn(Collections.singletonList(assessmentUnit));
        when(assessmentUnitRepository.findByRooms_Id(2L)).thenReturn(Collections.emptyList());

        boolean areAvailable = roomUnitService.areRoomsAvailable(roomIds, LocalDateTime.of(2024, 12, 15, 13, 0), LocalDateTime.of(2024, 12, 15, 14, 0));

        assertTrue(areAvailable);
    }

    /**
     * Tests if all rooms are available for a given time period when one room is not available.
     * 
     * 
     */
    @Test
    void testAreRoomsAvailable_whenOneRoomIsNotAvailable() {
        List<Long> roomIds = Arrays.asList(1L, 2L);
        when(roomUnitRepository.findById(2L)).thenReturn(Optional.of(roomUnit));
        when(assessmentUnitRepository.findByRooms_Id(1L)).thenReturn(Collections.singletonList(assessmentUnit));
        when(assessmentUnitRepository.findByRooms_Id(2L)).thenReturn(Collections.singletonList(assessmentUnit));

        boolean areAvailable = roomUnitService.areRoomsAvailable(roomIds, LocalDateTime.of(2024, 12, 15, 11, 0), LocalDateTime.of(2024, 12, 15, 13, 0));

        assertFalse(areAvailable);
    }

    /**
     * Tests the saving of a room unit.
     * 
     * 
     */
    @Test
    void testSaveRoom() {
        when(roomUnitRepository.save(any(RoomUnit.class))).thenReturn(roomUnit);

        RoomUnit savedRoom = roomUnitService.saveRoom(roomUnit);

        verify(roomUnitRepository, times(1)).save(roomUnitCaptor.capture());
        assertEquals(roomUnit.getRoomNumber(), savedRoom.getRoomNumber());
    }

    /**
     * Tests the creation of a room unit.
     * 
     * 
     */
    @Test
    void testCreateRoom() {
        Map<String, String> params = new HashMap<>();
        params.put("room-number", "102");
        params.put("room-designation", "Room 102");
        params.put("room-material-type", "Chairs");
        params.put("room-seats-count", "30");
        params.put("room-building", "Main Building");

        RoomUnit newRoom = roomUnitService.createRoom(params);

        assertEquals("102", newRoom.getRoomNumber());
        assertEquals("Room 102", newRoom.getDesignation());
        assertEquals("Chairs", newRoom.getMaterialType());
        assertEquals(30, newRoom.getSeatsCount());
        assertEquals("Main Building", newRoom.getBuilding());
    }

    /**
     * Tests the update of a room unit.
     * 
     * 
     */
    @Test
    void testUpdateRoom() {
        when(roomUnitRepository.findById(1L)).thenReturn(Optional.of(roomUnit));

        Map<String, String> params = new HashMap<>();
        params.put("room-id", "1");
        params.put("room-number", "103");
        params.put("room-designation", "Room 103");
        params.put("room-material-type", "Tables");
        params.put("room-seats-count", "25");
        params.put("room-building", "New Building");

        RoomUnit updatedRoom = roomUnitService.updateRoom(params);

        assertEquals("103", updatedRoom.getRoomNumber());
        assertEquals("Room 103", updatedRoom.getDesignation());
        assertEquals("Tables", updatedRoom.getMaterialType());
        assertEquals(25, updatedRoom.getSeatsCount());
        assertEquals("New Building", updatedRoom.getBuilding());
    }

    /**
     * Tests the deletion of a room unit.
     * 
     * 
     */
    @Test
    void testDeleteRoom() {
        roomUnitService.deleteRoom(1L);

        verify(roomUnitRepository, times(1)).deleteById(1L);
    }

    /**
     * Tests the retrieval of all room units.
     * 
     * 
     */
    @Test
    void testGetAllRooms() {
        when(roomUnitRepository.findAll()).thenReturn(Collections.singletonList(roomUnit));

        List<RoomUnit> rooms = roomUnitService.getAllRooms();

        assertEquals(1, rooms.size());
        assertEquals(roomUnit.getRoomNumber(), rooms.get(0).getRoomNumber());
    }

    /**
     * Tests the retrieval of a room unit by its ID.
     * 
     * 
     */
    @Test
    void testGetRoomById() {
        when(roomUnitRepository.findById(1L)).thenReturn(Optional.of(roomUnit));

        RoomUnit foundRoom = roomUnitService.getRoomById(1L);

        assertNotNull(foundRoom);
        assertEquals(roomUnit.getRoomNumber(), foundRoom.getRoomNumber());
    }

    /**
     * Tests the retrieval of room units by their designation.
     * 
     * 
     */
    @Test
    void testGetRoomsByDesignation() {
        when(roomUnitRepository.findByDesignation("Room 101")).thenReturn(Collections.singletonList(roomUnit));

        List<RoomUnit> rooms = roomUnitService.getRoomsByDesignation("Room 101");

        assertEquals(1, rooms.size());
        assertEquals(roomUnit.getRoomNumber(), rooms.get(0).getRoomNumber());
    }

    /**
     * Tests the retrieval of room units by their building.
     * 
     * 
     */
    @Test
    void testGetRoomsByBuilding() {
        when(roomUnitRepository.findByBuilding("Main Building")).thenReturn(Collections.singletonList(roomUnit));

        List<RoomUnit> rooms = roomUnitService.getRoomsByBuilding("Main Building");

        assertEquals(1, rooms.size());
        assertEquals(roomUnit.getRoomNumber(), rooms.get(0).getRoomNumber());
    }

    /**
     * Tests the retrieval of room units by their material type.
     * 
     * 
     */
    @Test
    void testGetRoomsByMaterialType() {
        when(roomUnitRepository.findByMaterialType("Chairs")).thenReturn(Collections.singletonList(roomUnit));

        List<RoomUnit> rooms = roomUnitService.getRoomsByMaterialType("Chairs");

        assertEquals(1, rooms.size());
        assertEquals(roomUnit.getRoomNumber(), rooms.get(0).getRoomNumber());
    }
}
