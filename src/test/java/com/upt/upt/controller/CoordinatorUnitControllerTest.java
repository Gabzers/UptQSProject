package com.upt.upt.controller;

import com.upt.upt.entity.CoordinatorUnit;
import com.upt.upt.entity.DirectorUnit;
import com.upt.upt.entity.UserType;
import com.upt.upt.service.CoordinatorUnitService;
import com.upt.upt.service.DirectorUnitService;
import com.upt.upt.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Test class for CoordinatorUnitController.
 * Provides unit tests for the controller's endpoints.
 * 
 * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
 */
class CoordinatorUnitControllerTest {

    @InjectMocks
    private CoordinatorUnitController controller;

    @Mock
    private CoordinatorUnitService coordinatorService;

    @Mock
    private DirectorUnitService directorUnitService;

    @Mock
    private UserService userService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    /**
     * Sets up the test environment before each test.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests creating a coordinator form with authorized director access.
     */
    @Test
    void testCreateCoordinatorForm_AuthorizedDirector() {
        when(session.getAttribute("userType")).thenReturn(UserType.DIRECTOR);
        when(session.getAttribute("userId")).thenReturn(1L);
        when(directorUnitService.getDirectorById(1L)).thenReturn(Optional.of(new DirectorUnit()));

        String viewName = controller.createCoordinatorForm(session, model);

        verify(model, times(1)).addAttribute(eq("coordinator"), any(CoordinatorUnit.class));
        assertEquals("director_addCoordinator", viewName);
    }

    /**
     * Tests creating a coordinator form with unauthorized access.
     */
    @Test
    void testCreateCoordinatorForm_UnauthorizedAccess() {
        when(session.getAttribute("userType")).thenReturn(UserType.COORDINATOR);

        String viewName = controller.createCoordinatorForm(session, model);

        assertEquals("redirect:/login?error=Unauthorized access", viewName);
    }

    /**
     * Tests saving a coordinator with a successful request.
     */
    @Test
    void testSaveCoordinator_Success() {
        when(session.getAttribute("userType")).thenReturn(UserType.DIRECTOR);
        when(session.getAttribute("userId")).thenReturn(1L);
        when(directorUnitService.getDirectorById(1L)).thenReturn(Optional.of(new DirectorUnit()));
        when(coordinatorService.hasYearCreated(1L)).thenReturn(true);
        when(userService.usernameExists("username")).thenReturn(false);

        CoordinatorUnit coordinator = new CoordinatorUnit();
        coordinator.setDuration(5);
        coordinator.setUsername("username");

        String viewName = controller.saveCoordinator(coordinator, session);

        verify(coordinatorService, times(1)).saveCoordinatorWithDirector(eq(coordinator), eq(session));
        assertEquals("redirect:/director", viewName);
    }

    /**
     * Tests saving a coordinator with unauthorized access.
     */
    @Test
    void testSaveCoordinator_UnauthorizedAccess() {
        when(session.getAttribute("userType")).thenReturn(UserType.COORDINATOR);

        CoordinatorUnit coordinator = new CoordinatorUnit();

        String viewName = controller.saveCoordinator(coordinator, session);

        assertEquals("redirect:/login?error=Unauthorized access", viewName);
    }

    /**
     * Tests saving a coordinator when the duration exceeds the limit.
     */
    @Test
    void testSaveCoordinator_DurationExceedsLimit() {
        when(session.getAttribute("userType")).thenReturn(UserType.DIRECTOR);
        when(session.getAttribute("userId")).thenReturn(1L);
        when(directorUnitService.getDirectorById(1L)).thenReturn(Optional.of(new DirectorUnit()));

        CoordinatorUnit coordinator = new CoordinatorUnit();
        coordinator.setDuration(15);

        String viewName = controller.saveCoordinator(coordinator, session);

        assertEquals("redirect:/director/create-coordinator?error=Course duration cannot exceed 10 years", viewName);
    }

    /**
     * Tests saving a coordinator when the username already exists.
     */
    @Test
    void testSaveCoordinator_UsernameAlreadyExists() {
        when(session.getAttribute("userType")).thenReturn(UserType.DIRECTOR);
        when(session.getAttribute("userId")).thenReturn(1L);
        when(directorUnitService.getDirectorById(1L)).thenReturn(Optional.of(new DirectorUnit()));
        when(coordinatorService.hasYearCreated(1L)).thenReturn(true);
        when(userService.usernameExists("username")).thenReturn(true);

        CoordinatorUnit coordinator = new CoordinatorUnit();
        coordinator.setDuration(5);
        coordinator.setUsername("username");

        String viewName = controller.saveCoordinator(coordinator, session);

        assertEquals("redirect:/director/create-coordinator?error=Username already exists", viewName);
    }
}
