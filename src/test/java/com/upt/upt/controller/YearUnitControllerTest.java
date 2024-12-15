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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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

    @BeforeEach
    void setUp() {
        // Setup mock session and director
        session = new MockHttpSession();
        mockDirector = new DirectorUnit();
        mockDirector.setId(1L);

        session.setAttribute("userId", mockDirector.getId());
        session.setAttribute("userType", UserType.DIRECTOR);
    }

    @Test
    void testGetDirectorYears_Authorized() {
        // Arrange
        YearUnit currentYear = new YearUnit();
        currentYear.setId(1L); // Ensure this has the highest ID

        // Add the current year to the director's academic years
        mockDirector.addAcademicYear(currentYear);

        when(directorUnitService.getDirectorById(anyLong()))
                .thenReturn(Optional.of(mockDirector));

        // Act
        String viewName = yearUnitController.getDirectorYears(session, model);

        // Assert
        assertEquals("director_index", viewName);
        verify(model).addAttribute("loggedInDirector", mockDirector);
        verify(model).addAttribute("currentYear", currentYear);
        verify(model).addAttribute("pastYears", List.of());
    }

    @Test
    void testGetDirectorYears_Unauthorized() {
        // Arrange
        session.removeAttribute("userType");

        // Act
        String viewName = yearUnitController.getDirectorYears(session, model);

        // Assert
        assertEquals("redirect:/login?error=Unauthorized access", viewName);
    }

    @Test
    void testNewYearForm_Authorized() {
        // Act
        String viewName = yearUnitController.newYearForm(session, model);

        // Assert
        assertEquals("director_newYear", viewName);
        verify(model).addAttribute(eq("yearUnit"), any(YearUnit.class));
        verify(model).addAttribute(eq("firstSemester"), any());
        verify(model).addAttribute(eq("secondSemester"), any());
    }

    @Test
    void testSaveNewYear_Authorized() {
        // Arrange
        YearUnit yearUnit = new YearUnit();
        Map<String, String> params = new HashMap<>();

        when(directorUnitService.getDirectorById(anyLong()))
                .thenReturn(Optional.of(mockDirector));
        when(yearUnitService.validateNewYearDates(eq(params), eq(model), anyLong()))
                .thenReturn(true);

        // Act
        String viewName = yearUnitController.saveNewYear(yearUnit, params, session, model);

        // Assert
        assertEquals("redirect:/director", viewName);
        verify(yearUnitService).saveNewYear(eq(yearUnit), eq(params), eq(session));
    }

    @Test
    void testSaveNewYear_ValidationFailed() {
        // Arrange
        YearUnit yearUnit = new YearUnit();
        Map<String, String> params = new HashMap<>();

        when(directorUnitService.getDirectorById(anyLong()))
                .thenReturn(Optional.of(mockDirector));
        when(yearUnitService.validateNewYearDates(eq(params), eq(model), anyLong()))
                .thenReturn(false);

        // Act
        String viewName = yearUnitController.saveNewYear(yearUnit, params, session, model);

        // Assert
        assertEquals("director_newYear", viewName);
    }

    @Test
    void testEditYearForm_Authorized() {
        // Arrange
        YearUnit yearUnit = new YearUnit();
        when(yearUnitService.getYearUnitById(anyLong()))
                .thenReturn(Optional.of(yearUnit));

        // Act
        String viewName = yearUnitController.editYearForm(1L, session, model);

        // Assert
        assertEquals("director_editYear", viewName);
        verify(model).addAttribute("yearUnit", yearUnit);
    }

    @Test
    void testDeleteYear_Authorized() {
        // Act
        String viewName = yearUnitController.deleteYear(1L, model, session);

        // Assert
        assertEquals("director_confirmRemoveYear", viewName);
        verify(model).addAttribute("warning", "Are you sure you want to remove this year?");
        verify(model).addAttribute("yearId", 1L);
    }

    @Test
    void testConfirmRemoveYear_Authorized() {
        // Act
        String viewName = yearUnitController.confirmRemoveYear(1L, session);

        // Assert
        assertEquals("redirect:/director", viewName);
        verify(yearUnitService).deleteYearUnit(1L);
    }
}