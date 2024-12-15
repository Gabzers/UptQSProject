package com.upt.upt.service;

import com.upt.upt.entity.*;
import com.upt.upt.repository.CurricularUnitRepository;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CurricularUnitServiceTest {

    @Mock
    private CurricularUnitRepository curricularUnitRepository;

    @Mock
    private CoordinatorUnitService coordinatorUnitService;

    @Mock
    private AssessmentUnitService assessmentUnitService;

    @Mock
    private YearUnitService yearUnitService;

    @InjectMocks
    private CurricularUnitService curricularUnitService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    private CurricularUnit curricularUnit;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

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
    void testSaveCurricularUnit() {
        when(curricularUnitRepository.save(curricularUnit)).thenReturn(curricularUnit);

        CurricularUnit savedUnit = curricularUnitService.saveCurricularUnit(curricularUnit);

        assertNotNull(savedUnit);
        assertEquals("Mathematics", savedUnit.getNameUC());
        verify(curricularUnitRepository, times(1)).save(curricularUnit);
    }

    @Test
    void testGetAllCurricularUnits() {
        when(curricularUnitRepository.findAll()).thenReturn(List.of(curricularUnit));

        List<CurricularUnit> units = curricularUnitService.getAllCurricularUnits();

        assertNotNull(units);
        assertEquals(1, units.size());
        assertEquals("Mathematics", units.get(0).getNameUC());
        verify(curricularUnitRepository, times(1)).findAll();
    }

    @Test
    void testGetCurricularUnitById() {
        when(curricularUnitRepository.findById(1L)).thenReturn(Optional.of(curricularUnit));

        Optional<CurricularUnit> result = curricularUnitService.getCurricularUnitById(1L);

        assertTrue(result.isPresent());
        assertEquals("Mathematics", result.get().getNameUC());
        verify(curricularUnitRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateCurricularUnit() {
        CurricularUnit updatedUnit = new CurricularUnit();
        updatedUnit.setNameUC("Physics");
        updatedUnit.setStudentsNumber(50);

        when(curricularUnitRepository.findById(1L)).thenReturn(Optional.of(curricularUnit));
        when(curricularUnitRepository.save(any(CurricularUnit.class))).thenReturn(curricularUnit);

        CurricularUnit result = curricularUnitService.updateCurricularUnit(1L, updatedUnit);

        assertNotNull(result);
        assertEquals("Physics", result.getNameUC());
        assertEquals(50, result.getStudentsNumber());
        verify(curricularUnitRepository, times(1)).save(curricularUnit);
    }

    @Test
    void testDeleteCurricularUnit() {
        doNothing().when(curricularUnitRepository).deleteById(1L);

        curricularUnitService.deleteCurricularUnit(1L);

        verify(curricularUnitRepository, times(1)).deleteById(1L);
    }

    @Test
    void testValidateEvaluationsCount() {
        when(model.addAttribute(anyString(), any())).thenReturn(model);

        boolean result = curricularUnitService.validateEvaluationsCount(curricularUnit, 1, "Mixed", model);

        assertFalse(result);
        verify(model, times(1)).addAttribute(eq("error"), anyString());

        result = curricularUnitService.validateEvaluationsCount(curricularUnit, 3, "Continuous", model);

        assertTrue(result);
    }

    @Test
    void testCreateCurricularUnit() {
        when(session.getAttribute("userId")).thenReturn(1L);
        CoordinatorUnit coordinatorUnit = mock(CoordinatorUnit.class);
        DirectorUnit directorUnit = mock(DirectorUnit.class);
        YearUnit currentYear = mock(YearUnit.class);
        SemesterUnit semesterUnit = mock(SemesterUnit.class);

        when(coordinatorUnitService.getCoordinatorById(1L)).thenReturn(Optional.of(coordinatorUnit));
        when(coordinatorUnit.getDirectorUnit()).thenReturn(directorUnit);
        when(directorUnit.getCurrentYear()).thenReturn(currentYear);
        when(currentYear.getFirstSemester()).thenReturn(semesterUnit);

        Map<String, String> params = Map.of(
                "ucName", "Mathematics",
                "ucNumStudents", "100",
                "ucEvaluationType", "Mixed",
                "ucAttendance", "true",
                "ucEvaluationsCount", "2",
                "ucYear", "1",
                "ucSemester", "1"
        );

        CurricularUnit createdUnit = curricularUnitService.createCurricularUnit(params, session, model);

        assertNotNull(createdUnit);
        assertEquals("Mathematics", createdUnit.getNameUC());
        verify(curricularUnitRepository, never()).save(any());
    }
}
