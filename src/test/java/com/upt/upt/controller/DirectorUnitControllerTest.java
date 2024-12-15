package com.upt.upt.controller;

import com.upt.upt.entity.*;
import com.upt.upt.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DirectorUnitController.class)
class DirectorUnitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private DirectorUnitService directorUnitService;

    @Mock
    private CoordinatorUnitService coordinatorService;

    @Mock
    private YearUnitService yearUnitService;

    @Mock
    private UserService userService;

    @Mock
    private PdfService pdfService;

    @InjectMocks
    private DirectorUnitController directorUnitController;

    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(directorUnitController).build();
        session = new MockHttpSession();
    }

    @Test
    void testListDirectors_WhenDirectorIsLoggedIn() throws Exception {
        // Mock session and service
        session.setAttribute("userType", UserType.DIRECTOR);
        session.setAttribute("userId", 1L);

        DirectorUnit director = new DirectorUnit();
        director.setId(1L);
        director.setName("Test Director");

        when(directorUnitService.getDirectorById(1L)).thenReturn(Optional.of(director));

        mockMvc.perform(get("/director").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("director_index"))
                .andExpect(model().attributeExists("loggedInDirector"))
                .andExpect(model().attribute("loggedInDirector", director));

        verify(directorUnitService, times(1)).getDirectorById(1L);
    }

    @Test
    void testListDirectors_WhenUnauthorized() throws Exception {
        // Mock session without DIRECTOR userType
        session.setAttribute("userType", UserType.COORDINATOR);

        mockMvc.perform(get("/director").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error=Unauthorized access"));
    }

    @Test
    void testViewSemesters_WhenYearExists() throws Exception {
        // Mock session and services
        session.setAttribute("userType", UserType.DIRECTOR);

        YearUnit year = new YearUnit();
        year.setId(1L);
        year.setFirstSemester(new SemesterUnit());
        year.setSecondSemester(new SemesterUnit());

        when(yearUnitService.getYearUnitById(1L)).thenReturn(Optional.of(year));

        mockMvc.perform(get("/director/viewSemester/1").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("director_viewSemester"))
                .andExpect(model().attributeExists("yearUnit"))
                .andExpect(model().attribute("yearUnit", year));

        verify(yearUnitService, times(1)).getYearUnitById(1L);
    }

    @Test
    void testViewSemesters_WhenYearDoesNotExist() throws Exception {
        // Mock session and service
        session.setAttribute("userType", UserType.DIRECTOR);

        when(yearUnitService.getYearUnitById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/director/viewSemester/1").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/director?error=Year not found"));

        verify(yearUnitService, times(1)).getYearUnitById(1L);
    }

    @Test
    void testShowEditCoordinatorForm_WhenCoordinatorExists() throws Exception {
        // Mock session and services
        session.setAttribute("userType", UserType.DIRECTOR);

        CoordinatorUnit coordinator = new CoordinatorUnit();
        coordinator.setId(1L);
        coordinator.setName("Test Coordinator");

        when(coordinatorService.getCoordinatorById(1L)).thenReturn(Optional.of(coordinator));

        mockMvc.perform(get("/director/edit-coordinator").param("id", "1").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("director_editCoordinator"))
                .andExpect(model().attributeExists("coordinator"))
                .andExpect(model().attribute("coordinator", coordinator));

        verify(coordinatorService, times(1)).getCoordinatorById(1L);
    }

    @Test
    void testShowEditCoordinatorForm_WhenCoordinatorDoesNotExist() throws Exception {
        // Mock session and service
        session.setAttribute("userType", UserType.DIRECTOR);

        when(coordinatorService.getCoordinatorById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/director/edit-coordinator").param("id", "1").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/director?error=Coordinator not found"));

        verify(coordinatorService, times(1)).getCoordinatorById(1L);
    }
}
