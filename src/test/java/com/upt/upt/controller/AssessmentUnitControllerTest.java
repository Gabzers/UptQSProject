package com.upt.upt.controller;

import com.upt.upt.entity.*;
import com.upt.upt.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AssessmentUnitControllerTest {

    @Mock
    private AssessmentUnitService assessmentUnitService;

    @Mock
    private CurricularUnitService curricularUnitService;

    @Mock
    private CoordinatorUnitService coordinatorUnitService;

    @Mock
    private RoomUnitService roomUnitService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @InjectMocks
    private AssessmentUnitController controller;

    private CurricularUnit mockCurricularUnit;
    private CoordinatorUnit mockCoordinator;
    private DirectorUnit mockDirector;
    private YearUnit mockYear;

    @BeforeEach
    public void setUp() {
        // Setup mock objects
        mockYear = new YearUnit();
        mockYear.setId(1L);
        mockYear.setSpecialExamStart("2024-06-01");
        mockYear.setSpecialExamEnd("2024-06-15");

        mockDirector = new DirectorUnit();
        mockDirector.setId(1L);
        mockYear.setDirectorUnit(mockDirector);

        mockCoordinator = new CoordinatorUnit();
        mockCoordinator.setId(1L);
        mockCoordinator.setDirectorUnit(mockDirector);
        mockCurricularUnit = new CurricularUnit();
        mockCurricularUnit.setYear(1);
        mockCurricularUnit.setSemester(1);
        mockCurricularUnit.setStudentsNumber(50);
    }

    @Test
    public void testCreateEvaluationPage_Unauthorized() {
        // Arrange
        when(session.getAttribute("userType")).thenReturn(UserType.STUDENT);

        // Act
        String result = controller.createEvaluationPage(1L, model, null, session);

        // Assert
        assertEquals("redirect:/login?error=Unauthorized access", result);
    }

    @Test
    public void testCreateEvaluationPage_ValidCurricularUnit() {
        // Arrange
        when(session.getAttribute("userType")).thenReturn(UserType.COORDINATOR);
        when(curricularUnitService.getCurricularUnitById(1L)).thenReturn(Optional.of(mockCurricularUnit));
        when(roomUnitService.getAllRooms()).thenReturn(List.of());

        // Act
        String result = controller.createEvaluationPage(1L, model, null, session);

        // Assert
        assertEquals("coordinator_addEvaluations", result);
        verify(model).addAttribute("uc", mockCurricularUnit);
        verify(model).addAttribute("rooms", List.of());
    }

    @Test
    public void testSaveEvaluation_InvalidParameters() {
        // Arrange
        Map<String, String> params = new HashMap<>();
        params.put("curricularUnitId", "1");
        params.put("assessmentExamPeriod", "");

        when(session.getAttribute("userType")).thenReturn(UserType.COORDINATOR);

        // Act
        String result = controller.saveEvaluation(params, session, model);

        // Assert
        assertTrue(result.contains("redirect:/coordinator/coordinator_create_evaluation"));
        verify(model).addAttribute("error", "Exam Period is required.");
    }

    @Test
    public void testSaveEvaluation_ValidParameters() {
        // Arrange
        Map<String, String> params = createValidAssessmentParams();

        when(session.getAttribute("userType")).thenReturn(UserType.COORDINATOR);
        when(session.getAttribute("userId")).thenReturn(1L);
        when(curricularUnitService.getCurricularUnitById(anyLong())).thenReturn(Optional.of(mockCurricularUnit));
        when(coordinatorUnitService.getCoordinatorById(1L)).thenReturn(Optional.of(mockCoordinator));
        when(roomUnitService.getAvailableRooms(anyInt(), anyBoolean(), any(), any())).thenReturn(List.of(new RoomUnit()));
        when(assessmentUnitService.calculatePeriodTotalWeight(any(), anyString(), anyInt())).thenReturn(50);
        when(assessmentUnitService.validateAssessmentDates(any(), any(), any(), any(), any(), any(), anyLong())).thenReturn(true);

        // Act
        String result = controller.saveEvaluation(params, session, model);

        // Assert
        assertEquals("redirect:/coordinator/coordinator_evaluationsUC?id=1", result);
        verify(assessmentUnitService).saveAssessment(any(), anyList());
    }

    @Test
    public void testGetValidDateRanges() {
        // Arrange
        when(session.getAttribute("userType")).thenReturn(UserType.COORDINATOR);
        when(coordinatorUnitService.getCoordinatorById(anyLong())).thenReturn(Optional.of(mockCoordinator));
        when(curricularUnitService.getCurricularUnitById(anyLong())).thenReturn(Optional.of(mockCurricularUnit));

        Map<String, String> mockRanges = new HashMap<>();
        mockRanges.put("start", "2024-01-01");
        mockRanges.put("end", "2024-01-31");
        when(assessmentUnitService.getValidDateRanges(anyString(), any(), any())).thenReturn(mockRanges);

        // Act
        Map<String, String> result = controller.getValidDateRanges("Normal", 1L, session);

        // Assert
        assertNotNull(result);
        assertEquals("2024-01-01", result.get("start"));
        assertEquals("2024-01-31", result.get("end"));
    }

    // Helper method to create valid assessment parameters
    private Map<String, String> createValidAssessmentParams() {
        Map<String, String> params = new HashMap<>();
        params.put("curricularUnitId", "1");
        params.put("assessmentExamPeriod", "Normal");
        params.put("assessmentType", "Exam");
        params.put("assessmentWeight", "50");
        params.put("assessmentMinimumGrade", "10.0");
        params.put("assessmentStartTime", LocalDateTime.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
        params.put("assessmentEndTime", LocalDateTime.now().plusDays(1).plusHours(2).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
        return params;
    }
}