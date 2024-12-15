package com.upt.upt.controller;

import com.upt.upt.entity.CoordinatorUnit;
import com.upt.upt.entity.CurricularUnit;
import com.upt.upt.entity.DirectorUnit;
import com.upt.upt.entity.UserType;
import com.upt.upt.entity.YearUnit;
import com.upt.upt.service.CurricularUnitService;
import com.upt.upt.service.CoordinatorUnitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CurricularUnitControllerTest {

    @Mock
    private CurricularUnitService curricularUnitService;

    @Mock
    private CoordinatorUnitService coordinatorUnitService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @InjectMocks
    private CurricularUnitController curricularUnitController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testShowCourseList_WithValidCoordinator() {
        // Arrange
        Long coordinatorId = 1L;
        when(session.getAttribute("userId")).thenReturn(coordinatorId);
        when(session.getAttribute("userType")).thenReturn(UserType.COORDINATOR);

        CoordinatorUnit coordinator = mock(CoordinatorUnit.class);
        DirectorUnit director = mock(DirectorUnit.class);
        YearUnit currentYear = mock(YearUnit.class);

        when(coordinatorUnitService.getCoordinatorById(coordinatorId)).thenReturn(Optional.of(coordinator));
        when(coordinator.getDirectorUnit()).thenReturn(director);
        when(director.getCurrentYear()).thenReturn(currentYear);

        // Mock curricular units
        List<CurricularUnit> firstSemesterUnits = List.of(mock(CurricularUnit.class));
        List<CurricularUnit> secondSemesterUnits = List.of(mock(CurricularUnit.class));

        when(currentYear.getFirstSemester().getCurricularUnits()).thenReturn(firstSemesterUnits);
        when(currentYear.getSecondSemester().getCurricularUnits()).thenReturn(secondSemesterUnits);

        // Act
        String viewName = curricularUnitController.showCourseList(model, session);

        // Assert
        assertEquals("coordinator_index", viewName);
        verify(model).addAttribute(eq("firstSemesterUnits"), any());
        verify(model).addAttribute(eq("secondSemesterUnits"), any());
    }

    @Test
    void testShowCourseList_UnauthorizedAccess() {
        // Arrange
        when(session.getAttribute("userType")).thenReturn(UserType.STUDENT);

        // Act
        String viewName = curricularUnitController.showCourseList(model, session);

        // Assert
        assertEquals("redirect:/login?error=Unauthorized access", viewName);
    }

    @Test
    void testRemoveCurricularUnit_WithValidCoordinator() {
        // Arrange
        Long ucId = 1L;
        when(session.getAttribute("userType")).thenReturn(UserType.COORDINATOR);

        // Act
        String viewName = curricularUnitController.removeCurricularUnit(ucId, model, session);

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
        String viewName = curricularUnitController.removeCurricularUnit(1L, model, session);

        // Assert
        assertEquals("redirect:/login?error=Unauthorized access", viewName);
    }

    @Test
    void testConfirmRemoveCurricularUnit_WithValidCoordinator() {
        // Arrange
        Long ucId = 1L;
        when(session.getAttribute("userType")).thenReturn(UserType.COORDINATOR);

        // Act
        String viewName = curricularUnitController.confirmRemoveCurricularUnit(ucId, session);

        // Assert
        assertEquals("redirect:/coordinator", viewName);
        verify(curricularUnitService).deleteCurricularUnit(ucId);
    }

    @Test
    void testConfirmRemoveCurricularUnit_UnauthorizedAccess() {
        // Arrange
        when(session.getAttribute("userType")).thenReturn(UserType.STUDENT);

        // Act
        String viewName = curricularUnitController.confirmRemoveCurricularUnit(1L, session);

        // Assert
        assertEquals("redirect:/login?error=Unauthorized access", viewName);
    }
}
