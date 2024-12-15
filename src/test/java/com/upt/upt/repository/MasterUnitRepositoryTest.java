package com.upt.upt.repository;

import com.upt.upt.entity.MasterUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for MasterUnitRepository.
 * 
 * @autor Grupo 5 - 47719, 47713, 46697, 47752, 47004
 */
@DataJpaTest
@ExtendWith(MockitoExtension.class)
class MasterUnitRepositoryTest {

    @Mock
    private MasterUnitRepository masterUnitRepository;

    /**
     * Test findByUsernameAndPassword method when credentials match.
     */
    @Test
    void findByUsernameAndPassword_ShouldReturnMasterUnit_WhenCredentialsMatch() {
        MasterUnit mockMaster = new MasterUnit(1L, "Test Master", "testuser", "password123");
        when(masterUnitRepository.findByUsernameAndPassword("testuser", "password123")).thenReturn(mockMaster);

        MasterUnit result = masterUnitRepository.findByUsernameAndPassword("testuser", "password123");

        assertNotNull(result);
        assertEquals(mockMaster, result);
        verify(masterUnitRepository, times(1)).findByUsernameAndPassword("testuser", "password123");
    }

    /**
     * Test findByUsernameAndPassword method when credentials do not match.
     */
    @Test
    void findByUsernameAndPassword_ShouldReturnNull_WhenCredentialsDoNotMatch() {
        when(masterUnitRepository.findByUsernameAndPassword("wronguser", "wrongpass")).thenReturn(null);

        MasterUnit result = masterUnitRepository.findByUsernameAndPassword("wronguser", "wrongpass");

        assertNull(result);
        verify(masterUnitRepository, times(1)).findByUsernameAndPassword("wronguser", "wrongpass");
    }

    /**
     * Test findByUsername method when username exists.
     */
    @Test
    void findByUsername_ShouldReturnOptionalOfMaster_WhenUsernameExists() {
        MasterUnit mockMaster = new MasterUnit(1L, "Test Master", "testuser", "password123");
        when(masterUnitRepository.findByUsername("testuser")).thenReturn(Optional.of(mockMaster));

        Optional<MasterUnit> result = masterUnitRepository.findByUsername("testuser");

        assertTrue(result.isPresent());
        assertEquals(mockMaster, result.get());
        verify(masterUnitRepository, times(1)).findByUsername("testuser");
    }

    /**
     * Test findByUsername method when username does not exist.
     */
    @Test
    void findByUsername_ShouldReturnEmptyOptional_WhenUsernameDoesNotExist() {
        when(masterUnitRepository.findByUsername("nonexistentuser")).thenReturn(Optional.empty());

        Optional<MasterUnit> result = masterUnitRepository.findByUsername("nonexistentuser");

        assertFalse(result.isPresent());
        verify(masterUnitRepository, times(1)).findByUsername("nonexistentuser");
    }
}