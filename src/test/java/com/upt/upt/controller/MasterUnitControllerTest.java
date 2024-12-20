package com.upt.upt.controller;

import com.upt.upt.entity.MasterUnit;
import com.upt.upt.entity.UserType;
import com.upt.upt.service.DirectorUnitService;
import com.upt.upt.service.MasterUnitService;
import com.upt.upt.service.RoomUnitService;
import com.upt.upt.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpSession;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;

/**
 * Unit tests for MasterUnitController.
 * 
 * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
 */
public class MasterUnitControllerTest {

    @Mock
    private DirectorUnitService directorUnitService;

    @Mock
    private MasterUnitService masterUnitService;

    @Mock
    private RoomUnitService roomUnitService;

    @Mock
    private UserService userService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @InjectMocks
    private MasterUnitController masterUnitController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test method for listDirectorsMastersAndRooms when user is not a master.
     */
    @Test
    void listDirectorsMastersAndRooms_WhenUserIsNotMaster_ShouldRedirectToLogin() {
        when(session.getAttribute("userType")).thenReturn(UserType.DIRECTOR);

        String result = masterUnitController.listDirectorsMastersAndRooms(model, session);

        assertEquals("redirect:/login?error=Unauthorized access", result);
    }

    /**
     * Test method for listDirectorsMastersAndRooms when user is a master.
     */
    @Test
    void listDirectorsMastersAndRooms_WhenUserIsMaster_ShouldReturnMasterIndex() {
        when(session.getAttribute("userType")).thenReturn(UserType.MASTER);

        String result = masterUnitController.listDirectorsMastersAndRooms(model, session);

        verify(model).addAttribute(eq("directorUnits"), any());
        verify(model).addAttribute(eq("masterUnits"), any());
        verify(model).addAttribute(eq("roomUnits"), any());
        assertEquals("master_index", result);
    }

    /**
     * Test method for createMaster when user is not a master.
     */
    @Test
    void createMaster_WhenUserIsNotMaster_ShouldRedirectToLogin() {
        when(session.getAttribute("userType")).thenReturn(UserType.DIRECTOR);

        String result = masterUnitController.createMaster("name", "username", "password", session);

        assertEquals("redirect:/login?error=Unauthorized access", result);
    }

    /**
     * Test method for createMaster when username already exists.
     */
    @Test
    void createMaster_WhenUsernameExists_ShouldReturnError() {
        when(session.getAttribute("userType")).thenReturn(UserType.MASTER);
        when(userService.usernameExists("username")).thenReturn(true);

        String result = masterUnitController.createMaster("name", "username", "password", session);

        assertEquals("redirect:/master/create-master?error=Username already exists", result);
    }

    /**
     * Test method for createMaster when successful.
     */
    @Test
    void createMaster_WhenSuccessful_ShouldRedirectToMaster() {
        when(session.getAttribute("userType")).thenReturn(UserType.MASTER);
        when(userService.usernameExists("username")).thenReturn(false);
        when(masterUnitService.createMaster("name", "username", "password"))
                .thenReturn(new MasterUnit());

        String result = masterUnitController.createMaster("name", "username", "password", session);

        verify(masterUnitService).saveMaster(any(MasterUnit.class));
        assertEquals("redirect:/master", result);
    }

    /**
     * Test method for confirmRemoveMaster when user is not a master.
     */
    @Test
    void confirmRemoveMaster_WhenUserIsNotMaster_ShouldRedirectToLogin() {
        when(session.getAttribute("userType")).thenReturn(UserType.DIRECTOR);

        String result = masterUnitController.confirmRemoveMaster(1L, session);

        assertEquals("redirect:/login?error=Unauthorized access", result);
    }

    /**
     * Test method for confirmRemoveMaster when successful.
     */
    @Test
    void confirmRemoveMaster_WhenSuccessful_ShouldRedirectToMaster() {
        when(session.getAttribute("userType")).thenReturn(UserType.MASTER);

        String result = masterUnitController.confirmRemoveMaster(1L, session);

        verify(masterUnitService).deleteMaster(1L);
        assertEquals("redirect:/master", result);
    }

    /**
     * Test method for showEditMasterForm when master not found.
     */
    @Test
    void showEditMasterForm_WhenMasterNotFound_ShouldRedirectWithError() {
        when(session.getAttribute("userType")).thenReturn(UserType.MASTER);
        when(masterUnitService.getMasterById(1L)).thenReturn(Optional.empty());

        String result = masterUnitController.showEditMasterForm(1L, model, session);

        assertEquals("redirect:/master?error=Master not found", result);
    }

    /**
     * Test method for showEditMasterForm when master found.
     */
    @Test
    void showEditMasterForm_WhenMasterFound_ShouldReturnEditPage() {
        when(session.getAttribute("userType")).thenReturn(UserType.MASTER);
        when(masterUnitService.getMasterById(1L)).thenReturn(Optional.of(new MasterUnit()));

        String result = masterUnitController.showEditMasterForm(1L, model, session);

        verify(model).addAttribute(eq("master"), any(MasterUnit.class));
        assertEquals("master_editMaster", result);
    }
}
