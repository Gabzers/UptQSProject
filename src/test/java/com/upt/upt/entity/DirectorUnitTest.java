package com.upt.upt.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for DirectorUnit.
 * 
 * @autor Grupo 5 - 47719, 47713, 46697, 47752, 47004
 */
class DirectorUnitTest {

    private DirectorUnit director;

    /**
     * Set up test data.
     */
    @BeforeEach
    void setUp() {
        director = new DirectorUnit();
    }

    /**
     * Test addCoordinator method.
     */
    @Test
    void testAddCoordinator() {
        CoordinatorUnit coordinator = new CoordinatorUnit();
        coordinator.setId(1L);
        coordinator.setName("Test Coordinator");

        director.addCoordinator(coordinator);

        assertEquals(1, director.getCoordinators().size());
        assertEquals(coordinator, director.getCoordinators().get(0));
        assertEquals(director, coordinator.getDirectorUnit());
    }

    /**
     * Test addAcademicYear method.
     */
    @Test
    void testAddAcademicYear() {
        SemesterUnit firstSemester = new SemesterUnit();
        firstSemester.setId(1L);
        firstSemester.setStartDate("2023-01-01");
        firstSemester.setEndDate("2023-06-30");

        SemesterUnit secondSemester = new SemesterUnit();
        secondSemester.setId(2L);
        secondSemester.setStartDate("2023-07-01");
        secondSemester.setEndDate("2023-12-31");

        YearUnit year1 = new YearUnit();
        year1.setId(1L);
        year1.setFirstSemester(firstSemester);
        year1.setSecondSemester(secondSemester);
        year1.setSpecialExamStart("2023-11-01");
        year1.setSpecialExamEnd("2023-11-15");

        director.addAcademicYear(year1);

        assertEquals(1, director.getAcademicYears().size());
        assertEquals(year1, director.getAcademicYears().get(0));
        assertEquals(director, year1.getDirectorUnit());
    }

    /**
     * Test getCurrentYear method.
     */
    @Test
    void testGetCurrentYear() {
        YearUnit year1 = new YearUnit();
        year1.setId(1L);

        YearUnit year2 = new YearUnit();
        year2.setId(2L);

        director.addAcademicYear(year1);
        director.addAcademicYear(year2);

        assertEquals(year2, director.getCurrentYear());
    }

    /**
     * Test getPastYears method.
     */
    @Test
    void testGetPastYears() {
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

    /**
     * Test isCurrentYear method.
     */
    @Test
    void testIsCurrentYear() {
        SemesterUnit firstSemester = new SemesterUnit();
        firstSemester.setId(1L);
        firstSemester.setStartDate(LocalDate.now().minusMonths(1).toString());
        firstSemester.setEndDate(LocalDate.now().plusMonths(1).toString());

        SemesterUnit secondSemester = new SemesterUnit();
        secondSemester.setId(2L);
        secondSemester.setStartDate(LocalDate.now().plusMonths(2).toString());
        secondSemester.setEndDate(LocalDate.now().plusMonths(6).toString());

        YearUnit year = new YearUnit();
        year.setId(1L);
        year.setFirstSemester(firstSemester);
        year.setSecondSemester(secondSemester);

        assertTrue(year.isCurrentYear());
    }

    /**
     * Test equals and hashCode methods.
     */
    @Test
    void testEqualsAndHashCode() {
        DirectorUnit director1 = new DirectorUnit();
        director1.setId(1L);

        DirectorUnit director2 = new DirectorUnit();
        director2.setId(1L);

        DirectorUnit director3 = new DirectorUnit();
        director3.setId(2L);

        assertEquals(director1, director2);
        assertNotEquals(director1, director3);
        assertEquals(director1.hashCode(), director2.hashCode());
        assertNotEquals(director1.hashCode(), director3.hashCode());
    }

    /**
     * Test toString method.
     */
    @Test
    void testToString() {
        director.setId(1L);
        director.setName("Director Name");
        director.setUsername("director123");
        director.setPassword("password123");
        director.setDepartment("IT");

        String expected = "DirectorUnit{id=1, name='Director Name', username='director123', password='password123', department='IT'}";
        assertEquals(expected, director.toString());
    }
}
