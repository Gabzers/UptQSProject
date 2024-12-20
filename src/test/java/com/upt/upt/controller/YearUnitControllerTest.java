package com.upt.upt.controller;

import com.upt.upt.entity.DirectorUnit;
import com.upt.upt.entity.UserType;
import com.upt.upt.entity.YearUnit;
import com.upt.upt.service.DirectorUnitService;
import com.upt.upt.service.YearUnitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for YearUnitController.
 * 
 * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
 */
@ExtendWith(MockitoExtension.class)
public class YearUnitControllerTest {

    @Mock
    private YearUnitService yearUnitService;

    @Mock
    private DirectorUnitService directorUnitService;

    @Mock
    private Model model;

    @InjectMocks
    private YearUnitController yearUnitController;

    private MockHttpSession session;
    private DirectorUnit mockDirector;

    /**
     * Sets up the test environment before each test.
     */
    @BeforeEach
    void setUp() {
        session = new MockHttpSession();
        mockDirector = new DirectorUnit();
        mockDirector.setId(1L);

        session.setAttribute("userId", mockDirector.getId());
        session.setAttribute("userType", UserType.DIRECTOR);
    }

    /**
     * Tests the getDirectorYears method for an authorized user.
     */
    @Test
    void testGetDirectorYears_Authorized() {
        YearUnit currentYear = new YearUnit();
        currentYear.setId(1L);
        mockDirector.addAcademicYear(currentYear);

        when(directorUnitService.getDirectorById(anyLong()))
                .thenReturn(Optional.of(mockDirector));

        String viewName = yearUnitController.getDirectorYears(session, model);

        assertEquals("director_index", viewName);
        verify(model).addAttribute("loggedInDirector", mockDirector);
        verify(model).addAttribute("currentYear", currentYear);
        verify(model).addAttribute("pastYears", List.of());
    }

    /**
     * Tests the getDirectorYears method for an unauthorized user.
     */
    @Test
    void testGetDirectorYears_Unauthorized() {
        session.removeAttribute("userType");

        String viewName = yearUnitController.getDirectorYears(session, model);

        assertEquals("redirect:/login?error=Unauthorized access", viewName);
    }

    /**
     * Tests the newYearForm method for an authorized user.
     */
    @Test
    void testNewYearForm_Authorized() {
        String viewName = yearUnitController.newYearForm(session, model);

        assertEquals("director_newYear", viewName);
        verify(model).addAttribute(eq("yearUnit"), any(YearUnit.class));
        verify(model).addAttribute(eq("firstSemester"), any());
        verify(model).addAttribute(eq("secondSemester"), any());
    }

    /**
     * Tests the saveNewYear method for an authorized user.
     */
    @Test
    void testSaveNewYear_Authorized() {
        YearUnit yearUnit = new YearUnit();
        Map<String, String> params = new HashMap<>();

        when(directorUnitService.getDirectorById(anyLong()))
                .thenReturn(Optional.of(mockDirector));
        when(yearUnitService.validateNewYearDates(eq(params), eq(model), anyLong()))
                .thenReturn(true);

        String viewName = yearUnitController.saveNewYear(yearUnit, params, session, model);

        assertEquals("redirect:/director", viewName);
        verify(yearUnitService).saveNewYear(eq(yearUnit), eq(params), eq(session));
    }

    /**
     * Tests the saveNewYear method when validation fails.
     */
    @Test
    void testSaveNewYear_ValidationFailed() {
        YearUnit yearUnit = new YearUnit();
        Map<String, String> params = new HashMap<>();

        when(directorUnitService.getDirectorById(anyLong()))
                .thenReturn(Optional.of(mockDirector));
        when(yearUnitService.validateNewYearDates(eq(params), eq(model), anyLong()))
                .thenReturn(false);

        String viewName = yearUnitController.saveNewYear(yearUnit, params, session, model);

        assertEquals("director_newYear", viewName);
    }

    /**
     * Tests the editYearForm method for an authorized user.
     */
    @Test
    void testEditYearForm_Authorized() {
        YearUnit yearUnit = new YearUnit();
        when(yearUnitService.getYearUnitById(anyLong()))
                .thenReturn(Optional.of(yearUnit));

        String viewName = yearUnitController.editYearForm(1L, session, model);

        assertEquals("director_editYear", viewName);
        verify(model).addAttribute("yearUnit", yearUnit);
    }

    /**
     * Tests the deleteYear method for an authorized user.
     */
    @Test
    void testDeleteYear_Authorized() {
        String viewName = yearUnitController.deleteYear(1L, model, session);

        assertEquals("director_confirmRemoveYear", viewName);
        verify(model).addAttribute("warning", "Are you sure you want to remove this year?");
        verify(model).addAttribute("yearId", 1L);
    }

    /**
     * Tests the confirmRemoveYear method for an authorized user.
     */
    @Test
    void testConfirmRemoveYear_Authorized() {
        String viewName = yearUnitController.confirmRemoveYear(1L, session);

        assertEquals("redirect:/director", viewName);
        verify(yearUnitService).deleteYearUnit(1L);
    }
}