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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCoordinatorForm_AuthorizedDirector() {
        // Mocking session attributes
        when(session.getAttribute("userType")).thenReturn(UserType.DIRECTOR);
        when(session.getAttribute("userId")).thenReturn(1L);
        when(directorUnitService.getDirectorById(1L)).thenReturn(Optional.of(new DirectorUnit()));

        // Calling the method
        String viewName = controller.createCoordinatorForm(session, model);

        // Verifying interactions and assertions
        verify(model, times(1)).addAttribute(eq("coordinator"), any(CoordinatorUnit.class));
        assertEquals("director_addCoordinator", viewName);
    }

    @Test
    void testCreateCoordinatorForm_UnauthorizedAccess() {
        // Mocking session attributes
        when(session.getAttribute("userType")).thenReturn(UserType.COORDINATOR);

        // Calling the method
        String viewName = controller.createCoordinatorForm(session, model);

        // Assertions
        assertEquals("redirect:/login?error=Unauthorized access", viewName);
    }

    @Test
    void testSaveCoordinator_Success() {
        // Mocking session attributes
        when(session.getAttribute("userType")).thenReturn(UserType.DIRECTOR);
        when(session.getAttribute("userId")).thenReturn(1L);
        when(directorUnitService.getDirectorById(1L)).thenReturn(Optional.of(new DirectorUnit()));
        when(coordinatorService.hasYearCreated(1L)).thenReturn(true);
        when(userService.usernameExists("username")).thenReturn(false);

        CoordinatorUnit coordinator = new CoordinatorUnit();
        coordinator.setDuration(5);
        coordinator.setUsername("username");

        // Calling the method
        String viewName = controller.saveCoordinator(coordinator, session);

        // Verifying interactions and assertions
        verify(coordinatorService, times(1)).saveCoordinatorWithDirector(eq(coordinator), eq(session));
        assertEquals("redirect:/director", viewName);
    }

    @Test
    void testSaveCoordinator_UnauthorizedAccess() {
        // Mocking session attributes
        when(session.getAttribute("userType")).thenReturn(UserType.COORDINATOR);

        CoordinatorUnit coordinator = new CoordinatorUnit();

        // Calling the method
        String viewName = controller.saveCoordinator(coordinator, session);

        // Assertions
        assertEquals("redirect:/login?error=Unauthorized access", viewName);
    }

    @Test
    void testSaveCoordinator_DurationExceedsLimit() {
        // Mocking session attributes
        when(session.getAttribute("userType")).thenReturn(UserType.DIRECTOR);
        when(session.getAttribute("userId")).thenReturn(1L);
        when(directorUnitService.getDirectorById(1L)).thenReturn(Optional.of(new DirectorUnit()));

        CoordinatorUnit coordinator = new CoordinatorUnit();
        coordinator.setDuration(15);

        // Calling the method
        String viewName = controller.saveCoordinator(coordinator, session);

        // Assertions
        assertEquals("redirect:/director/create-coordinator?error=Course duration cannot exceed 10 years", viewName);
    }

    @Test
    void testSaveCoordinator_UsernameAlreadyExists() {
        // Mocking session attributes
        when(session.getAttribute("userType")).thenReturn(UserType.DIRECTOR);
        when(session.getAttribute("userId")).thenReturn(1L);
        when(directorUnitService.getDirectorById(1L)).thenReturn(Optional.of(new DirectorUnit()));
        when(coordinatorService.hasYearCreated(1L)).thenReturn(true);
        when(userService.usernameExists("username")).thenReturn(true);

        CoordinatorUnit coordinator = new CoordinatorUnit();
        coordinator.setDuration(5);
        coordinator.setUsername("username");

        // Calling the method
        String viewName = controller.saveCoordinator(coordinator, session);

        // Assertions
        assertEquals("redirect:/director/create-coordinator?error=Username already exists", viewName);
    }
}
