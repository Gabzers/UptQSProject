package com.upt.upt.service;

import com.upt.upt.entity.SemesterUnit;
import com.upt.upt.repository.SemesterUnitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for SemesterUnitService.
 */
class SemesterUnitServiceTest {

    @Mock
    private SemesterUnitRepository semesterUnitRepository;

    @InjectMocks
    private SemesterUnitService semesterUnitService;

    private SemesterUnit semesterUnit;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Instância de teste para SemesterUnit
        semesterUnit = new SemesterUnit();
        semesterUnit.setId(1L);
        semesterUnit.setStartDate("2024-01-01");
        semesterUnit.setEndDate("2024-06-30");
        semesterUnit.setExamPeriodStart("2024-06-01");
        semesterUnit.setExamPeriodEnd("2024-06-15");
        semesterUnit.setResitPeriodStart("2024-07-01");
        semesterUnit.setResitPeriodEnd("2024-07-15");
    }

    @Test
    void testSaveSemesterUnit() {
        // Configuração do mock para o método save
        when(semesterUnitRepository.save(semesterUnit)).thenReturn(semesterUnit);

        // Testando o método saveSemesterUnit
        SemesterUnit savedSemesterUnit = semesterUnitService.saveSemesterUnit(semesterUnit);

        assertNotNull(savedSemesterUnit);
        assertEquals(semesterUnit.getId(), savedSemesterUnit.getId());

        // Verifica se o método save foi chamado no repositório
        verify(semesterUnitRepository, times(1)).save(semesterUnit);
    }

    @Test
    void testGetSemesterUnitById() {
        // Configuração do mock para o método findById
        when(semesterUnitRepository.findById(1L)).thenReturn(Optional.of(semesterUnit));

        // Testando o método getSemesterUnitById
        Optional<SemesterUnit> foundSemesterUnit = semesterUnitService.getSemesterUnitById(1L);

        assertTrue(foundSemesterUnit.isPresent());
        assertEquals(semesterUnit.getId(), foundSemesterUnit.get().getId());

        // Verifica se o método findById foi chamado no repositório
        verify(semesterUnitRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteSemesterUnit() {
        // Testando o método deleteSemesterUnit
        semesterUnitService.deleteSemesterUnit(1L);

        // Verifica se o método deleteById foi chamado no repositório
        verify(semesterUnitRepository, times(1)).deleteById(1L);
    }
}
