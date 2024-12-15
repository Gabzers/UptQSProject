package com.upt.upt.service;

import com.upt.upt.entity.DirectorUnit;
import com.upt.upt.repository.DirectorUnitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for DirectorUnitService.
 * 
 * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
 */
class DirectorUnitServiceTest {

    @InjectMocks
    private DirectorUnitService directorUnitService;

    @Mock
    private DirectorUnitRepository directorUnitRepository;

    @Mock
    private UserService userService;

    private DirectorUnit mockDirectorUnit;

    /**
     * Sets up the test environment before each test.
     * Initializes mocks and creates a test instance of DirectorUnit.
     * 
     * 
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockDirectorUnit = new DirectorUnit();
        mockDirectorUnit.setId(1L);
        mockDirectorUnit.setUsername("test_user");
        mockDirectorUnit.setPassword("password123");
        mockDirectorUnit.setName("Test Name");
        mockDirectorUnit.setDepartment("Test Department");

        directorUnitService = new DirectorUnitService(directorUnitRepository);
        directorUnitService.userService = userService;
    }

    /**
     * Tests the saving of a director unit.
     * 
     * 
     */
    @Test
    void testSaveDirector() {
        when(directorUnitRepository.save(mockDirectorUnit)).thenReturn(mockDirectorUnit);

        DirectorUnit result = directorUnitService.saveDirector(mockDirectorUnit);

        assertNotNull(result);
        assertEquals("test_user", result.getUsername());
        verify(directorUnitRepository, times(1)).save(mockDirectorUnit);
    }

    /**
     * Tests the retrieval of all director units.
     * 
     * 
     */
    @Test
    void testGetAllDirectors() {
        List<DirectorUnit> mockDirectorList = List.of(mockDirectorUnit);
        when(directorUnitRepository.findAll()).thenReturn(mockDirectorList);

        List<DirectorUnit> result = directorUnitService.getAllDirectors();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(directorUnitRepository, times(1)).findAll();
    }

    /**
     * Tests the retrieval of a director unit by its ID.
     * 
     * 
     */
    @Test
    void testGetDirectorById() {
        when(directorUnitRepository.findById(1L)).thenReturn(Optional.of(mockDirectorUnit));

        Optional<DirectorUnit> result = directorUnitService.getDirectorById(1L);

        assertTrue(result.isPresent());
        assertEquals("test_user", result.get().getUsername());
        verify(directorUnitRepository, times(1)).findById(1L);
    }

    /**
     * Tests the update of a director unit.
     * 
     * 
     */
    @Test
    void testUpdateDirector() {
        Map<String, String> params = Map.of(
                "director-name", "Updated Name",
                "director-department", "Updated Department",
                "director-username", "updated_user",
                "director-password", "new_password"
        );

        when(directorUnitRepository.findById(1L)).thenReturn(Optional.of(mockDirectorUnit));

        DirectorUnit result = directorUnitService.updateDirector(1L, params);

        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
        assertEquals("Updated Department", result.getDepartment());
        assertEquals("updated_user", result.getUsername());
        assertEquals("new_password", result.getPassword());
        verify(directorUnitRepository, times(1)).findById(1L);
    }

    /**
     * Tests the deletion of a director unit by its ID.
     * 
     * 
     */
    @Test
    void testDeleteDirector() {
        directorUnitService.deleteDirector(1L);

        verify(directorUnitRepository, times(1)).deleteById(1L);
    }

    /**
     * Tests if a username exists in the repository.
     * 
     * 
     */
    @Test
    void testUsernameExists() {
        when(directorUnitRepository.findByUsername("test_user")).thenReturn(Optional.of(mockDirectorUnit));

        boolean result = directorUnitService.usernameExists("test_user");

        assertTrue(result);
        verify(directorUnitRepository, times(1)).findByUsername("test_user");
    }

    /**
     * Tests the creation of a new director unit.
     * 
     * 
     */
    @Test
    void testCreateDirector() {
        Map<String, String> params = Map.of(
                "director-name", "New Name",
                "director-department", "New Department",
                "director-username", "new_user",
                "director-password", "new_password"
        );

        when(userService.usernameExists("new_user")).thenReturn(false);

        DirectorUnit result = directorUnitService.createDirector(params);

        assertNotNull(result);
        assertEquals("New Name", result.getName());
        assertEquals("New Department", result.getDepartment());
        assertEquals("new_user", result.getUsername());
        assertEquals("new_password", result.getPassword());
        verify(userService, times(1)).usernameExists("new_user");
    }

    /**
     * Tests the creation of a new director unit when the username already exists.
     * 
     * 
     */
    @Test
    void testCreateDirector_ThrowsExceptionWhenUsernameExists() {
        Map<String, String> params = Map.of(
                "director-name", "New Name",
                "director-department", "New Department",
                "director-username", "existing_user",
                "director-password", "new_password"
        );

        when(userService.usernameExists("existing_user")).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                directorUnitService.createDirector(params)
        );
        assertEquals("Username already exists", exception.getMessage());
        verify(userService, times(1)).usernameExists("existing_user");
    }
}
