package com.upt.upt.controller;

import com.upt.upt.entity.RoomUnit;
import com.upt.upt.entity.AssessmentUnit;
import com.upt.upt.entity.UserType;
import com.upt.upt.service.RoomUnitService;
import com.upt.upt.service.AssessmentUnitService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.mock.web.MockHttpSession;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoomUnitControllerTest {

    @Mock
    private RoomUnitService roomUnitService;

    @Mock
    private AssessmentUnitService assessmentUnitService;

    @Mock
    private Model model;

    @InjectMocks
    private RoomUnitController roomUnitController;

    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        session = new MockHttpSession();
    }

    @Test
    void testListRooms_UnauthorizedAccess() {
        String viewName = roomUnitController.listRooms(model, session);
        assertEquals("redirect:/login?error=Unauthorized access", viewName);
    }

    @Test
    void testListRooms_AuthorizedAccess() {
        session.setAttribute("userType", UserType.MASTER);
        List<RoomUnit> roomUnits = Arrays.asList(new RoomUnit());
        when(roomUnitService.getAllRooms()).thenReturn(roomUnits);

        String viewName = roomUnitController.listRooms(model, session);
        assertEquals("master_index", viewName);
        verify(model).addAttribute("roomUnits", roomUnits);
    }

    @Test
    void testShowCreateRoomForm_UnauthorizedAccess() {
        String viewName = roomUnitController.showCreateRoomForm(session);
        assertEquals("redirect:/login?error=Unauthorized access", viewName);
    }

    @Test
    void testShowCreateRoomForm_AuthorizedAccess() {
        session.setAttribute("userType", UserType.MASTER);
        String viewName = roomUnitController.showCreateRoomForm(session);
        assertEquals("master_addRoom", viewName);
    }

    @Test
    void testRemoveRoom_UnauthorizedAccess() {
        String viewName = roomUnitController.removeRoom(1L, model, session);
        assertEquals("redirect:/login?error=Unauthorized access", viewName);
    }

    @Test
    void testRemoveRoom_AuthorizedAccess_RoomExists() {
        session.setAttribute("userType", UserType.MASTER);
        RoomUnit room = new RoomUnit();
        when(roomUnitService.getRoomById(1L)).thenReturn(room);

        String viewName = roomUnitController.removeRoom(1L, model, session);
        assertEquals("master_confirmRemoveRoom", viewName);
        verify(model).addAttribute("warning", "Are you sure you want to remove this room?");
        verify(model).addAttribute("roomId", 1L);
    }

    @Test
    void testRemoveRoom_AuthorizedAccess_RoomDoesNotExist() {
        session.setAttribute("userType", UserType.MASTER);
        when(roomUnitService.getRoomById(1L)).thenReturn(null);

        String viewName = roomUnitController.removeRoom(1L, model, session);
        assertEquals("redirect:/master", viewName);
    }

    @Test
    void testConfirmRemoveRoom_UnauthorizedAccess() {
        String viewName = roomUnitController.confirmRemoveRoom(1L, session);
        assertEquals("redirect:/login?error=Unauthorized access", viewName);
    }

    @Test
    void testConfirmRemoveRoom_AuthorizedAccess_RoomExists() {
        session.setAttribute("userType", UserType.MASTER);
        RoomUnit room = new RoomUnit();
        when(roomUnitService.getRoomById(1L)).thenReturn(room);

        List<AssessmentUnit> assessments = Arrays.asList(new AssessmentUnit());
        when(assessmentUnitService.getAssessmentsByRoomId(1L)).thenReturn(assessments);

        String viewName = roomUnitController.confirmRemoveRoom(1L, session);
        assertEquals("redirect:/master", viewName);
        verify(assessmentUnitService).deleteAssessment(anyLong(), anyLong());
        verify(roomUnitService).deleteRoom(1L);
    }

    @Test
    void testConfirmRemoveRoom_AuthorizedAccess_RoomDoesNotExist() {
        session.setAttribute("userType", UserType.MASTER);
        when(roomUnitService.getRoomById(1L)).thenReturn(null);

        String viewName = roomUnitController.confirmRemoveRoom(1L, session);
        assertEquals("redirect:/master", viewName);
    }

    @Test
    void testSaveAssessment_UnauthorizedAccess() {
        String viewName = roomUnitController.saveAssessment(new HashMap<>(), Arrays.asList(1L), model, session);
        assertEquals("redirect:/login?error=Unauthorized access", viewName);
    }

    @Test
    void testSaveAssessment_AuthorizedAccess_Success() {
        session.setAttribute("userType", UserType.MASTER);

        Map<String, String> params = new HashMap<>();
        List<Long> roomIds = Arrays.asList(1L, 2L);

        String viewName = roomUnitController.saveAssessment(params, roomIds, model, session);
        assertEquals("redirect:/master", viewName);
        verify(assessmentUnitService).saveAssessment(any(AssessmentUnit.class), eq(roomIds));
    }

    @Test
    void testSaveAssessment_AuthorizedAccess_IncompleteInformation() {
        session.setAttribute("userType", UserType.MASTER);

        Map<String, String> params = new HashMap<>();
        List<Long> roomIds = Arrays.asList(1L, 2L);

        doThrow(new IllegalArgumentException("Incomplete information")).when(assessmentUnitService).saveAssessment(any(AssessmentUnit.class), eq(roomIds));

        String viewName = roomUnitController.saveAssessment(params, roomIds, model, session);
        assertEquals("master_addAssessment", viewName);
        verify(model).addAttribute("error", "Incomplete information");
    }

    @Test
    void testSaveAssessment_AuthorizedAccess_DuplicateEntry() {
        session.setAttribute("userType", UserType.MASTER);

        Map<String, String> params = new HashMap<>();
        List<Long> roomIds = Arrays.asList(1L, 2L);

        doThrow(new RuntimeException("Duplicate entry")).when(assessmentUnitService).saveAssessment(any(AssessmentUnit.class), eq(roomIds));

        String viewName = roomUnitController.saveAssessment(params, roomIds, model, session);
        assertEquals("master_addAssessment", viewName);
        verify(model).addAttribute("error", "Duplicate entry or integrity constraint violated");
    }

    @Test
    void testUpdateAssessment_UnauthorizedAccess() {
        String viewName = roomUnitController.updateAssessment(1L, new HashMap<>(), Arrays.asList(1L), model, session);
        assertEquals("redirect:/login?error=Unauthorized access", viewName);
    }

    @Test
    void testUpdateAssessment_AuthorizedAccess_Success() {
        session.setAttribute("userType", UserType.MASTER);

        Map<String, String> params = new HashMap<>();
        List<Long> roomIds = Arrays.asList(1L, 2L);

        String viewName = roomUnitController.updateAssessment(1L, params, roomIds, model, session);
        assertEquals("redirect:/master", viewName);
        verify(assessmentUnitService).updateAssessment(eq(1L), any(AssessmentUnit.class), eq(roomIds));
    }

    @Test
    void testUpdateAssessment_AuthorizedAccess_IncompleteInformation() {
        session.setAttribute("userType", UserType.MASTER);

        Map<String, String> params = new HashMap<>();
        List<Long> roomIds = Arrays.asList(1L, 2L);

        doThrow(new IllegalArgumentException("Incomplete information")).when(assessmentUnitService).updateAssessment(eq(1L), any(AssessmentUnit.class), eq(roomIds));

        String viewName = roomUnitController.updateAssessment(1L, params, roomIds, model, session);
        assertEquals("master_editAssessment", viewName);
        verify(model).addAttribute("error", "Incomplete information");
    }

    @Test
    void testUpdateAssessment_AuthorizedAccess_DuplicateEntry() {
        session.setAttribute("userType", UserType.MASTER);

        Map<String, String> params = new HashMap<>();
        List<Long> roomIds = Arrays.asList(1L, 2L);

        doThrow(new RuntimeException("Duplicate entry")).when(assessmentUnitService).updateAssessment(eq(1L), any(AssessmentUnit.class), eq(roomIds));

        String viewName = roomUnitController.updateAssessment(1L, params, roomIds, model, session);
        assertEquals("master_editAssessment", viewName);
        verify(model).addAttribute("error", "Duplicate entry or integrity constraint violated");
    }
}
