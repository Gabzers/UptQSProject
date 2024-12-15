package com.upt.upt.service;

import com.upt.upt.entity.CoordinatorUnit;
import com.upt.upt.entity.DirectorUnit;
import com.upt.upt.entity.YearUnit;
import com.upt.upt.repository.CoordinatorUnitRepository;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for CoordinatorUnitService.
 * 
 * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
 */
class CoordinatorUnitServiceTest {

    @Mock
    private CoordinatorUnitRepository coordinatorRepository;

    @Mock
    private DirectorUnitService directorUnitService;

    @Mock
    private UserService userService;

    @Mock
    private HttpSession session;

    @InjectMocks
    private CoordinatorUnitService coordinatorService;

    /**
     * Sets up the test environment.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests the getAllCoordinators method.
     */
    @Test
    void testGetAllCoordinators() {
        List<CoordinatorUnit> coordinators = new ArrayList<>();
        coordinators.add(new CoordinatorUnit());
        when(coordinatorRepository.findAll()).thenReturn(coordinators);

        List<CoordinatorUnit> result = coordinatorService.getAllCoordinators();
        assertEquals(1, result.size());
        verify(coordinatorRepository, times(1)).findAll();
    }

    /**
     * Tests the getCoordinatorById method.
     */
    @Test
    void testGetCoordinatorById() {
        CoordinatorUnit coordinator = new CoordinatorUnit();
        coordinator.setId(1L);
        when(coordinatorRepository.findById(1L)).thenReturn(Optional.of(coordinator));

        Optional<CoordinatorUnit> result = coordinatorService.getCoordinatorById(1L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(coordinatorRepository, times(1)).findById(1L);
    }

    /**
     * Tests the saveCoordinator method.
     */
    @Test
    void testSaveCoordinator() {
        CoordinatorUnit coordinator = new CoordinatorUnit();
        when(coordinatorRepository.save(coordinator)).thenReturn(coordinator);

        CoordinatorUnit result = coordinatorService.saveCoordinator(coordinator);
        assertNotNull(result);
        verify(coordinatorRepository, times(1)).save(coordinator);
    }

    /**
     * Tests the deleteCoordinator method.
     */
    @Test
    void testDeleteCoordinator() {
        doNothing().when(coordinatorRepository).deleteById(1L);

        coordinatorService.deleteCoordinator(1L);
        verify(coordinatorRepository, times(1)).deleteById(1L);
    }

    /**
     * Tests the usernameExists method.
     */
    @Test
    void testUsernameExists() {
        when(coordinatorRepository.findByUsername("existingUser")).thenReturn(Optional.of(new CoordinatorUnit()));
        when(coordinatorRepository.findByUsername("nonExistentUser")).thenReturn(Optional.empty());

        assertTrue(coordinatorService.usernameExists("existingUser"));
        assertFalse(coordinatorService.usernameExists("nonExistentUser"));
        verify(coordinatorRepository, times(2)).findByUsername(anyString());
    }

    /**
     * Tests the saveCoordinatorWithDirector method.
     */
    @Test
    void testSaveCoordinatorWithDirector() {
        CoordinatorUnit coordinator = new CoordinatorUnit();
        coordinator.setUsername("newUser");

        DirectorUnit director = new DirectorUnit();
        when(userService.usernameExists("newUser")).thenReturn(false);
        when(session.getAttribute("userId")).thenReturn(1L);
        when(directorUnitService.getDirectorById(1L)).thenReturn(Optional.of(director));

        coordinatorService.saveCoordinatorWithDirector(coordinator, session);

        verify(coordinatorRepository, times(1)).save(coordinator);
        verify(directorUnitService, times(1)).getDirectorById(1L);
        verify(userService, times(1)).usernameExists("newUser");
    }

    /**
     * Tests the saveCoordinatorWithDirector method when the username already exists.
     */
    @Test
    void testSaveCoordinatorWithDirectorThrowsWhenUsernameExists() {
        CoordinatorUnit coordinator = new CoordinatorUnit();
        coordinator.setUsername("existingUser");

        when(userService.usernameExists("existingUser")).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            coordinatorService.saveCoordinatorWithDirector(coordinator, session);
        });

        assertEquals("Username already exists", exception.getMessage());
        verify(coordinatorRepository, never()).save(any(CoordinatorUnit.class));
    }

    /**
     * Tests the updateCoordinator method.
     */
    @Test
    void testUpdateCoordinator() {
        CoordinatorUnit existingCoordinator = new CoordinatorUnit();
        existingCoordinator.setId(1L);
        existingCoordinator.setName("Old Name");

        CoordinatorUnit updatedData = new CoordinatorUnit();
        updatedData.setName("New Name");

        when(coordinatorRepository.findById(1L)).thenReturn(Optional.of(existingCoordinator));

        coordinatorService.updateCoordinator(1L, updatedData);

        verify(coordinatorRepository, times(1)).save(existingCoordinator);
        assertEquals("New Name", existingCoordinator.getName());
    }

    /**
     * Tests the hasYearCreated method.
     */
    @Test
    void testHasYearCreated() {
        DirectorUnit director = new DirectorUnit();
        director.setAcademicYears(new ArrayList<>());

        when(directorUnitService.getDirectorById(1L)).thenReturn(Optional.of(director));

        // Initially, there are no academic years
        assertFalse(coordinatorService.hasYearCreated(1L));

        // Add a valid YearUnit
        YearUnit yearUnit = new YearUnit();
        yearUnit.setId(1L);
        yearUnit.setSpecialExamStart("2024-01-01");
        yearUnit.setSpecialExamEnd("2024-01-31");

        director.getAcademicYears().add(yearUnit);

        // Now, it should return true
        assertTrue(coordinatorService.hasYearCreated(1L));
    }
}
