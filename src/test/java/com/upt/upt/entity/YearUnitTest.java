package com.upt.upt.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for YearUnit.
 * 
 * @autor Grupo 5 - 47719, 47713, 46697, 47752, 47004
 */
public class YearUnitTest {

    private YearUnit yearUnit;
    private SemesterUnit firstSemester;
    private SemesterUnit secondSemester;
    private DirectorUnit directorUnit;

    /**
     * Set up test data.
     */
    @BeforeEach
    void setUp() {
        // Create mock dependencies
        directorUnit = mock(DirectorUnit.class);
        firstSemester = mock(SemesterUnit.class);
        secondSemester = mock(SemesterUnit.class);

        // Create YearUnit instance
        yearUnit = new YearUnit();
        yearUnit.setDirectorUnit(directorUnit);
        yearUnit.setFirstSemester(firstSemester);
        yearUnit.setSecondSemester(secondSemester);
    }

    /**
     * Test constructor and getters.
     */
    @Test
    void testConstructorAndGetters() {
        // Arrange
        Long testId = 1L;
        String testSpecialExamStart = "2024-02-01";
        String testSpecialExamEnd = "2024-02-15";

        // Create a new YearUnit with all parameters
        YearUnit fullYearUnit = new YearUnit(
                testId,
                firstSemester,
                secondSemester,
                testSpecialExamStart,
                testSpecialExamEnd,
                directorUnit
        );

        // Assert
        assertEquals(testId, fullYearUnit.getId());
        assertEquals(firstSemester, fullYearUnit.getFirstSemester());
        assertEquals(secondSemester, fullYearUnit.getSecondSemester());
        assertEquals(testSpecialExamStart, fullYearUnit.getSpecialExamStart());
        assertEquals(testSpecialExamEnd, fullYearUnit.getSpecialExamEnd());
        assertEquals(directorUnit, fullYearUnit.getDirectorUnit());
    }

    /**
     * Test setters.
     */
    @Test
    void testSetters() {
        // Arrange
        Long testId = 2L;
        String testSpecialExamStart = "2024-02-01";
        String testSpecialExamEnd = "2024-02-15";
        SemesterUnit newFirstSemester = mock(SemesterUnit.class);
        SemesterUnit newSecondSemester = mock(SemesterUnit.class);
        DirectorUnit newDirectorUnit = mock(DirectorUnit.class);

        // Act
        yearUnit.setId(testId);
        yearUnit.setFirstSemester(newFirstSemester);
        yearUnit.setSecondSemester(newSecondSemester);
        yearUnit.setSpecialExamStart(testSpecialExamStart);
        yearUnit.setSpecialExamEnd(testSpecialExamEnd);
        yearUnit.setDirectorUnit(newDirectorUnit);

        // Assert
        assertEquals(testId, yearUnit.getId());
        assertEquals(newFirstSemester, yearUnit.getFirstSemester());
        assertEquals(newSecondSemester, yearUnit.getSecondSemester());
        assertEquals(testSpecialExamStart, yearUnit.getSpecialExamStart());
        assertEquals(testSpecialExamEnd, yearUnit.getSpecialExamEnd());
        assertEquals(newDirectorUnit, yearUnit.getDirectorUnit());
    }

    /**
     * Test isCurrentYear method within first semester.
     */
    @Test
    void testIsCurrentYear_WithinFirstSemester() {
        // Arrange
        String currentDate = LocalDate.now().toString();
        String startDate = LocalDate.now().minusDays(10).toString();
        String endDate = LocalDate.now().plusDays(10).toString();
        String secondSemesterEnd = LocalDate.now().plusMonths(3).toString();

        when(firstSemester.getStartDate()).thenReturn(startDate);
        when(secondSemester.getEndDate()).thenReturn(secondSemesterEnd);

        // Act
        boolean isCurrentYear = yearUnit.isCurrentYear();

        // Assert
        assertTrue(isCurrentYear);
    }

    /**
     * Test isCurrentYear method before first semester.
     */
    @Test
    void testIsCurrentYear_BeforeFirstSemester() {
        // Arrange
        String startDate = LocalDate.now().plusDays(10).toString();
        String secondSemesterEnd = LocalDate.now().plusMonths(3).toString();

        when(firstSemester.getStartDate()).thenReturn(startDate);
        when(secondSemester.getEndDate()).thenReturn(secondSemesterEnd);

        // Act
        boolean isCurrentYear = yearUnit.isCurrentYear();

        // Assert
        assertFalse(isCurrentYear);
    }

    /**
     * Test isCurrentYear method after second semester.
     */
    @Test
    void testIsCurrentYear_AfterSecondSemester() {
        // Arrange
        String startDate = LocalDate.now().minusMonths(4).toString();
        String secondSemesterEnd = LocalDate.now().minusDays(10).toString();

        when(firstSemester.getStartDate()).thenReturn(startDate);
        when(secondSemester.getEndDate()).thenReturn(secondSemesterEnd);

        // Act
        boolean isCurrentYear = yearUnit.isCurrentYear();

        // Assert
        assertFalse(isCurrentYear);
    }

    /**
     * Test equals method.
     */
    @Test
    void testEquals() {
        // Arrange
        YearUnit yearUnit1 = new YearUnit();
        yearUnit1.setId(1L);

        YearUnit yearUnit2 = new YearUnit();
        yearUnit2.setId(1L);

        YearUnit yearUnit3 = new YearUnit();
        yearUnit3.setId(2L);

        // Assert
        assertEquals(yearUnit1, yearUnit2);
        assertNotEquals(yearUnit1, yearUnit3);
        assertNotEquals(yearUnit1, null);
        assertEquals(yearUnit1, yearUnit1);
    }

    /**
     * Test hashCode method.
     */
    @Test
    void testHashCode() {
        // Arrange
        YearUnit yearUnit1 = new YearUnit();
        yearUnit1.setId(1L);

        YearUnit yearUnit2 = new YearUnit();
        yearUnit2.setId(1L);

        // Assert
        assertEquals(yearUnit1.hashCode(), yearUnit2.hashCode());
    }

    /**
     * Test toString method.
     */
    @Test
    void testToString() {
        // Arrange
        yearUnit.setId(1L);
        yearUnit.setSpecialExamStart("2024-02-01");
        yearUnit.setSpecialExamEnd("2024-02-15");

        // Act
        String toStringResult = yearUnit.toString();

        // Assert
        assertTrue(toStringResult.contains("id=1"));
        assertTrue(toStringResult.contains("specialExamStart='2024-02-01'"));
        assertTrue(toStringResult.contains("specialExamEnd='2024-02-15'"));
    }
}