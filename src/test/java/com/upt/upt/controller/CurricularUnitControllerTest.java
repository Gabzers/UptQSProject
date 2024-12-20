package com.upt.upt.controller;

import com.upt.upt.entity.CoordinatorUnit;
import com.upt.upt.entity.DirectorUnit;
import com.upt.upt.entity.SemesterUnit;
import com.upt.upt.entity.UserType;
import com.upt.upt.entity.YearUnit;
import com.upt.upt.service.CoordinatorUnitService;
import com.upt.upt.service.CurricularUnitService;
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

/**
 * Test class for CurricularUnitController.
 * Provides unit tests for the controller's endpoints.
 * 
 * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
 */
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

    /**
     * Sets up the test environment before each test.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests showing the course list with authorized access.
     */
    @Test
    void testShowCourseList_AuthorizedAccess() {
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

        String viewName = controller.showCourseList(model, session);

        assertEquals("coordinator_index", viewName);
        verify(model).addAttribute("firstSemesterUnits", List.of());
        verify(model).addAttribute("secondSemesterUnits", List.of());
    }

    /**
     * Tests showing the course list with unauthorized access.
     */
    @Test
    void testShowCourseList_UnauthorizedAccess() {
        when(session.getAttribute("userType")).thenReturn(UserType.STUDENT);

        String viewName = controller.showCourseList(model, session);

        assertEquals("redirect:/login?error=Unauthorized access", viewName);
    }

    /**
     * Tests removing a curricular unit.
     */
    @Test
    void testRemoveCurricularUnit() {
        Long ucId = 1L;
        when(session.getAttribute("userType")).thenReturn(UserType.COORDINATOR);

        String viewName = controller.removeCurricularUnit(ucId, model, session);

        assertEquals("coordinator_confirmRemoveUC", viewName);
        verify(model).addAttribute("warning", "Are you sure you want to remove this UC?");
        verify(model).addAttribute("ucId", ucId);
    }

    /**
     * Tests removing a curricular unit with unauthorized access.
     */
    @Test
    void testRemoveCurricularUnit_UnauthorizedAccess() {
        when(session.getAttribute("userType")).thenReturn(UserType.STUDENT);

        String viewName = controller.removeCurricularUnit(1L, model, session);

        assertEquals("redirect:/login?error=Unauthorized access", viewName);
    }

    /**
     * Tests confirming the removal of a curricular unit.
     */
    @Test
    void testConfirmRemoveCurricularUnit() {
        Long ucId = 1L;
        when(session.getAttribute("userType")).thenReturn(UserType.COORDINATOR);

        String viewName = controller.confirmRemoveCurricularUnit(ucId, session);

        assertEquals("redirect:/coordinator", viewName);
        verify(curricularUnitService).deleteCurricularUnit(ucId);
    }

    /**
     * Tests confirming the removal of a curricular unit with unauthorized access.
     */
    @Test
    void testConfirmRemoveCurricularUnit_UnauthorizedAccess() {
        when(session.getAttribute("userType")).thenReturn(UserType.STUDENT);

        String viewName = controller.confirmRemoveCurricularUnit(1L, session);

        assertEquals("redirect:/login?error=Unauthorized access", viewName);
    }

    /**
     * Tests semester unit integration in showing the course list.
     */
    @Test
    void testSemesterUnitIntegrationInShowCourseList() {
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

        String viewName = controller.showCourseList(model, session);

        assertEquals("coordinator_index", viewName);
        verify(model).addAttribute("firstSemesterUnits", firstSemester.getCurricularUnits());
        verify(model).addAttribute("secondSemesterUnits", secondSemester.getCurricularUnits());
    }
}
