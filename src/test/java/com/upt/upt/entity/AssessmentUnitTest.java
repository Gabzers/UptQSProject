package com.upt.upt.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for the AssessmentUnit class.
 */
public class AssessmentUnitTest {

    private AssessmentUnit assessmentUnit;
    private CurricularUnit curricularUnit;
    private MapUnit mapUnit;
    private List<RoomUnit> rooms;

    @BeforeEach
    void setUp() {
        curricularUnit = new CurricularUnit(); // Mock object or simple instantiation if the class is straightforward
        mapUnit = new MapUnit();              // Mock object or simple instantiation if the class is straightforward

        rooms = new ArrayList<>();
        rooms.add(new RoomUnit());           // Add dummy RoomUnit objects if necessary

        assessmentUnit = new AssessmentUnit(
                1L,
                "Test",
                30,
                "Teaching Period",
                true,
                false,
                LocalDateTime.of(2024, 12, 15, 9, 0),
                LocalDateTime.of(2024, 12, 15, 12, 0),
                rooms,
                10.0,
                curricularUnit,
                mapUnit
        );
    }

    @Test
    void testAssessmentUnitConstructorAndGetters() {
        assertEquals(1L, assessmentUnit.getId());
        assertEquals("Test", assessmentUnit.getType());
        assertEquals(30, assessmentUnit.getWeight());
        assertEquals("Teaching Period", assessmentUnit.getExamPeriod());
        assertTrue(assessmentUnit.getComputerRequired());
        assertFalse(assessmentUnit.getClassTime());
        assertEquals(LocalDateTime.of(2024, 12, 15, 9, 0), assessmentUnit.getStartTime());
        assertEquals(LocalDateTime.of(2024, 12, 15, 12, 0), assessmentUnit.getEndTime());
        assertEquals(rooms, assessmentUnit.getRooms());
        assertEquals(10.0, assessmentUnit.getMinimumGrade());
        assertEquals(curricularUnit, assessmentUnit.getCurricularUnit());
        assertEquals(mapUnit, assessmentUnit.getMap());
    }

    @Test
    void testSetters() {
        assessmentUnit.setType("Presentation");
        assessmentUnit.setWeight(40);
        assessmentUnit.setExamPeriod("Exam Period");
        assessmentUnit.setComputerRequired(false);
        assessmentUnit.setClassTime(true);
        assessmentUnit.setStartTime(LocalDateTime.of(2024, 12, 20, 10, 0));
        assessmentUnit.setEndTime(LocalDateTime.of(2024, 12, 20, 13, 0));
        assessmentUnit.setMinimumGrade(12.0);

        assertEquals("Presentation", assessmentUnit.getType());
        assertEquals(40, assessmentUnit.getWeight());
        assertEquals("Exam Period", assessmentUnit.getExamPeriod());
        assertFalse(assessmentUnit.getComputerRequired());
        assertTrue(assessmentUnit.getClassTime());
        assertEquals(LocalDateTime.of(2024, 12, 20, 10, 0), assessmentUnit.getStartTime());
        assertEquals(LocalDateTime.of(2024, 12, 20, 13, 0), assessmentUnit.getEndTime());
        assertEquals(12.0, assessmentUnit.getMinimumGrade());
    }

    @Test
    void testEqualsAndHashCode() {
        AssessmentUnit anotherAssessmentUnit = new AssessmentUnit(1L, "Test", 30, "Teaching Period", true, false,
                LocalDateTime.of(2024, 12, 15, 9, 0), LocalDateTime.of(2024, 12, 15, 12, 0), rooms, 10.0, curricularUnit, mapUnit);

        assertEquals(assessmentUnit, anotherAssessmentUnit);
        assertEquals(assessmentUnit.hashCode(), anotherAssessmentUnit.hashCode());

        anotherAssessmentUnit.setId(2L);
        assertNotEquals(assessmentUnit, anotherAssessmentUnit);
    }

    @Test
    void testToString() {
        String expected = "AssessmentUnit{" +
                "id=1, type='Test', weight=30, examPeriod='Teaching Period', computerRequired=true, classTime=false, " +
                "startTime=2024-12-15T09:00, endTime=2024-12-15T12:00, rooms=[RoomUnit{}], minimumGrade=10.0}";

        assertTrue(assessmentUnit.toString().contains("AssessmentUnit{"));
        assertTrue(assessmentUnit.toString().contains("id=1"));
        assertTrue(assessmentUnit.toString().contains("type='Test'"));
    }
}
