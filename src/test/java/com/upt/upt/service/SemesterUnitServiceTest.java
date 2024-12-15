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
 * 
 * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
 */
class SemesterUnitServiceTest {

    @Mock
    private SemesterUnitRepository semesterUnitRepository;

    @InjectMocks
    private SemesterUnitService semesterUnitService;

    private SemesterUnit semesterUnit;

    /**
     * Sets up the test environment before each test.
     * Initializes mocks and creates a test instance of SemesterUnit.
     * 
     * 
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        semesterUnit = new SemesterUnit();
        semesterUnit.setId(1L);
        semesterUnit.setStartDate("2024-01-01");
        semesterUnit.setEndDate("2024-06-30");
        semesterUnit.setExamPeriodStart("2024-06-01");
        semesterUnit.setExamPeriodEnd("2024-06-15");
        semesterUnit.setResitPeriodStart("2024-07-01");
        semesterUnit.setResitPeriodEnd("2024-07-15");
    }

    /**
     * Tests the saving of a semester unit.
     * 
     * 
     */
    @Test
    void testSaveSemesterUnit() {
        when(semesterUnitRepository.save(semesterUnit)).thenReturn(semesterUnit);

        SemesterUnit savedSemesterUnit = semesterUnitService.saveSemesterUnit(semesterUnit);

        assertNotNull(savedSemesterUnit);
        assertEquals(semesterUnit.getId(), savedSemesterUnit.getId());

        verify(semesterUnitRepository, times(1)).save(semesterUnit);
    }

    /**
     * Tests the retrieval of a semester unit by its ID.
     * 
     * 
     */
    @Test
    void testGetSemesterUnitById() {
        when(semesterUnitRepository.findById(1L)).thenReturn(Optional.of(semesterUnit));

        Optional<SemesterUnit> foundSemesterUnit = semesterUnitService.getSemesterUnitById(1L);

        assertTrue(foundSemesterUnit.isPresent());
        assertEquals(semesterUnit.getId(), foundSemesterUnit.get().getId());

        verify(semesterUnitRepository, times(1)).findById(1L);
    }

    /**
     * Tests the deletion of a semester unit by its ID.
     * 
     * 
     */
    @Test
    void testDeleteSemesterUnit() {
        semesterUnitService.deleteSemesterUnit(1L);

        verify(semesterUnitRepository, times(1)).deleteById(1L);
    }
}
