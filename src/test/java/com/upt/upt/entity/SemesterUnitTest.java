package com.upt.upt.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SemesterUnitTest {

    private SemesterUnit semesterUnit;

    @BeforeEach
    void setUp() {
        semesterUnit = new SemesterUnit(
                1L,
                "2024-02-01",
                "2024-07-01",
                "2024-06-01",
                "2024-06-15",
                "2024-06-20",
                "2024-06-30"
        );
    }

    @Test
    void testGettersAndSetters() {
        semesterUnit.setStartDate("2024-01-01");
        assertEquals("2024-01-01", semesterUnit.getStartDate());

        semesterUnit.setEndDate("2024-06-30");
        assertEquals("2024-06-30", semesterUnit.getEndDate());
    }

    @Test
    void testAddCurricularUnit() {
        CurricularUnit curricularUnit = new CurricularUnit();
        curricularUnit.setNameUC("Mathematics");

        semesterUnit.setCurricularUnits(new ArrayList<>());

        semesterUnit.addCurricularUnit(curricularUnit);

        assertEquals(1, semesterUnit.getCurricularUnits().size());
        assertEquals(semesterUnit, curricularUnit.getSemesterUnit());
    }

    @Test
    void testRemoveCurricularUnit() {
        CurricularUnit curricularUnit = new CurricularUnit();
        curricularUnit.setNameUC("Mathematics");

        List<CurricularUnit> curricularUnits = new ArrayList<>();
        curricularUnits.add(curricularUnit);

        semesterUnit.setCurricularUnits(curricularUnits);
        semesterUnit.removeCurricularUnit(curricularUnit);

        assertEquals(0, semesterUnit.getCurricularUnits().size());
        assertNull(curricularUnit.getSemesterUnit());
    }

    @Test
    void testGetYear() {
        assertEquals(2024, semesterUnit.getYear());
    }

    @Test
    void testEqualsAndHashCode() {
        SemesterUnit otherSemester = new SemesterUnit(
                1L,
                "2024-02-01",
                "2024-07-01",
                "2024-06-01",
                "2024-06-15",
                "2024-06-20",
                "2024-06-30"
        );

        assertEquals(semesterUnit, otherSemester);
        assertEquals(semesterUnit.hashCode(), otherSemester.hashCode());

        otherSemester.setId(2L);
        assertNotEquals(semesterUnit, otherSemester);
    }

    @Test
    void testToString() {
        String expected = "SemesterUnit{id=1, startDate='2024-02-01', endDate='2024-07-01', examPeriodStart='2024-06-01', " +
                "examPeriodEnd='2024-06-15', resitPeriodStart='2024-06-20', resitPeriodEnd='2024-06-30'}";
        assertEquals(expected, semesterUnit.toString());
    }
}
