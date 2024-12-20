package com.upt.upt.controller;

import com.upt.upt.entity.AssessmentUnit;
import com.upt.upt.entity.CoordinatorUnit;
import com.upt.upt.entity.CurricularUnit;
import com.upt.upt.entity.DirectorUnit;
import com.upt.upt.entity.SemesterUnit;
import com.upt.upt.entity.UserType;
import com.upt.upt.entity.YearUnit;
import com.upt.upt.service.AssessmentUnitService;
import com.upt.upt.service.CoordinatorUnitService;
import com.upt.upt.service.PdfService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for MapUnitController.
 * Provides unit tests for the controller's endpoints.
 * 
 * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
 */
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

    /**
     * Sets up the test environment before each test.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(mapUnitController).build();
        session = new MockHttpSession();
    }

    /**
     * Tests unauthorized access to the assessment map.
     */
    @Test
    public void testShowAssessmentMapUnauthorized() throws Exception {
        mockMvc.perform(get("/coordinator/map")
                        .param("semester", "1")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error=Unauthorized access"));
    }

    /**
     * Tests access to the assessment map when the coordinator is not found.
     */
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

    /**
     * Tests access to the assessment map when the current year is not found.
     */
    @Test
    public void testShowAssessmentMapCurrentYearNotFound() throws Exception {
        session.setAttribute("userType", UserType.COORDINATOR);
        session.setAttribute("userId", 1L);

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

    /**
     * Tests unauthorized access to generate PDF.
     */
    @Test
    public void testGeneratePdfUnauthorized() throws Exception {
        mockMvc.perform(get("/coordinator/map/pdf")
                        .param("year", "2021")
                        .param("semester", "1")
                        .session(session))
                .andExpect(status().isUnauthorized());
    }

    /**
     * Tests generating PDF when the coordinator is not found.
     */
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

    /**
     * Tests generating PDF when there is no content.
     */
    @Test
    public void testGeneratePdfNoContent() throws Exception {
        session.setAttribute("userType", UserType.COORDINATOR);
        session.setAttribute("userId", 1L);

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

    /**
     * Tests generating PDF with valid data.
     */
    @Test
    public void testGeneratePdf() throws Exception {
        session.setAttribute("userType", UserType.COORDINATOR);
        session.setAttribute("userId", 1L);

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