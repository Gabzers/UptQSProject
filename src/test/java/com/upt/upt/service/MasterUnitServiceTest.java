package com.upt.upt.service;

import com.upt.upt.entity.MasterUnit;
import com.upt.upt.repository.MasterUnitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


/**
 * Test class for MasterUnitServiceTest.
 * 
 * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
 */
public class MasterUnitServiceTest {

    @Mock
    private MasterUnitRepository masterUnitRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private MasterUnitService masterUnitService;

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
     * Tests the retrieval of all master units.
     * 
     * 
     */
    @Test
    void getAllMasters_ShouldReturnListOfMasters() {
        List<MasterUnit> mockMasters = Arrays.asList(new MasterUnit(), new MasterUnit());
        when(masterUnitRepository.findAll()).thenReturn(mockMasters);

        List<MasterUnit> result = masterUnitService.getAllMasters();

        assertEquals(mockMasters, result);
        verify(masterUnitRepository, times(1)).findAll();
    }

    /**
     * Tests the saving of a master unit.
     * 
     * 
     */
    @Test
    void saveMaster_ShouldInvokeSaveOnRepository() {
        MasterUnit master = new MasterUnit();

        masterUnitService.saveMaster(master);

        verify(masterUnitRepository, times(1)).save(master);
    }

    /**
     * Tests the retrieval of a master unit by its ID when it exists.
     * 
     * 
     */
    @Test
    void getMasterById_WhenMasterExists_ShouldReturnOptionalOfMaster() {
        MasterUnit master = new MasterUnit();
        when(masterUnitRepository.findById(1L)).thenReturn(Optional.of(master));

        Optional<MasterUnit> result = masterUnitService.getMasterById(1L);

        assertTrue(result.isPresent());
        assertEquals(master, result.get());
        verify(masterUnitRepository, times(1)).findById(1L);
    }

    /**
     * Tests the retrieval of a master unit by its ID when it does not exist.
     * 
     * 
     */
    @Test
    void getMasterById_WhenMasterDoesNotExist_ShouldReturnEmptyOptional() {
        when(masterUnitRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<MasterUnit> result = masterUnitService.getMasterById(1L);

        assertFalse(result.isPresent());
        verify(masterUnitRepository, times(1)).findById(1L);
    }

    /**
     * Tests the deletion of a master unit by its ID.
     * 
     * 
     */
    @Test
    void deleteMaster_ShouldInvokeDeleteOnRepository() {
        masterUnitService.deleteMaster(1L);

        verify(masterUnitRepository, times(1)).deleteById(1L);
    }

    /**
     * Tests if a username exists in the system.
     * 
     * 
     */
    @Test
    void usernameExists_WhenUsernameExists_ShouldReturnTrue() {
        when(masterUnitRepository.findByUsername("existingUsername")).thenReturn(Optional.of(new MasterUnit()));

        boolean result = masterUnitService.usernameExists("existingUsername");

        assertTrue(result);
        verify(masterUnitRepository, times(1)).findByUsername("existingUsername");
    }

    /**
     * Tests if a username does not exist in the system.
     * 
     * 
     */
    @Test
    void usernameExists_WhenUsernameDoesNotExist_ShouldReturnFalse() {
        when(masterUnitRepository.findByUsername("nonexistentUsername")).thenReturn(Optional.empty());

        boolean result = masterUnitService.usernameExists("nonexistentUsername");

        assertFalse(result);
        verify(masterUnitRepository, times(1)).findByUsername("nonexistentUsername");
    }

    /**
     * Tests the creation of a master unit when the username already exists.
     * 
     * 
     */
    @Test
    void createMaster_WhenUsernameExists_ShouldThrowException() {
        when(userService.usernameExists("existingUsername")).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                masterUnitService.createMaster("name", "existingUsername", "password"));

        assertEquals("Username already exists", exception.getMessage());
        verify(userService, times(1)).usernameExists("existingUsername");
        verifyNoInteractions(masterUnitRepository);
    }

    /**
     * Tests the creation of a master unit when the username does not exist.
     * 
     * 
     */
    @Test
    void createMaster_WhenUsernameDoesNotExist_ShouldReturnNewMaster() {
        when(userService.usernameExists("newUsername")).thenReturn(false);

        MasterUnit result = masterUnitService.createMaster("name", "newUsername", "password");

        assertNotNull(result);
        assertEquals("name", result.getName());
        assertEquals("newUsername", result.getUsername());
        assertEquals("password", result.getPassword());
        verify(userService, times(1)).usernameExists("newUsername");
    }

    /**
     * Tests the update of a master unit when it exists.
     * 
     * 
     */
    @Test
    void updateMaster_WhenMasterExists_ShouldUpdateFields() {
        MasterUnit existingMaster = new MasterUnit();
        existingMaster.setName("oldName");
        existingMaster.setUsername("oldUsername");

        when(masterUnitRepository.findById(1L)).thenReturn(Optional.of(existingMaster));

        MasterUnit result = masterUnitService.updateMaster(1L, "newName", "newUsername", "newPassword");

        assertEquals("newName", result.getName());
        assertEquals("newUsername", result.getUsername());
        assertEquals("newPassword", result.getPassword());
        verify(masterUnitRepository, times(1)).findById(1L);
    }

    /**
     * Tests the update of a master unit when it does not exist.
     * 
     * 
     */
    @Test
    void updateMaster_WhenMasterDoesNotExist_ShouldThrowException() {
        when(masterUnitRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                masterUnitService.updateMaster(1L, "name", "username", "password"));

        assertEquals("Invalid Master ID: 1", exception.getMessage());
        verify(masterUnitRepository, times(1)).findById(1L);
    }
}
