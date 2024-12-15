package com.upt.upt.service;

import com.upt.upt.entity.*;
import com.upt.upt.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private MasterUnitRepository masterUnitRepository;

    @Mock
    private DirectorUnitRepository directorUnitRepository;

    @Mock
    private CoordinatorUnitRepository coordinatorUnitRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testValidateUser_Master() {
        // Arrange
        String username = "masteruser";
        String password = "password";
        MasterUnit master = new MasterUnit();
        when(masterUnitRepository.findByUsernameAndPassword(username, password)).thenReturn(master);

        // Act
        UserType result = userService.validateUser(username, password);

        // Assert
        assertEquals(UserType.MASTER, result);
    }

    @Test
    void testValidateUser_Director() {
        // Arrange
        String username = "directoruser";
        String password = "password";
        DirectorUnit director = new DirectorUnit();
        when(masterUnitRepository.findByUsernameAndPassword(username, password)).thenReturn(null);
        when(directorUnitRepository.findByUsernameAndPassword(username, password)).thenReturn(director);

        // Act
        UserType result = userService.validateUser(username, password);

        // Assert
        assertEquals(UserType.DIRECTOR, result);
    }

    @Test
    void testValidateUser_Coordinator() {
        // Arrange
        String username = "coordinatoruser";
        String password = "password";
        CoordinatorUnit coordinator = new CoordinatorUnit();
        when(masterUnitRepository.findByUsernameAndPassword(username, password)).thenReturn(null);
        when(directorUnitRepository.findByUsernameAndPassword(username, password)).thenReturn(null);
        when(coordinatorUnitRepository.findByUsernameAndPassword(username, password)).thenReturn(coordinator);

        // Act
        UserType result = userService.validateUser(username, password);

        // Assert
        assertEquals(UserType.COORDINATOR, result);
    }

    @Test
    void testValidateUser_NotFound() {
        // Arrange
        String username = "unknownuser";
        String password = "password";
        when(masterUnitRepository.findByUsernameAndPassword(username, password)).thenReturn(null);
        when(directorUnitRepository.findByUsernameAndPassword(username, password)).thenReturn(null);
        when(coordinatorUnitRepository.findByUsernameAndPassword(username, password)).thenReturn(null);

        // Act
        UserType result = userService.validateUser(username, password);

        // Assert
        assertNull(result);
    }

    @Test
    void testGetUserIdByUsername_Master() {
        // Arrange
        String username = "masteruser";
        MasterUnit master = new MasterUnit();
        master.setId(1L);
        when(masterUnitRepository.findByUsername(username)).thenReturn(Optional.of(master));

        // Act
        Long result = userService.getUserIdByUsername(username, UserType.MASTER);

        // Assert
        assertEquals(1L, result);
    }

    @Test
    void testGetUserIdByUsername_NotFound() {
        // Arrange
        String username = "unknownuser";
        when(masterUnitRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act
        Long result = userService.getUserIdByUsername(username, UserType.MASTER);

        // Assert
        assertNull(result);
    }

    @Test
    void testUsernameExists_True() {
        // Arrange
        String username = "existinguser";
        when(masterUnitRepository.findByUsername(username)).thenReturn(Optional.of(new MasterUnit()));

        // Act
        boolean result = userService.usernameExists(username);

        // Assert
        assertTrue(result);
    }

    @Test
    void testUsernameExists_False() {
        // Arrange
        String username = "nonexistentuser";
        when(masterUnitRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(directorUnitRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(coordinatorUnitRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act
        boolean result = userService.usernameExists(username);

        // Assert
        assertFalse(result);
    }
}
