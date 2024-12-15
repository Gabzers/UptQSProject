package com.upt.upt.controller;

import com.upt.upt.entity.*;
import com.upt.upt.service.CurricularUnitService;
import com.upt.upt.service.CoordinatorUnitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CurricularUnitControllerTest {

    @Mock
    private CurricularUnitService curricularUnitService;

    @Mock
    private CoordinatorUnitService coordinatorUnitService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @InjectMocks
    private CurricularUnitController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testShowCourseList_AuthorizedAccess() {
        // Arrange
        Long coordinatorId = 1L;
        UserType userType = UserType.COORDINATOR;
        CoordinatorUnit coordinator = mock(CoordinatorUnit.class);
        DirectorUnit director = mock(DirectorUnit.class);
        YearUnit year = mock(YearUnit.class);
        SemesterUnit firstSemester = mock(SemesterUnit.class);
        SemesterUnit secondSemester = mock(SemesterUnit.class);

        when(session.getAttribute("userId")).thenReturn(coordinatorId);
        when(session.getAttribute("userType")).thenReturn(userType);
        when(coordinatorUnitService.getCoordinatorById(coordinatorId)).thenReturn(Optional.of(coordinator));
        when(coordinator.getDirectorUnit()).thenReturn(director);
        when(director.getCurrentYear()).thenReturn(year);
        when(year.getFirstSemester()).thenReturn(firstSemester);
        when(year.getSecondSemester()).thenReturn(secondSemester);
        when(firstSemester.getCurricularUnits()).thenReturn(List.of());
        when(secondSemester.getCurricularUnits()).thenReturn(List.of());

        // Act
        String viewName = controller.showCourseList(model, session);

        // Assert
        assertEquals("coordinator_index", viewName);
        verify(model).addAttribute("firstSemesterUnits", List.of());
        verify(model).addAttribute("secondSemesterUnits", List.of());
    }

    @Test
    void testShowCourseList_UnauthorizedAccess() {
        // Arrange
        when(session.getAttribute("userType")).thenReturn(UserType.STUDENT);

        // Act
        String viewName = controller.showCourseList(model, session);

        // Assert
        assertEquals("redirect:/login?error=Unauthorized access", viewName);
    }

    @Test
    void testRemoveCurricularUnit() {
        // Arrange
        Long ucId = 1L;
        when(session.getAttribute("userType")).thenReturn(UserType.COORDINATOR);

        // Act
        String viewName = controller.removeCurricularUnit(ucId, model, session);

        // Assert
        assertEquals("coordinator_confirmRemoveUC", viewName);
        verify(model).addAttribute("warning", "Are you sure you want to remove this UC?");
        verify(model).addAttribute("ucId", ucId);
    }

    @Test
    void testRemoveCurricularUnit_UnauthorizedAccess() {
        // Arrange
        when(session.getAttribute("userType")).thenReturn(UserType.STUDENT);

        // Act
        String viewName = controller.removeCurricularUnit(1L, model, session);

        // Assert
        assertEquals("redirect:/login?error=Unauthorized access", viewName);
    }

    @Test
    void testConfirmRemoveCurricularUnit() {
        // Arrange
        Long ucId = 1L;
        when(session.getAttribute("userType")).thenReturn(UserType.COORDINATOR);

        // Act
        String viewName = controller.confirmRemoveCurricularUnit(ucId, session);

        // Assert
        assertEquals("redirect:/coordinator", viewName);
        verify(curricularUnitService).deleteCurricularUnit(ucId);
    }

    @Test
    void testConfirmRemoveCurricularUnit_UnauthorizedAccess() {
        // Arrange
        when(session.getAttribute("userType")).thenReturn(UserType.STUDENT);

        // Act
        String viewName = controller.confirmRemoveCurricularUnit(1L, session);

        // Assert
        assertEquals("redirect:/login?error=Unauthorized access", viewName);
    }

    @Test
    void testSemesterUnitIntegrationInShowCourseList() {
        // Arrange
        Long coordinatorId = 1L;
        UserType userType = UserType.COORDINATOR;
        CoordinatorUnit coordinator = mock(CoordinatorUnit.class);
        DirectorUnit director = mock(DirectorUnit.class);
        YearUnit year = mock(YearUnit.class);

        SemesterUnit firstSemester = new SemesterUnit(1L, "2024-01-01", "2024-06-30", "2024-05-01", "2024-05-15", "2024-07-01", "2024-07-15", new ArrayList<>());
        SemesterUnit secondSemester = new SemesterUnit(2L, "2024-07-01", "2024-12-31", "2024-11-01", "2024-11-15", "2025-01-01", "2025-01-15", new ArrayList<>());

        when(session.getAttribute("userId")).thenReturn(coordinatorId);
        when(session.getAttribute("userType")).thenReturn(userType);
        when(coordinatorUnitService.getCoordinatorById(coordinatorId)).thenReturn(Optional.of(coordinator));
        when(coordinator.getDirectorUnit()).thenReturn(director);
        when(director.getCurrentYear()).thenReturn(year);
        when(year.getFirstSemester()).thenReturn(firstSemester);
        when(year.getSecondSemester()).thenReturn(secondSemester);

        // Act
        String viewName = controller.showCourseList(model, session);

        // Assert
        assertEquals("coordinator_index", viewName);
        verify(model).addAttribute("firstSemesterUnits", firstSemester.getCurricularUnits());
        verify(model).addAttribute("secondSemesterUnits", secondSemester.getCurricularUnits());
    }
}
