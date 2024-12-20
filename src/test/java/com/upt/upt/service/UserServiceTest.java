package com.upt.upt.service;

import com.upt.upt.entity.CoordinatorUnit;
import com.upt.upt.entity.DirectorUnit;
import com.upt.upt.entity.MasterUnit;
import com.upt.upt.entity.UserType;
import com.upt.upt.repository.CoordinatorUnitRepository;
import com.upt.upt.repository.DirectorUnitRepository;
import com.upt.upt.repository.MasterUnitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


/**
 * Test class for UserServiceTest.
 * 
 * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
 */
public class UserServiceTest {

    @Mock
    private MasterUnitRepository masterUnitRepository;

    @Mock
    private DirectorUnitRepository directorUnitRepository;

    @Mock
    private CoordinatorUnitRepository coordinatorUnitRepository;

    @InjectMocks
    private UserService userService;

    /**
     * Sets up the test environment before each test.
     * Initializes mocks.
     * 
     * 
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests the validation of a master user.
     * 
     * 
     */
    @Test
    void testValidateUser_Master() {
        String username = "masteruser";
        String password = "password";
        MasterUnit master = new MasterUnit();
        when(masterUnitRepository.findByUsernameAndPassword(username, password)).thenReturn(master);

        UserType result = userService.validateUser(username, password);

        assertEquals(UserType.MASTER, result);
    }

    /**
     * Tests the validation of a director user.
     * 
     * 
     */
    @Test
    void testValidateUser_Director() {
        String username = "directoruser";
        String password = "password";
        DirectorUnit director = new DirectorUnit();
        when(masterUnitRepository.findByUsernameAndPassword(username, password)).thenReturn(null);
        when(directorUnitRepository.findByUsernameAndPassword(username, password)).thenReturn(director);

        UserType result = userService.validateUser(username, password);

        assertEquals(UserType.DIRECTOR, result);
    }

    /**
     * Tests the validation of a coordinator user.
     * 
     * 
     */
    @Test
    void testValidateUser_Coordinator() {
        String username = "coordinatoruser";
        String password = "password";
        CoordinatorUnit coordinator = new CoordinatorUnit();
        when(masterUnitRepository.findByUsernameAndPassword(username, password)).thenReturn(null);
        when(directorUnitRepository.findByUsernameAndPassword(username, password)).thenReturn(null);
        when(coordinatorUnitRepository.findByUsernameAndPassword(username, password)).thenReturn(coordinator);

        UserType result = userService.validateUser(username, password);

        assertEquals(UserType.COORDINATOR, result);
    }

    /**
     * Tests the validation of a user that is not found.
     * 
     * 
     */
    @Test
    void testValidateUser_NotFound() {
        String username = "unknownuser";
        String password = "password";
        when(masterUnitRepository.findByUsernameAndPassword(username, password)).thenReturn(null);
        when(directorUnitRepository.findByUsernameAndPassword(username, password)).thenReturn(null);
        when(coordinatorUnitRepository.findByUsernameAndPassword(username, password)).thenReturn(null);

        UserType result = userService.validateUser(username, password);

        assertNull(result);
    }

    /**
     * Tests the retrieval of a user ID by username for a master user.
     * 
     * 
     */
    @Test
    void testGetUserIdByUsername_Master() {
        String username = "masteruser";
        MasterUnit master = new MasterUnit();
        master.setId(1L);
        when(masterUnitRepository.findByUsername(username)).thenReturn(Optional.of(master));

        Long result = userService.getUserIdByUsername(username, UserType.MASTER);

        assertEquals(1L, result);
    }

    /**
     * Tests the retrieval of a user ID by username when the user is not found.
     * 
     * 
     */
    @Test
    void testGetUserIdByUsername_NotFound() {
        String username = "unknownuser";
        when(masterUnitRepository.findByUsername(username)).thenReturn(Optional.empty());

        Long result = userService.getUserIdByUsername(username, UserType.MASTER);

        assertNull(result);
    }

    /**
     * Tests if a username exists in the system.
     * 
     * 
     */
    @Test
    void testUsernameExists_True() {
        String username = "existinguser";
        when(masterUnitRepository.findByUsername(username)).thenReturn(Optional.of(new MasterUnit()));

        boolean result = userService.usernameExists(username);

        assertTrue(result);
    }

    /**
     * Tests if a username does not exist in the system.
     * 
     * 
     */
    @Test
    void testUsernameExists_False() {
        String username = "nonexistentuser";
        when(masterUnitRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(directorUnitRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(coordinatorUnitRepository.findByUsername(username)).thenReturn(Optional.empty());

        boolean result = userService.usernameExists(username);

        assertFalse(result);
    }
}
