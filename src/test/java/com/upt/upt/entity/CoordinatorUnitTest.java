package com.upt.upt.entity;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for CoordinatorUnit.
 * 
 * @autor Grupo 5 - 47719, 47713, 46697, 47752, 47004
 */
class CoordinatorUnitTest {

    /**
     * Test constructor and getters.
     */
    @Test
    void testConstructorAndGetters() {
        DirectorUnit directorUnit = new DirectorUnit();
        List<CurricularUnit> curricularUnits = new ArrayList<>();

        CoordinatorUnit coordinator = new CoordinatorUnit(
                1L, "John Doe", "jdoe", "password123", "Computer Science", 4, directorUnit, curricularUnits
        );

        assertEquals(1L, coordinator.getId());
        assertEquals("John Doe", coordinator.getName());
        assertEquals("jdoe", coordinator.getUsername());
        assertEquals("password123", coordinator.getPassword());
        assertEquals("Computer Science", coordinator.getCourse());
        assertEquals(4, coordinator.getDuration());
        assertEquals(directorUnit, coordinator.getDirectorUnit());
        assertEquals(curricularUnits, coordinator.getCurricularUnits());
    }

    /**
     * Test setters.
     */
    @Test
    void testSetters() {
        CoordinatorUnit coordinator = new CoordinatorUnit();
        DirectorUnit directorUnit = new DirectorUnit();
        List<CurricularUnit> curricularUnits = new ArrayList<>();

        coordinator.setId(1L);
        coordinator.setName("Jane Doe");
        coordinator.setUsername("jdoe");
        coordinator.setPassword("securePass");
        coordinator.setCourse("Mathematics");
        coordinator.setDuration(3);
        coordinator.setDirectorUnit(directorUnit);
        coordinator.setCurricularUnits(curricularUnits);

        assertEquals(1L, coordinator.getId());
        assertEquals("Jane Doe", coordinator.getName());
        assertEquals("jdoe", coordinator.getUsername());
        assertEquals("securePass", coordinator.getPassword());
        assertEquals("Mathematics", coordinator.getCourse());
        assertEquals(3, coordinator.getDuration());
        assertEquals(directorUnit, coordinator.getDirectorUnit());
        assertEquals(curricularUnits, coordinator.getCurricularUnits());
    }

    /**
     * Test addCurricularUnit method.
     */
    @Test
    void testAddCurricularUnit() {
        CoordinatorUnit coordinator = new CoordinatorUnit();
        CurricularUnit curricularUnit = new CurricularUnit();

        coordinator.addCurricularUnit(curricularUnit);

        assertEquals(1, coordinator.getCurricularUnits().size());
        assertTrue(coordinator.getCurricularUnits().contains(curricularUnit));
        assertEquals(coordinator, curricularUnit.getCoordinator());
    }

    /**
     * Test equals and hashCode methods.
     */
    @Test
    void testEqualsAndHashCode() {
        CoordinatorUnit coordinator1 = new CoordinatorUnit();
        CoordinatorUnit coordinator2 = new CoordinatorUnit();
        CoordinatorUnit coordinator3 = new CoordinatorUnit();

        coordinator1.setId(1L);
        coordinator2.setId(1L);
        coordinator3.setId(2L);

        assertEquals(coordinator1, coordinator2);
        assertNotEquals(coordinator1, coordinator3);

        assertEquals(coordinator1.hashCode(), coordinator2.hashCode());
        assertNotEquals(coordinator1.hashCode(), coordinator3.hashCode());
    }

    /**
     * Test toString method.
     */
    @Test
    void testToString() {
        CoordinatorUnit coordinator = new CoordinatorUnit();
        coordinator.setId(1L);
        coordinator.setName("John Doe");
        coordinator.setUsername("jdoe");
        coordinator.setPassword("password123");
        coordinator.setCourse("Computer Science");
        coordinator.setDuration(4);

        String expected = "CoordinatorUnit{" +
                "id=1, name='John Doe', username='jdoe', password='password123', course='Computer Science', duration=4}";

        assertEquals(expected, coordinator.toString());
    }
}
