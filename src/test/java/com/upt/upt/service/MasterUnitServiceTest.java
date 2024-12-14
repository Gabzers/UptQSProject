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

public class MasterUnitServiceTest {

    @Mock
    private MasterUnitRepository masterUnitRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private MasterUnitService masterUnitService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllMasters_ShouldReturnListOfMasters() {
        List<MasterUnit> mockMasters = Arrays.asList(new MasterUnit(), new MasterUnit());
        when(masterUnitRepository.findAll()).thenReturn(mockMasters);

        List<MasterUnit> result = masterUnitService.getAllMasters();

        assertEquals(mockMasters, result);
        verify(masterUnitRepository, times(1)).findAll();
    }

    @Test
    void saveMaster_ShouldInvokeSaveOnRepository() {
        MasterUnit master = new MasterUnit();

        masterUnitService.saveMaster(master);

        verify(masterUnitRepository, times(1)).save(master);
    }

    @Test
    void getMasterById_WhenMasterExists_ShouldReturnOptionalOfMaster() {
        MasterUnit master = new MasterUnit();
        when(masterUnitRepository.findById(1L)).thenReturn(Optional.of(master));

        Optional<MasterUnit> result = masterUnitService.getMasterById(1L);

        assertTrue(result.isPresent());
        assertEquals(master, result.get());
        verify(masterUnitRepository, times(1)).findById(1L);
    }

    @Test
    void getMasterById_WhenMasterDoesNotExist_ShouldReturnEmptyOptional() {
        when(masterUnitRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<MasterUnit> result = masterUnitService.getMasterById(1L);

        assertFalse(result.isPresent());
        verify(masterUnitRepository, times(1)).findById(1L);
    }

    @Test
    void deleteMaster_ShouldInvokeDeleteOnRepository() {
        masterUnitService.deleteMaster(1L);

        verify(masterUnitRepository, times(1)).deleteById(1L);
    }

    @Test
    void usernameExists_WhenUsernameExists_ShouldReturnTrue() {
        when(masterUnitRepository.findByUsername("existingUsername")).thenReturn(Optional.of(new MasterUnit()));

        boolean result = masterUnitService.usernameExists("existingUsername");

        assertTrue(result);
        verify(masterUnitRepository, times(1)).findByUsername("existingUsername");
    }

    @Test
    void usernameExists_WhenUsernameDoesNotExist_ShouldReturnFalse() {
        when(masterUnitRepository.findByUsername("nonexistentUsername")).thenReturn(Optional.empty());

        boolean result = masterUnitService.usernameExists("nonexistentUsername");

        assertFalse(result);
        verify(masterUnitRepository, times(1)).findByUsername("nonexistentUsername");
    }

    @Test
    void createMaster_WhenUsernameExists_ShouldThrowException() {
        when(userService.usernameExists("existingUsername")).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                masterUnitService.createMaster("name", "existingUsername", "password"));

        assertEquals("Username already exists", exception.getMessage());
        verify(userService, times(1)).usernameExists("existingUsername");
        verifyNoInteractions(masterUnitRepository);
    }

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

    @Test
    void updateMaster_WhenMasterDoesNotExist_ShouldThrowException() {
        when(masterUnitRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                masterUnitService.updateMaster(1L, "name", "username", "password"));

        assertEquals("Invalid Master ID: 1", exception.getMessage());
        verify(masterUnitRepository, times(1)).findById(1L);
    }
}
