package com.upt.upt.controller;

import com.upt.upt.entity.*;
import com.upt.upt.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class MapUnitControllerTest {

    @Mock
    private CoordinatorUnitService coordinatorUnitService;

    @Mock
    private AssessmentUnitService assessmentService;

    @Mock
    private PdfService pdfService;

    @InjectMocks
    private MapUnitController mapUnitController;

    private MockMvc mockMvc;

    private MockHttpSession session;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(mapUnitController).build();
        session = new MockHttpSession();
    }

    @Test
    public void testShowAssessmentMapUnauthorized() throws Exception {
        mockMvc.perform(get("/coordinator/map")
                        .param("semester", "1")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error=Unauthorized access"));
    }

    @Test
    public void testShowAssessmentMapCoordinatorNotFound() throws Exception {
        session.setAttribute("userType", UserType.COORDINATOR);
        when(coordinatorUnitService.getCoordinatorById(any(Long.class))).thenReturn(Optional.empty());

        mockMvc.perform(get("/coordinator/map")
                        .param("semester", "1")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error=Coordinator not found"));
    }

    @Test
    public void testShowAssessmentMapCurrentYearNotFound() throws Exception {
        session.setAttribute("userType", UserType.COORDINATOR);
        session.setAttribute("userId", 1L);

        // Create a DirectorUnit with an empty list of academic years
        DirectorUnit directorUnit = new DirectorUnit();
        directorUnit.setAcademicYears(new ArrayList<>());

        CoordinatorUnit coordinator = new CoordinatorUnit();
        coordinator.setDirectorUnit(directorUnit);

        when(coordinatorUnitService.getCoordinatorById(1L)).thenReturn(Optional.of(coordinator));

        mockMvc.perform(get("/coordinator/map")
                        .param("semester", "1")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error=Current year not found"));
    }

    @Test
    public void testGeneratePdfUnauthorized() throws Exception {
        mockMvc.perform(get("/coordinator/map/pdf")
                        .param("year", "2021")
                        .param("semester", "1")
                        .session(session))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testGeneratePdfCoordinatorNotFound() throws Exception {
        session.setAttribute("userType", UserType.COORDINATOR);
        when(coordinatorUnitService.getCoordinatorById(any(Long.class))).thenReturn(Optional.empty());

        mockMvc.perform(get("/coordinator/map/pdf")
                        .param("year", "2021")
                        .param("semester", "1")
                        .session(session))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testGeneratePdfNoContent() throws Exception {
        session.setAttribute("userType", UserType.COORDINATOR);
        session.setAttribute("userId", 1L);

        // Create a DirectorUnit with a current year
        DirectorUnit directorUnit = new DirectorUnit();
        YearUnit currentYear = new YearUnit();
        directorUnit.addAcademicYear(currentYear);

        SemesterUnit semesterUnit = new SemesterUnit();
        semesterUnit.setCurricularUnits(Collections.emptyList());
        currentYear.setFirstSemester(semesterUnit);
        currentYear.setSecondSemester(semesterUnit);

        CoordinatorUnit coordinator = new CoordinatorUnit();
        coordinator.setDirectorUnit(directorUnit);

        when(coordinatorUnitService.getCoordinatorById(1L)).thenReturn(Optional.of(coordinator));

        mockMvc.perform(get("/coordinator/map/pdf")
                        .param("year", "2021")
                        .param("semester", "1")
                        .session(session))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGeneratePdf() throws Exception {
        session.setAttribute("userType", UserType.COORDINATOR);
        session.setAttribute("userId", 1L);

        // Create a DirectorUnit with a current year
        DirectorUnit directorUnit = new DirectorUnit();
        YearUnit currentYear = new YearUnit();
        directorUnit.addAcademicYear(currentYear);

        SemesterUnit semesterUnit = new SemesterUnit();
        CurricularUnit curricularUnit = new CurricularUnit();
        curricularUnit.setYear(2021);

        AssessmentUnit assessment = new AssessmentUnit();
        assessment.setStartTime(LocalDateTime.of(2021, 1, 1, 10, 0));
        assessment.setCurricularUnit(curricularUnit);
        curricularUnit.setAssessments(Collections.singletonList(assessment));

        semesterUnit.setCurricularUnits(Collections.singletonList(curricularUnit));
        currentYear.setFirstSemester(semesterUnit);
        currentYear.setSecondSemester(semesterUnit);

        CoordinatorUnit coordinator = new CoordinatorUnit();
        coordinator.setDirectorUnit(directorUnit);

        when(coordinatorUnitService.getCoordinatorById(1L)).thenReturn(Optional.of(coordinator));
        when(pdfService.generatePdfForYearAndSemester(any(CoordinatorUnit.class), eq(2021), eq(1)))
                .thenReturn(new byte[0]);

        mockMvc.perform(get("/coordinator/map/pdf")
                        .param("year", "2021")
                        .param("semester", "1")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF));
    }
}