package com.upt.upt.repository;

import com.upt.upt.entity.DirectorUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for DirectorUnitRepository.
 * 
 * @autor Grupo 5 - 47719, 47713, 46697, 47752, 47004
 */
class DirectorUnitRepositoryTest {

    @Mock
    private DirectorUnitRepository directorUnitRepository;

    private DirectorUnit mockDirectorUnit;

    /**
     * Set up test data.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock DirectorUnit object
        mockDirectorUnit = new DirectorUnit();
        mockDirectorUnit.setId(1L);
        mockDirectorUnit.setUsername("test_user");
        mockDirectorUnit.setPassword("password123");
    }

    /**
     * Test findByUsernameAndPassword method.
     */
    @Test
    void testFindByUsernameAndPassword() {
        // Arrange
        when(directorUnitRepository.findByUsernameAndPassword("test_user", "password123"))
                .thenReturn(mockDirectorUnit);

        // Act
        DirectorUnit result = directorUnitRepository.findByUsernameAndPassword("test_user", "password123");

        // Assert
        assertNotNull(result);
        assertEquals("test_user", result.getUsername());
        assertEquals("password123", result.getPassword());
        verify(directorUnitRepository, times(1)).findByUsernameAndPassword("test_user", "password123");
    }

    /**
     * Test findByUsername method.
     */
    @Test
    void testFindByUsername() {
        // Arrange
        when(directorUnitRepository.findByUsername("test_user"))
                .thenReturn(Optional.of(mockDirectorUnit));

        // Act
        Optional<DirectorUnit> result = directorUnitRepository.findByUsername("test_user");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("test_user", result.get().getUsername());
        verify(directorUnitRepository, times(1)).findByUsername("test_user");
    }

    /**
     * Test findByUsername method when username is not found.
     */
    @Test
    void testFindByUsername_NotFound() {
        // Arrange
        when(directorUnitRepository.findByUsername("unknown_user"))
                .thenReturn(Optional.empty());

        // Act
        Optional<DirectorUnit> result = directorUnitRepository.findByUsername("unknown_user");

        // Assert
        assertFalse(result.isPresent());
        verify(directorUnitRepository, times(1)).findByUsername("unknown_user");
    }
}
