package com.upt.upt.entity;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DirectorUnitTest {

    @Test
    void testGettersAndSetters() {
        DirectorUnit director = new DirectorUnit();

        director.setId(1L);
        director.setName("Test Director");
        director.setUsername("testuser");
        director.setPassword("password123");
        director.setDepartment("Computer Science");

        assertEquals(1L, director.getId());
        assertEquals("Test Director", director.getName());
        assertEquals("testuser", director.getUsername());
        assertEquals("password123", director.getPassword());
        assertEquals("Computer Science", director.getDepartment());
    }

    @Test
    void testAddCoordinator() {
        DirectorUnit director = new DirectorUnit();
        CoordinatorUnit coordinator = new CoordinatorUnit();

        director.addCoordinator(coordinator);

        assertTrue(director.getCoordinators().contains(coordinator));
        assertEquals(director, coordinator.getDirectorUnit());
    }

    @Test
    void testAddAcademicYear() {
        DirectorUnit director = new DirectorUnit();
        YearUnit year = new YearUnit();

        director.addAcademicYear(year);

        assertTrue(director.getAcademicYears().contains(year));
        assertEquals(director, year.getDirectorUnit());
    }

    @Test
    void testGetCurrentYear() {
        DirectorUnit director = new DirectorUnit();
        YearUnit year1 = new YearUnit();
        year1.setId(1L);
        YearUnit year2 = new YearUnit();
        year2.setId(2L);

        director.addAcademicYear(year1);
        director.addAcademicYear(year2);

        assertEquals(year2, director.getCurrentYear());
    }

    @Test
    void testGetPastYears() {
        DirectorUnit director = new DirectorUnit();
        YearUnit year1 = new YearUnit();
        year1.setId(1L);
        YearUnit year2 = new YearUnit();
        year2.setId(2L);

        director.addAcademicYear(year1);
        director.addAcademicYear(year2);

        List<YearUnit> pastYears = director.getPastYears();

        assertEquals(1, pastYears.size());
        assertEquals(year1, pastYears.get(0));
    }

    @Test
    void testEquals_SameObject_ShouldReturnTrue() {
        DirectorUnit director = new DirectorUnit(1L, "Test Director", "testuser", "password123", "Computer Science", new ArrayList<>(), new ArrayList<>());

        assertTrue(director.equals(director));
    }

    @Test
    void testEquals_DifferentObjectSameId_ShouldReturnTrue() {
        DirectorUnit director1 = new DirectorUnit(1L, "Director One", "user1", "password1", "Math", new ArrayList<>(), new ArrayList<>());
        DirectorUnit director2 = new DirectorUnit(1L, "Director Two", "user2", "password2", "Physics", new ArrayList<>(), new ArrayList<>());

        assertTrue(director1.equals(director2));
    }

    @Test
    void testEquals_DifferentId_ShouldReturnFalse() {
        DirectorUnit director1 = new DirectorUnit(1L, "Director One", "user1", "password1", "Math", new ArrayList<>(), new ArrayList<>());
        DirectorUnit director2 = new DirectorUnit(2L, "Director Two", "user2", "password2", "Physics", new ArrayList<>(), new ArrayList<>());

        assertFalse(director1.equals(director2));
    }

    @Test
    void testHashCode_SameId_ShouldBeEqual() {
        DirectorUnit director1 = new DirectorUnit(1L, "Director One", "user1", "password1", "Math", new ArrayList<>(), new ArrayList<>());
        DirectorUnit director2 = new DirectorUnit(1L, "Director Two", "user2", "password2", "Physics", new ArrayList<>(), new ArrayList<>());

        assertEquals(director1.hashCode(), director2.hashCode());
    }

    @Test
    void testHashCode_DifferentId_ShouldNotBeEqual() {
        DirectorUnit director1 = new DirectorUnit(1L, "Director One", "user1", "password1", "Math", new ArrayList<>(), new ArrayList<>());
        DirectorUnit director2 = new DirectorUnit(2L, "Director Two", "user2", "password2", "Physics", new ArrayList<>(), new ArrayList<>());

        assertNotEquals(director1.hashCode(), director2.hashCode());
    }

    @Test
    void testToString_ShouldContainAllFields() {
        DirectorUnit director = new DirectorUnit(1L, "Test Director", "testuser", "password123", "Computer Science", new ArrayList<>(), new ArrayList<>());

        String toString = director.toString();

        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("name='Test Director'"));
        assertTrue(toString.contains("username='testuser'"));
        assertTrue(toString.contains("password='password123'"));
        assertTrue(toString.contains("department='Computer Science'"));
    }
}
