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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.mock.web.MockHttpSession;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.doThrow;

/**
 * Unit tests for RoomUnitController.
 * 
 * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
 */
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

    /**
     * Sets up the test environment before each test.
     */
    @BeforeEach
    void setUp() {
        session = new MockHttpSession();
    }

    /**
     * Tests the listRooms method for unauthorized access.
     */
    @Test
    void testListRooms_UnauthorizedAccess() {
        String viewName = roomUnitController.listRooms(model, session);
        assertEquals("redirect:/login?error=Unauthorized access", viewName);
    }

    /**
     * Tests the listRooms method for authorized access.
     */
    @Test
    void testListRooms_AuthorizedAccess() {
        session.setAttribute("userType", UserType.MASTER);
        List<RoomUnit> roomUnits = Arrays.asList(new RoomUnit());
        when(roomUnitService.getAllRooms()).thenReturn(roomUnits);

        String viewName = roomUnitController.listRooms(model, session);
        assertEquals("master_index", viewName);
        verify(model).addAttribute("roomUnits", roomUnits);
    }

    /**
     * Tests the showCreateRoomForm method for unauthorized access.
     */
    @Test
    void testShowCreateRoomForm_UnauthorizedAccess() {
        String viewName = roomUnitController.showCreateRoomForm(session);
        assertEquals("redirect:/login?error=Unauthorized access", viewName);
    }

    /**
     * Tests the showCreateRoomForm method for authorized access.
     */
    @Test
    void testShowCreateRoomForm_AuthorizedAccess() {
        session.setAttribute("userType", UserType.MASTER);
        String viewName = roomUnitController.showCreateRoomForm(session);
        assertEquals("master_addRoom", viewName);
    }

    /**
     * Tests the removeRoom method for unauthorized access.
     */
    @Test
    void testRemoveRoom_UnauthorizedAccess() {
        String viewName = roomUnitController.removeRoom(1L, model, session);
        assertEquals("redirect:/login?error=Unauthorized access", viewName);
    }

    /**
     * Tests the removeRoom method for authorized access when the room exists.
     */
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

    /**
     * Tests the removeRoom method for authorized access when the room does not exist.
     */
    @Test
    void testRemoveRoom_AuthorizedAccess_RoomDoesNotExist() {
        session.setAttribute("userType", UserType.MASTER);
        when(roomUnitService.getRoomById(1L)).thenReturn(null);

        String viewName = roomUnitController.removeRoom(1L, model, session);
        assertEquals("redirect:/master", viewName);
    }

    /**
     * Tests the confirmRemoveRoom method for unauthorized access.
     */
    @Test
    void testConfirmRemoveRoom_UnauthorizedAccess() {
        String viewName = roomUnitController.confirmRemoveRoom(1L, session);
        assertEquals("redirect:/login?error=Unauthorized access", viewName);
    }

    /**
     * Tests the confirmRemoveRoom method for authorized access when the room exists.
     */
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

    /**
     * Tests the confirmRemoveRoom method for authorized access when the room does not exist.
     */
    @Test
    void testConfirmRemoveRoom_AuthorizedAccess_RoomDoesNotExist() {
        session.setAttribute("userType", UserType.MASTER);
        when(roomUnitService.getRoomById(1L)).thenReturn(null);

        String viewName = roomUnitController.confirmRemoveRoom(1L, session);
        assertEquals("redirect:/master", viewName);
    }

    /**
     * Tests the saveAssessment method for unauthorized access.
     */
    @Test
    void testSaveAssessment_UnauthorizedAccess() {
        String viewName = roomUnitController.saveAssessment(new HashMap<>(), Arrays.asList(1L), model, session);
        assertEquals("redirect:/login?error=Unauthorized access", viewName);
    }

    /**
     * Tests the saveAssessment method for authorized access with successful save.
     */
    @Test
    void testSaveAssessment_AuthorizedAccess_Success() {
        session.setAttribute("userType", UserType.MASTER);

        Map<String, String> params = new HashMap<>();
        List<Long> roomIds = Arrays.asList(1L, 2L);

        String viewName = roomUnitController.saveAssessment(params, roomIds, model, session);
        assertEquals("redirect:/master", viewName);
        verify(assessmentUnitService).saveAssessment(any(AssessmentUnit.class), eq(roomIds));
    }

    /**
     * Tests the saveAssessment method for authorized access with incomplete information.
     */
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

    /**
     * Tests the saveAssessment method for authorized access with duplicate entry.
     */
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

    /**
     * Tests the updateAssessment method for unauthorized access.
     */
    @Test
    void testUpdateAssessment_UnauthorizedAccess() {
        String viewName = roomUnitController.updateAssessment(1L, new HashMap<>(), Arrays.asList(1L), model, session);
        assertEquals("redirect:/login?error=Unauthorized access", viewName);
    }

    /**
     * Tests the updateAssessment method for authorized access with successful update.
     */
    @Test
    void testUpdateAssessment_AuthorizedAccess_Success() {
        session.setAttribute("userType", UserType.MASTER);

        Map<String, String> params = new HashMap<>();
        List<Long> roomIds = Arrays.asList(1L, 2L);

        String viewName = roomUnitController.updateAssessment(1L, params, roomIds, model, session);
        assertEquals("redirect:/master", viewName);
        verify(assessmentUnitService).updateAssessment(eq(1L), any(AssessmentUnit.class), eq(roomIds));
    }

    /**
     * Tests the updateAssessment method for authorized access with incomplete information.
     */
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

    /**
     * Tests the updateAssessment method for authorized access with duplicate entry.
     */
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
