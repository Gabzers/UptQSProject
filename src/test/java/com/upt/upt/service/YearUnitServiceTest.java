package com.upt.upt.service;

import com.upt.upt.entity.SemesterUnit;
import com.upt.upt.entity.YearUnit;
import com.upt.upt.repository.SemesterUnitRepository;
import com.upt.upt.repository.YearUnitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


/**
 * Test class for YearUnitServiceTest.
 * 
 * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
 */
class YearUnitServiceTest {

    @InjectMocks
    private YearUnitService yearUnitService;

    @Mock
    private YearUnitRepository yearUnitRepository;

    @Mock
    private SemesterUnitRepository semesterUnitRepository;

    @Mock
    private DirectorUnitService directorUnitService;

    @Mock
    private MapUnitService mapUnitService;

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
     * Tests the retrieval of all year units.
     * 
     * 
     */
    @Test
    @DisplayName("Test getAllYearUnits")
    void testGetAllYearUnits() {
        // Arrange
        YearUnit yearUnit1 = new YearUnit();
        YearUnit yearUnit2 = new YearUnit();
        when(yearUnitRepository.findAll()).thenReturn(Arrays.asList(yearUnit1, yearUnit2));

        // Act
        List<YearUnit> result = yearUnitService.getAllYearUnits();

        // Assert
        assertThat(result).hasSize(2);
        verify(yearUnitRepository, times(1)).findAll();
    }

    /**
     * Tests the retrieval of a year unit by its ID.
     * 
     * 
     */
    @Test
    @DisplayName("Test getYearUnitById")
    void testGetYearUnitById() {
        // Arrange
        YearUnit yearUnit = new YearUnit();
        when(yearUnitRepository.findById(1L)).thenReturn(Optional.of(yearUnit));

        // Act
        Optional<YearUnit> result = yearUnitService.getYearUnitById(1L);

        // Assert
        assertThat(result).isPresent().contains(yearUnit);
        verify(yearUnitRepository, times(1)).findById(1L);
    }

    /**
     * Tests the saving of a year unit.
     * 
     * 
     */
    @Test
    @DisplayName("Test saveYearUnit")
    void testSaveYearUnit() {
        // Arrange
        YearUnit yearUnit = new YearUnit();
        when(yearUnitRepository.save(yearUnit)).thenReturn(yearUnit);

        // Act
        YearUnit result = yearUnitService.saveYearUnit(yearUnit);

        // Assert
        assertThat(result).isEqualTo(yearUnit);
        verify(yearUnitRepository, times(1)).save(yearUnit);
    }

    /**
     * Tests the deletion of a year unit by its ID.
     * 
     * 
     */
    @Test
    @DisplayName("Test deleteYearUnit")
    void testDeleteYearUnit() {
        // Act
        yearUnitService.deleteYearUnit(1L);

        // Assert
        verify(yearUnitRepository, times(1)).deleteById(1L);
    }

    /**
     * Tests the retrieval of the most recent year unit.
     * 
     * 
     */
    @Test
    @DisplayName("Test getMostRecentYearUnit")
    void testGetMostRecentYearUnit() {
        // Arrange
        YearUnit yearUnit = new YearUnit();
        when(yearUnitRepository.findTopByOrderByIdDesc()).thenReturn(Optional.of(yearUnit));

        // Act
        Optional<YearUnit> result = yearUnitService.getMostRecentYearUnit();

        // Assert
        assertThat(result).isPresent().contains(yearUnit);
        verify(yearUnitRepository, times(1)).findTopByOrderByIdDesc();
    }

    /**
     * Tests the retrieval of the most recent year unit by director ID.
     * 
     */
    @Test
    @DisplayName("Test getMostRecentYearUnitByDirector")
    void testGetMostRecentYearUnitByDirector() {
        // Arrange
        YearUnit yearUnit = new YearUnit();
        when(yearUnitRepository.findTopByDirectorUnitIdOrderByIdDesc(1L)).thenReturn(Optional.of(yearUnit));

        // Act
        Optional<YearUnit> result = yearUnitService.getMostRecentYearUnitByDirector(1L);

        // Assert
        assertThat(result).isPresent().contains(yearUnit);
        verify(yearUnitRepository, times(1)).findTopByDirectorUnitIdOrderByIdDesc(1L);
    }

    /**
     * Tests the update of a semester unit.
     * 
     * 
     */
    @Test
    @DisplayName("Test updateSemester")
    void testUpdateSemester() {
        // Arrange
        SemesterUnit semesterUnit = new SemesterUnit();
        semesterUnit.setStartDate("2023-09-01");
        semesterUnit.setEndDate("2023-12-31");
        semesterUnit.setExamPeriodStart("2023-10-01");
        semesterUnit.setExamPeriodEnd("2023-10-15");
        semesterUnit.setResitPeriodStart("2023-11-01");
        semesterUnit.setResitPeriodEnd("2023-11-15");

        when(semesterUnitRepository.save(any(SemesterUnit.class))).thenReturn(semesterUnit);

        // Act
        SemesterUnit result = yearUnitService.updateSemester(
                semesterUnit,
                "2023-09-01",
                "2023-12-31",
                "2023-10-01",
                "2023-10-15",
                "2023-11-01",
                "2023-11-15"
        );

        // Assert
        assertThat(result).isEqualTo(semesterUnit);
        verify(semesterUnitRepository, times(1)).save(any(SemesterUnit.class));
    }

    /**
     * Tests the retrieval of the current year unit by director ID.
     * 
     * 
     */
    @Test
    @DisplayName("Test getCurrentYearUnitByDirector")
    void testGetCurrentYearUnitByDirector() {
        // Arrange
        SemesterUnit semester = new SemesterUnit();
        semester.setStartDate("2023-01-01");
        semester.setEndDate("2023-06-01");

        YearUnit yearUnit = new YearUnit();
        yearUnit.setFirstSemester(semester);

        when(yearUnitRepository.findByDirectorUnitId(1L)).thenReturn(Collections.singletonList(yearUnit));

        // Act
        Optional<YearUnit> result = yearUnitService.getCurrentYearUnitByDirector(1L);

        // Assert
        assertThat(result).isPresent().contains(yearUnit);
        verify(yearUnitRepository, times(1)).findByDirectorUnitId(1L);
    }
}
