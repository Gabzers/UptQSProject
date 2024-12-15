package com.upt.upt.repository;

import com.upt.upt.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CurricularUnitRepositoryTest {

    @Autowired
    private CurricularUnitRepository curricularUnitRepository;

    @Test
    public void testSaveAndFindCurricularUnit() {
        // Arrange
        CurricularUnit curricularUnit = new CurricularUnit();
        curricularUnit.setNameUC("Mathematics");
        curricularUnit.setStudentsNumber(50);
        curricularUnit.setEvaluationType("Mixed");
        curricularUnit.setAttendance(true);
        curricularUnit.setEvaluationsCount(2);
        curricularUnit.setYear(1);
        curricularUnit.setSemester(1);

        // Act
        CurricularUnit savedUnit = curricularUnitRepository.save(curricularUnit);
        Optional<CurricularUnit> foundUnit = curricularUnitRepository.findById(savedUnit.getId());

        // Assert
        assertThat(foundUnit).isPresent();
        assertThat(foundUnit.get().getNameUC()).isEqualTo("Mathematics");
        assertThat(foundUnit.get().getStudentsNumber()).isEqualTo(50);
    }

    @Test
    public void testUpdateCurricularUnit() {
        // Arrange
        CurricularUnit curricularUnit = new CurricularUnit();
        curricularUnit.setNameUC("Chemistry");
        curricularUnit.setStudentsNumber(30);
        curricularUnit.setEvaluationType("Continua");
        curricularUnit.setAttendance(false);
        curricularUnit.setEvaluationsCount(1);
        curricularUnit.setYear(2);
        curricularUnit.setSemester(2);
        CurricularUnit savedUnit = curricularUnitRepository.save(curricularUnit);

        // Act
        savedUnit.setNameUC("Advanced Chemistry");
        CurricularUnit updatedUnit = curricularUnitRepository.save(savedUnit);

        // Assert
        assertThat(updatedUnit.getNameUC()).isEqualTo("Advanced Chemistry");
    }

    @Test
    public void testDeleteCurricularUnit() {
        // Arrange
        CurricularUnit curricularUnit = new CurricularUnit();
        curricularUnit.setNameUC("Biology");
        curricularUnit.setStudentsNumber(40);
        curricularUnit.setEvaluationType("Mista");
        curricularUnit.setAttendance(true);
        curricularUnit.setEvaluationsCount(3);
        curricularUnit.setYear(3);
        curricularUnit.setSemester(1);
        CurricularUnit savedUnit = curricularUnitRepository.save(curricularUnit);

        // Act
        curricularUnitRepository.delete(savedUnit);
        Optional<CurricularUnit> foundUnit = curricularUnitRepository.findById(savedUnit.getId());

        // Assert
        assertThat(foundUnit).isNotPresent();
    }

    @Test
    public void testTotalWeightValidation() {
        // Arrange
        CurricularUnit curricularUnit = new CurricularUnit();
        curricularUnit.setNameUC("Physics");
        curricularUnit.setStudentsNumber(35);
        curricularUnit.setEvaluationType("Mista");
        curricularUnit.setAttendance(true);
        curricularUnit.setEvaluationsCount(2);
        curricularUnit.setYear(1);
        curricularUnit.setSemester(1);

        AssessmentUnit assessment1 = new AssessmentUnit();
        assessment1.setExamPeriod("Teaching Period");
        assessment1.setWeight(60);
        assessment1.setCurricularUnit(curricularUnit);

        AssessmentUnit assessment2 = new AssessmentUnit();
        assessment2.setExamPeriod("Exam Period");
        assessment2.setWeight(40);
        assessment2.setCurricularUnit(curricularUnit);

        curricularUnit.setAssessments(List.of(assessment1, assessment2));

        // Act
        CurricularUnit savedUnit = curricularUnitRepository.save(curricularUnit);

        // Assert
        assertThat(savedUnit.isTotalWeightInvalid()).isFalse();
    }

    @Test
    public void testMissingExamPeriodValidation() {
        // Arrange
        CurricularUnit curricularUnit = new CurricularUnit();
        curricularUnit.setNameUC("Programming");
        curricularUnit.setStudentsNumber(45);
        curricularUnit.setEvaluationType("Mixed");
        curricularUnit.setAttendance(false);
        curricularUnit.setEvaluationsCount(1);
        curricularUnit.setYear(2);
        curricularUnit.setSemester(2);

        AssessmentUnit assessment = new AssessmentUnit();
        assessment.setExamPeriod("Teaching Period");
        assessment.setWeight(100);
        assessment.setCurricularUnit(curricularUnit);

        curricularUnit.setAssessments(List.of(assessment));

        // Act
        CurricularUnit savedUnit = curricularUnitRepository.save(curricularUnit);

        // Assert
        assertThat(savedUnit.isMixedAndMissingExamPeriod()).isTrue();
    }
}
