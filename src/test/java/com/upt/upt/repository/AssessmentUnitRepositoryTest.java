package com.upt.upt.repository;

import com.upt.upt.entity.AssessmentUnit;
import com.upt.upt.entity.CurricularUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for AssessmentUnitRepository.
 * 
 * @autor Grupo 5 - 47719, 47713, 46697, 47752, 47004
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class AssessmentUnitRepositoryTest {

    @Autowired
    private AssessmentUnitRepository assessmentUnitRepository;

    private AssessmentUnit assessmentUnit;

    /**
     * Set up test data.
     */
    @BeforeEach
    public void setUp() {
        // Create an instance of CurricularUnit associated
        CurricularUnit curricularUnit = new CurricularUnit();
        curricularUnit.setId(1L); // Set a valid ID, depending on your mapping or database

        // Initialize the AssessmentUnit with valid data
        assessmentUnit = new AssessmentUnit();
        assessmentUnit.setType("Exame");
        assessmentUnit.setWeight(50);
        assessmentUnit.setExamPeriod("Normal");
        assessmentUnit.setComputerRequired(false);
        assessmentUnit.setClassTime(true);
        assessmentUnit.setStartTime(LocalDateTime.now().plusDays(1));
        assessmentUnit.setEndTime(LocalDateTime.now().plusDays(1).plusHours(2));
        assessmentUnit.setMinimumGrade(10.0);
        assessmentUnit.setCurricularUnit(curricularUnit);

        // Save the entity to the database
        assessmentUnitRepository.save(assessmentUnit);
    }

    /**
     * Test findByCurricularUnitId method.
     */
    @Test
    public void testFindByCurricularUnitId() {
        List<AssessmentUnit> result = assessmentUnitRepository.findByCurricularUnitId(1L);
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getCurricularUnit().getId()).isEqualTo(1L);
    }

    /**
     * Test findByCurricularUnitIdAndId method.
     */
    @Test
    public void testFindByCurricularUnitIdAndId() {
        Optional<AssessmentUnit> result = assessmentUnitRepository.findByCurricularUnitIdAndId(1L, assessmentUnit.getId());
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(assessmentUnit.getId());
    }

    /**
     * Test findAll method.
     */
    @Test
    public void testFindAll() {
        List<AssessmentUnit> result = assessmentUnitRepository.findAll();
        assertThat(result).isNotEmpty();
    }

    /**
     * Test findByCurricularUnitCoordinatorId method.
     */
    @Test
    public void testFindByCurricularUnitCoordinatorId() {
        // Add coordinatorId to the test entity if applicable
        assessmentUnitRepository.save(assessmentUnit);
        List<AssessmentUnit> result = assessmentUnitRepository.findByCurricularUnitCoordinatorId(1L);
        assertThat(result).isEmpty(); // Update as needed for actual data
    }

    /**
     * Test findByCurricularUnit_SemesterUnit_Id method.
     */
    @Test
    public void testFindByCurricularUnit_SemesterUnit_Id() {
        // Add semesterUnitId to the test entity if applicable
        List<AssessmentUnit> result = assessmentUnitRepository.findByCurricularUnit_SemesterUnit_Id(1L);
        assertThat(result).isEmpty(); // Update as needed for actual data
    }

    /**
     * Test findByRooms_Id method.
     */
    @Test
    public void testFindByRooms_Id() {
        // Add roomId to the test entity if applicable
        List<AssessmentUnit> result = assessmentUnitRepository.findByRooms_Id(1L);
        assertThat(result).isEmpty(); // Update as needed for actual data
    }

    /**
     * Test findByCurricularUnit_Year method.
     */
    @Test
    public void testFindByCurricularUnit_Year() {
        // Add year to the test entity if applicable
        List<AssessmentUnit> result = assessmentUnitRepository.findByCurricularUnit_Year(2024);
        assertThat(result).isEmpty(); // Update as needed for actual data
    }

    /**
     * Test findByCurricularUnit_Semester method.
     */
    @Test
    public void testFindByCurricularUnit_Semester() {
        // Add semester to the test entity if applicable
        List<AssessmentUnit> result = assessmentUnitRepository.findByCurricularUnit_Semester(1);
        assertThat(result).isEmpty(); // Update as needed for actual data
    }

    /**
     * Test findByCurricularUnit_YearAndCurricularUnit_SemesterAndCurricularUnit_CoordinatorId method.
     */
    @Test
    public void testFindByCurricularUnit_YearAndCurricularUnit_SemesterAndCurricularUnit_CoordinatorId() {
        // Add year, semester, and coordinatorId to the test entity if applicable
        List<AssessmentUnit> result = assessmentUnitRepository.findByCurricularUnit_YearAndCurricularUnit_SemesterAndCurricularUnit_CoordinatorId(2024, 1, 1L);
        assertThat(result).isEmpty(); // Update as needed for actual data
    }

    /**
     * Test findByCurricularUnit_SemesterAndCurricularUnit_CoordinatorIdAndCurricularUnit_YearNot method.
     */
    @Test
    public void testFindByCurricularUnit_SemesterAndCurricularUnit_CoordinatorIdAndCurricularUnit_YearNot() {
        // Add semester, coordinatorId, and year to the test entity if applicable
        List<AssessmentUnit> result = assessmentUnitRepository.findByCurricularUnit_SemesterAndCurricularUnit_CoordinatorIdAndCurricularUnit_YearNot(1, 1L, 2024);
        assertThat(result).isEmpty(); // Update as needed for actual data
    }
}
