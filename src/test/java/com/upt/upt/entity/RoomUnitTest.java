package com.upt.upt.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RoomUnitTest {

    private RoomUnit room;

    @BeforeEach
    void setUp() {
        room = new RoomUnit(1L, "101", "Classroom", "Desks", 30, "Building A");
    }

    @Test
    void testGetters() {
        assertEquals(1L, room.getId());
        assertEquals("101", room.getRoomNumber());
        assertEquals("Classroom", room.getDesignation());
        assertEquals("Desks", room.getMaterialType());
        assertEquals(30, room.getSeatsCount());
        assertEquals("Building A", room.getBuilding());
    }

    @Test
    void testSetters() {
        room.setId(2L);
        room.setRoomNumber("202");
        room.setDesignation("Laboratory");
        room.setMaterialType("Computers");
        room.setSeatsCount(40);
        room.setBuilding("Building B");

        assertEquals(2L, room.getId());
        assertEquals("202", room.getRoomNumber());
        assertEquals("Laboratory", room.getDesignation());
        assertEquals("Computers", room.getMaterialType());
        assertEquals(40, room.getSeatsCount());
        assertEquals("Building B", room.getBuilding());
    }

    @Test
    void testAddAssessment() {
        AssessmentUnit assessment = new AssessmentUnit();
        List<AssessmentUnit> assessments = new ArrayList<>();
        assessments.add(assessment);

        room.setAssessments(assessments);

        assertEquals(1, room.getAssessments().size());
        assertEquals(assessment, room.getAssessments().get(0));
    }

    @Test
    void testEqualsAndHashCode() {
        RoomUnit anotherRoom = new RoomUnit(1L, "102", "Laboratory", "Desks", 20, "Building B");
        assertEquals(room, anotherRoom);
        assertEquals(room.hashCode(), anotherRoom.hashCode());

        anotherRoom.setId(2L);
        assertNotEquals(room, anotherRoom);
    }

    @Test
    void testToString() {
        String expected = "RoomUnit{id=1, roomNumber='101', designation='Classroom', materialType='Desks', seatsCount=30, building='Building A'}";
        assertEquals(expected, room.toString());
    }
}
