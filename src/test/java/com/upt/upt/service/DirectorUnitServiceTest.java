package com.upt.upt.service;

import com.upt.upt.entity.DirectorUnit;
import com.upt.upt.entity.YearUnit;
import com.upt.upt.repository.DirectorUnitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for DirectorUnitService.
 */
class DirectorUnitServiceTest {

    @InjectMocks
    private DirectorUnitService directorUnitService;

    @Mock
    private DirectorUnitRepository directorUnitRepository;

    @Mock
    private UserService userService;

    private DirectorUnit mockDirectorUnit;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock DirectorUnit object
        mockDirectorUnit = new DirectorUnit();
        mockDirectorUnit.setId(1L);
        mockDirectorUnit.setUsername("test_user");
        mockDirectorUnit.setPassword("password123");
        mockDirectorUnit.setName("Test Name");
        mockDirectorUnit.setDepartment("Test Department");

        // Injetar o mock manualmente, caso necessário
        directorUnitService = new DirectorUnitService(directorUnitRepository);
        directorUnitService.userService = userService;
    }

    @Test
    void testSaveDirector() {
        // Arrange
        when(directorUnitRepository.save(mockDirectorUnit)).thenReturn(mockDirectorUnit);

        // Act
        DirectorUnit result = directorUnitService.saveDirector(mockDirectorUnit);

        // Assert
        assertNotNull(result);
        assertEquals("test_user", result.getUsername());
        verify(directorUnitRepository, times(1)).save(mockDirectorUnit);
    }

    @Test
    void testGetAllDirectors() {
        // Arrange
        List<DirectorUnit> mockDirectorList = List.of(mockDirectorUnit);
        when(directorUnitRepository.findAll()).thenReturn(mockDirectorList);

        // Act
        List<DirectorUnit> result = directorUnitService.getAllDirectors();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(directorUnitRepository, times(1)).findAll();
    }

    @Test
    void testGetDirectorById() {
        // Arrange
        when(directorUnitRepository.findById(1L)).thenReturn(Optional.of(mockDirectorUnit));

        // Act
        Optional<DirectorUnit> result = directorUnitService.getDirectorById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("test_user", result.get().getUsername());
        verify(directorUnitRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateDirector() {
        // Arrange
        Map<String, String> params = Map.of(
                "director-name", "Updated Name",
                "director-department", "Updated Department",
                "director-username", "updated_user",
                "director-password", "new_password"
        );

        when(directorUnitRepository.findById(1L)).thenReturn(Optional.of(mockDirectorUnit));

        // Act
        DirectorUnit result = directorUnitService.updateDirector(1L, params);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
        assertEquals("Updated Department", result.getDepartment());
        assertEquals("updated_user", result.getUsername());
        assertEquals("new_password", result.getPassword());
        verify(directorUnitRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteDirector() {
        // Act
        directorUnitService.deleteDirector(1L);

        // Assert
        verify(directorUnitRepository, times(1)).deleteById(1L);
    }

    @Test
    void testUsernameExists() {
        // Arrange
        when(directorUnitRepository.findByUsername("test_user")).thenReturn(Optional.of(mockDirectorUnit));

        // Act
        boolean result = directorUnitService.usernameExists("test_user");

        // Assert
        assertTrue(result);
        verify(directorUnitRepository, times(1)).findByUsername("test_user");
    }

    @Test
    void testCreateDirector() {
        // Arrange
        Map<String, String> params = Map.of(
                "director-name", "New Name",
                "director-department", "New Department",
                "director-username", "new_user",
                "director-password", "new_password"
        );

        when(userService.usernameExists("new_user")).thenReturn(false);

        // Act
        DirectorUnit result = directorUnitService.createDirector(params);

        // Assert
        assertNotNull(result);
        assertEquals("New Name", result.getName());
        assertEquals("New Department", result.getDepartment());
        assertEquals("new_user", result.getUsername());
        assertEquals("new_password", result.getPassword());
        verify(userService, times(1)).usernameExists("new_user");
    }

    @Test
    void testCreateDirector_ThrowsExceptionWhenUsernameExists() {
        // Arrange
        Map<String, String> params = Map.of(
                "director-name", "New Name",
                "director-department", "New Department",
                "director-username", "existing_user",
                "director-password", "new_password"
        );

        when(userService.usernameExists("existing_user")).thenReturn(true);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                directorUnitService.createDirector(params)
        );
        assertEquals("Username already exists", exception.getMessage());
        verify(userService, times(1)).usernameExists("existing_user");
    }
}
