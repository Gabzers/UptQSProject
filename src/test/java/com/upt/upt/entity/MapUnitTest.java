package com.upt.upt.entity;

import com.upt.upt.repository.MapUnitRepository;
import com.upt.upt.repository.SemesterUnitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for MapUnit.
 * 
 * @autor Grupo 5 - 47719, 47713, 46697, 47752, 47004
 */
@DataJpaTest
public class MapUnitTest {

    @Autowired
    private MapUnitRepository mapUnitRepository;

    @Autowired
    private SemesterUnitRepository semesterUnitRepository;

    private MapUnit mapUnit;
    private SemesterUnit semesterUnit;

    /**
     * Set up test data.
     */
    @BeforeEach
    public void setUp() {
        // Create SemesterUnit instance
        semesterUnit = new SemesterUnit();
        semesterUnit.setStartDate("2024-01-01");
        semesterUnit.setEndDate("2024-06-30");
        semesterUnit.setExamPeriodStart("2024-05-01");
        semesterUnit.setExamPeriodEnd("2024-05-15");
        semesterUnit.setResitPeriodStart("2024-06-01");
        semesterUnit.setResitPeriodEnd("2024-06-10");

        // Save SemesterUnit instance to repository
        semesterUnit = semesterUnitRepository.save(semesterUnit);

        // Create MapUnit instance
        mapUnit = new MapUnit();
        mapUnit.setSemesterUnit(semesterUnit);

        List<AssessmentUnit> assessments = new ArrayList<>();
        AssessmentUnit assessment1 = new AssessmentUnit();
        assessment1.setType("Test");
        assessment1.setWeight(30);
        assessment1.setMap(mapUnit);
        assessments.add(assessment1);

        AssessmentUnit assessment2 = new AssessmentUnit();
        assessment2.setType("Presentation");
        assessment2.setWeight(20);
        assessment2.setMap(mapUnit);
        assessments.add(assessment2);

        mapUnit.setAssessments(assessments);
    }

    /**
     * Test save MapUnit method.
     */
    @Test
    public void testSaveMapUnit() {
        MapUnit savedMapUnit = mapUnitRepository.save(mapUnit);
        assertThat(savedMapUnit).isNotNull();
        assertThat(savedMapUnit.getId()).isNotNull();
        assertThat(savedMapUnit.getSemesterUnit()).isEqualTo(semesterUnit);
        assertThat(savedMapUnit.getAssessments()).hasSize(2);
    }

    /**
     * Test find MapUnit by ID method.
     */
    @Test
    public void testFindMapUnitById() {
        mapUnitRepository.save(mapUnit);
        MapUnit foundMapUnit = mapUnitRepository.findById(mapUnit.getId()).orElse(null);
        assertThat(foundMapUnit).isNotNull();
        assertThat(foundMapUnit.getSemesterUnit()).isEqualTo(semesterUnit);
        assertThat(foundMapUnit.getAssessments()).hasSize(2);
    }

    /**
     * Test update MapUnit method.
     */
    @Test
    public void testUpdateMapUnit() {
        MapUnit savedMapUnit = mapUnitRepository.save(mapUnit);
        savedMapUnit.getSemesterUnit().setEndDate("2024-07-01");
        mapUnitRepository.save(savedMapUnit);

        MapUnit updatedMapUnit = mapUnitRepository.findById(savedMapUnit.getId()).orElse(null);
        assertThat(updatedMapUnit).isNotNull();
        assertThat(updatedMapUnit.getSemesterUnit().getEndDate()).isEqualTo("2024-07-01");
    }

    /**
     * Test delete MapUnit method.
     */
    @Test
    public void testDeleteMapUnit() {
        mapUnitRepository.save(mapUnit);
        mapUnitRepository.deleteById(mapUnit.getId());
        MapUnit deletedMapUnit = mapUnitRepository.findById(mapUnit.getId()).orElse(null);
        assertThat(deletedMapUnit).isNull();
    }
}
