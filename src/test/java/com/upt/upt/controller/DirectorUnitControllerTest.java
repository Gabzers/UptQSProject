package com.upt.upt.controller;

import com.upt.upt.entity.*;
import com.upt.upt.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.mock.web.MockHttpSession;

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
    private CoordinatorUnitService coordinatorService;

    @Mock
    private YearUnitService yearUnitService;

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        session = new MockHttpSession();
    }

    @Test
    void testListDirectors_WhenNotDirector_ShouldRedirectToLogin() {
        session.setAttribute("userType", UserType.COORDINATOR);
        String result = directorUnitController.listDirectors(session, model);
        assertEquals("redirect:/login?error=Unauthorized access", result);
    }

    @Test
    void testListDirectors_WhenDirectorAndSessionValid_ShouldReturnDirectorIndex() {
        session.setAttribute("userType", UserType.DIRECTOR);
        session.setAttribute("userId", 1L);

        DirectorUnit mockDirector = mock(DirectorUnit.class);
        when(directorUnitService.getDirectorById(1L)).thenReturn(Optional.of(mockDirector));
        when(mockDirector.getCoordinators()).thenReturn(List.of());
        when(mockDirector.getAcademicYears()).thenReturn(List.of());

        String result = directorUnitController.listDirectors(session, model);

        verify(model).addAttribute("loggedInDirector", mockDirector);
        verify(model).addAttribute("coordinators", List.of());
        verify(model).addAttribute("currentYear", null);
        verify(model).addAttribute("pastYears", List.of());
        assertEquals("director_index", result);
    }

    @Test
    void testViewSemesters_WhenYearExists_ShouldReturnViewSemesterTemplate() {
        session.setAttribute("userType", UserType.DIRECTOR);
        YearUnit mockYearUnit = mock(YearUnit.class);
        when(yearUnitService.getYearUnitById(1L)).thenReturn(Optional.of(mockYearUnit));

        String result = directorUnitController.viewSemesters(1L, model, session);

        verify(model).addAttribute("yearUnit", mockYearUnit);
        verify(model).addAttribute("firstSemester", mockYearUnit.getFirstSemester());
        verify(model).addAttribute("secondSemester", mockYearUnit.getSecondSemester());
        assertEquals("director_viewSemester", result);
    }

    @Test
    void testViewSemesters_WhenYearNotFound_ShouldRedirectToDirector() {
        session.setAttribute("userType", UserType.DIRECTOR);
        when(yearUnitService.getYearUnitById(1L)).thenReturn(Optional.empty());

        String result = directorUnitController.viewSemesters(1L, model, session);

        assertEquals("redirect:/director?error=Year not found", result);
    }

    @Test
    void testUpdateCoordinator_WhenCoordinatorExists_ShouldUpdateAndRedirectToDirector() {
        session.setAttribute("userType", UserType.DIRECTOR);
        session.setAttribute("userId", 1L);

        CoordinatorUnit mockCoordinator = mock(CoordinatorUnit.class);
        DirectorUnit mockDirector = mock(DirectorUnit.class);

        when(directorUnitService.getDirectorById(1L)).thenReturn(Optional.of(mockDirector));
        when(coordinatorService.getCoordinatorById(1L)).thenReturn(Optional.of(mockCoordinator));

        Map<String, String> params = Map.of(
                "name", "Updated Name",
                "course", "Updated Course",
                "duration", "4",
                "username", "updated_username",
                "password", "new_password"
        );

        String result = directorUnitController.updateCoordinator(1L, params, session);

        verify(mockCoordinator).setName("Updated Name");
        verify(mockCoordinator).setCourse("Updated Course");
        verify(mockCoordinator).setDuration(4);
        verify(mockCoordinator).setUsername("updated_username");
        verify(mockCoordinator).setPassword("new_password");
        verify(coordinatorService).saveCoordinator(mockCoordinator);
        assertEquals("redirect:/director", result);
    }

    @Test
    void testUpdateCoordinator_WhenCoordinatorNotFound_ShouldRedirectToError() {
        session.setAttribute("userType", UserType.DIRECTOR);
        session.setAttribute("userId", 1L);

        when(directorUnitService.getDirectorById(1L)).thenReturn(Optional.of(mock(DirectorUnit.class)));
        when(coordinatorService.getCoordinatorById(1L)).thenReturn(Optional.empty());

        Map<String, String> params = Map.of("name", "New Name");

        String result = directorUnitController.updateCoordinator(1L, params, session);

        assertEquals("redirect:/director/edit-coordinator?id=1&error=true", result);
    }

    @Test
    void testUpdateCoordinator_WhenParamsMissing_ShouldHandleGracefully() {
        session.setAttribute("userType", UserType.DIRECTOR);
        session.setAttribute("userId", 1L);

        when(directorUnitService.getDirectorById(1L)).thenReturn(Optional.of(mock(DirectorUnit.class)));
        when(coordinatorService.getCoordinatorById(1L)).thenReturn(Optional.of(mock(CoordinatorUnit.class)));

        Map<String, String> params = Map.of(); // Parâmetros vazios
        String result = directorUnitController.updateCoordinator(1L, params, session);

        assertEquals("redirect:/director/edit-coordinator?id=1&error=true", result);
    }

}
