package com.upt.upt.repository;

import com.upt.upt.entity.DirectorUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class DirectorUnitRepositoryTest {

    @Mock
    private DirectorUnitRepository directorUnitRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldFindByUsernameAndPassword() {
        // Dado
        DirectorUnit mockDirector = new DirectorUnit();
        mockDirector.setUsername("testuser");
        mockDirector.setPassword("testpassword");

        when(directorUnitRepository.findByUsernameAndPassword("testuser", "testpassword"))
                .thenReturn(mockDirector);
        DirectorUnit result = directorUnitRepository.findByUsernameAndPassword("testuser", "testpassword");
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("testuser");
        verify(directorUnitRepository, times(1)).findByUsernameAndPassword("testuser", "testpassword");
    }

    @Test
    void shouldReturnEmptyForInvalidUsername() {
        // Dado
        when(directorUnitRepository.findByUsername("nonexistent"))
                .thenReturn(Optional.empty());

        Optional<DirectorUnit> result = directorUnitRepository.findByUsername("nonexistent");
        assertThat(result).isEmpty();
        verify(directorUnitRepository, times(1)).findByUsername("nonexistent");
    }
}
