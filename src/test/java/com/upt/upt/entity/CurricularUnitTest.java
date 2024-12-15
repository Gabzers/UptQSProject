package com.upt.upt.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CurricularUnitTest {

    private CurricularUnit curricularUnit;

    @BeforeEach
    void setUp() {
        curricularUnit = new CurricularUnit();
        curricularUnit.setId(1L);
        curricularUnit.setNameUC("Mathematics");
        curricularUnit.setStudentsNumber(100);
        curricularUnit.setEvaluationType("Mixed");
        curricularUnit.setAttendance(true);
        curricularUnit.setEvaluationsCount(2);
        curricularUnit.setYear(1);
        curricularUnit.setSemester(1);
    }

    @Test
    void testHasExamPeriodEvaluation() {
        AssessmentUnit assessment = mock(AssessmentUnit.class);
        when(assessment.getExamPeriod()).thenReturn("Exam Period");

        curricularUnit.setAssessments(List.of(assessment));
        assertTrue(curricularUnit.hasExamPeriodEvaluation());
    }

    @Test
    void testIsMixedAndMissingExamPeriod() {
        AssessmentUnit assessment = mock(AssessmentUnit.class);
        when(assessment.getExamPeriod()).thenReturn("Teaching Period");

        curricularUnit.setAssessments(List.of(assessment));
        assertTrue(curricularUnit.isMixedAndMissingExamPeriod());
    }

    @Test
    void testIsEvaluationsCountMismatch() {
        AssessmentUnit assessment1 = mock(AssessmentUnit.class);
        AssessmentUnit assessment2 = mock(AssessmentUnit.class);

        curricularUnit.setAssessments(List.of(assessment1));
        assertTrue(curricularUnit.isEvaluationsCountMismatch());

        curricularUnit.setAssessments(List.of(assessment1, assessment2));
        assertFalse(curricularUnit.isEvaluationsCountMismatch());
    }

    @Test
    void testIsTotalWeightInvalid() {
        AssessmentUnit assessment1 = mock(AssessmentUnit.class);
        when(assessment1.getExamPeriod()).thenReturn("Teaching Period");
        when(assessment1.getWeight()).thenReturn(50);

        AssessmentUnit assessment2 = mock(AssessmentUnit.class);
        when(assessment2.getExamPeriod()).thenReturn("Exam Period");
        when(assessment2.getWeight()).thenReturn(50);

        curricularUnit.setAssessments(List.of(assessment1, assessment2));
        assertFalse(curricularUnit.isTotalWeightInvalid());

        when(assessment2.getWeight()).thenReturn(30);
        assertTrue(curricularUnit.isTotalWeightInvalid());
    }

    @Test
    void testIsTotalWeightLessThan100() {
        AssessmentUnit assessment1 = mock(AssessmentUnit.class);
        when(assessment1.getWeight()).thenReturn(30);

        AssessmentUnit assessment2 = mock(AssessmentUnit.class);
        when(assessment2.getWeight()).thenReturn(40);

        curricularUnit.setAssessments(List.of(assessment1, assessment2));
        assertTrue(curricularUnit.isTotalWeightLessThan100());

        when(assessment2.getWeight()).thenReturn(70);
        assertFalse(curricularUnit.isTotalWeightLessThan100());
    }

    @Test
    void testGetNormalPeriodAssessments() {
        AssessmentUnit teachingAssessment = mock(AssessmentUnit.class);
        when(teachingAssessment.getExamPeriod()).thenReturn("Teaching Period");

        AssessmentUnit examAssessment = mock(AssessmentUnit.class);
        when(examAssessment.getExamPeriod()).thenReturn("Exam Period");

        AssessmentUnit resourceAssessment = mock(AssessmentUnit.class);
        when(resourceAssessment.getExamPeriod()).thenReturn("Resource Period");

        curricularUnit.setAssessments(List.of(teachingAssessment, examAssessment, resourceAssessment));
        List<AssessmentUnit> normalAssessments = curricularUnit.getNormalPeriodAssessments();

        assertEquals(2, normalAssessments.size());
        assertTrue(normalAssessments.contains(teachingAssessment));
        assertTrue(normalAssessments.contains(examAssessment));
    }

    @Test
    void testHasAssessments() {
        assertFalse(curricularUnit.hasAssessments());

        AssessmentUnit assessment = mock(AssessmentUnit.class);
        curricularUnit.setAssessments(List.of(assessment));
        assertTrue(curricularUnit.hasAssessments());
    }
}
