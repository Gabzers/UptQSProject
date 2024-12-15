package com.upt.upt.controller;

import com.upt.upt.entity.*;
import com.upt.upt.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DirectorUnitControllerTest {

    @InjectMocks
    private DirectorUnitController directorUnitController;

    @Mock
    private DirectorUnitService directorUnitService;

    @Mock
    private CoordinatorUnitService coordinatorUnitService;

    @Mock
    private YearUnitService yearUnitService;

    @Mock
    private UserService userService;

    @Mock
    private PdfService pdfService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListDirectors_ValidSession() {
        when(session.getAttribute("userType")).thenReturn(UserType.DIRECTOR);
        DirectorUnit director = new DirectorUnit();
        when(session.getAttribute("userId")).thenReturn(1L);
        when(directorUnitService.getDirectorById(1L)).thenReturn(Optional.of(director));

        String viewName = directorUnitController.listDirectors(session, model);

        assertEquals("director_index", viewName);
        verify(model).addAttribute("loggedInDirector", director);
    }

    @Test
    void testListDirectors_InvalidSession() {
        when(session.getAttribute("userType")).thenReturn(null);

        String viewName = directorUnitController.listDirectors(session, model);

        assertEquals("redirect:/login?error=Unauthorized access", viewName);
    }

    @Test
    void testViewSemesters_ValidYear() {
        when(session.getAttribute("userType")).thenReturn(UserType.DIRECTOR);
        YearUnit yearUnit = new YearUnit();
        when(yearUnitService.getYearUnitById(1L)).thenReturn(Optional.of(yearUnit));

        String viewName = directorUnitController.viewSemesters(1L, model, session);

        assertEquals("director_viewSemester", viewName);
        verify(model).addAttribute("yearUnit", yearUnit);
    }

    @Test
    void testViewSemesters_InvalidYear() {
        when(session.getAttribute("userType")).thenReturn(UserType.DIRECTOR);
        when(yearUnitService.getYearUnitById(1L)).thenReturn(Optional.empty());

        String viewName = directorUnitController.viewSemesters(1L, model, session);

        assertEquals("redirect:/director?error=Year not found", viewName);
    }

    @Test
    void testGeneratePdf_ValidRequest() {
        when(session.getAttribute("userType")).thenReturn(UserType.DIRECTOR);
        DirectorUnit director = new DirectorUnit();
        when(session.getAttribute("userId")).thenReturn(1L);
        when(directorUnitService.getDirectorById(1L)).thenReturn(Optional.of(director));
        YearUnit yearUnit = new YearUnit();
        when(yearUnitService.getYearUnitById(1L)).thenReturn(Optional.of(yearUnit));

        byte[] pdfContent = new byte[10];
        when(pdfService.generatePdfForYearAndSemester(director, yearUnit, 1)).thenReturn(pdfContent);

        var response = directorUnitController.generatePdf(1L, 1, session);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertArrayEquals(pdfContent, response.getBody());
    }

    @Test
    void testGeneratePdf_UnauthorizedAccess() {
        when(session.getAttribute("userType")).thenReturn(null);

        var response = directorUnitController.generatePdf(1L, 1, session);

        assertNotNull(response);
        assertEquals(401, response.getStatusCodeValue());
    }
}
