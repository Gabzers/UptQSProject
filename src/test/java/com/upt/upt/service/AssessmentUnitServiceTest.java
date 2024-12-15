package com.upt.upt.service;

import com.upt.upt.entity.AssessmentUnit;
import com.upt.upt.entity.RoomUnit;
import com.upt.upt.repository.AssessmentUnitRepository;
import com.upt.upt.repository.RoomUnitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Test class for AssessmentUnitService.
 * 
 * @autor Grupo 5 - 47719, 47713, 46697, 47752, 47004
 */
public class AssessmentUnitServiceTest {

    @Mock
    private AssessmentUnitRepository assessmentUnitRepository;

    @Mock
    private RoomUnitRepository roomUnitRepository;

    @InjectMocks
    private AssessmentUnitService assessmentUnitService;

    private AssessmentUnit assessmentUnit;

    /**
     * Set up test data.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        assessmentUnit = new AssessmentUnit();
        assessmentUnit.setId(1L);
        assessmentUnit.setType("Exam");
        assessmentUnit.setWeight(50);
    }

    /**
     * Test getAssessmentsByCurricularUnit method.
     */
    @Test
    public void testGetAssessmentsByCurricularUnit() {
        when(assessmentUnitRepository.findByCurricularUnitId(1L)).thenReturn(List.of(assessmentUnit));

        List<AssessmentUnit> result = assessmentUnitService.getAssessmentsByCurricularUnit(1L);

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getId()).isEqualTo(1L);
        verify(assessmentUnitRepository, times(1)).findByCurricularUnitId(1L);
    }

    /**
     * Test getAssessmentByUnitAndId method.
     */
    @Test
    public void testGetAssessmentByUnitAndId() {
        when(assessmentUnitRepository.findByCurricularUnitIdAndId(1L, 1L)).thenReturn(Optional.of(assessmentUnit));

        Optional<AssessmentUnit> result = assessmentUnitService.getAssessmentByUnitAndId(1L, 1L);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        verify(assessmentUnitRepository, times(1)).findByCurricularUnitIdAndId(1L, 1L);
    }

    /**
     * Test saveAssessment method.
     */
    @Test
    public void testSaveAssessment() {
        RoomUnit room = new RoomUnit();
        room.setId(1L);
        when(roomUnitRepository.findAllById(List.of(1L))).thenReturn(List.of(room));
        when(assessmentUnitRepository.save(any(AssessmentUnit.class))).thenReturn(assessmentUnit);

        AssessmentUnit result = assessmentUnitService.saveAssessment(assessmentUnit, List.of(1L));

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(roomUnitRepository, times(1)).findAllById(List.of(1L));
        verify(assessmentUnitRepository, times(1)).save(assessmentUnit);
    }

    /**
     * Test updateAssessment method.
     */
    @Test
    public void testUpdateAssessment() {
        AssessmentUnit updatedAssessment = new AssessmentUnit();
        updatedAssessment.setType("Updated Type");
        updatedAssessment.setWeight(75);

        when(assessmentUnitRepository.findById(1L)).thenReturn(Optional.of(assessmentUnit));
        when(assessmentUnitRepository.save(any(AssessmentUnit.class))).thenReturn(updatedAssessment);

        AssessmentUnit result = assessmentUnitService.updateAssessment(1L, updatedAssessment);

        assertThat(result).isNotNull();
        assertThat(result.getType()).isEqualTo("Updated Type");
        assertThat(result.getWeight()).isEqualTo(75);
        verify(assessmentUnitRepository, times(1)).findById(1L);
        verify(assessmentUnitRepository, times(1)).save(assessmentUnit);
    }

    /**
     * Test deleteAssessment method.
     */
    @Test
    public void testDeleteAssessment() {
        when(assessmentUnitRepository.findByCurricularUnitIdAndId(1L, 1L)).thenReturn(Optional.of(assessmentUnit));

        assessmentUnitService.deleteAssessment(1L, 1L);

        verify(assessmentUnitRepository, times(1)).delete(assessmentUnit);
    }

    /**
     * Test isRoomAvailable method.
     */
    @Test
    public void testIsRoomAvailable() {
        LocalDateTime startTime = LocalDateTime.of(2024, 12, 15, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2024, 12, 15, 12, 0);

        when(assessmentUnitRepository.findByRooms_Id(1L)).thenReturn(Arrays.asList(assessmentUnit));

        boolean result = assessmentUnitService.isRoomAvailable(1L, startTime, endTime);

        assertThat(result).isTrue(); // Adjust logic if there's overlap
        verify(assessmentUnitRepository, times(1)).findByRooms_Id(1L);
    }

    /**
     * Test areRoomsAvailable method.
     */
    @Test
    public void testAreRoomsAvailable() {
        LocalDateTime startTime = LocalDateTime.of(2024, 12, 15, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2024, 12, 15, 12, 0);

        when(assessmentUnitRepository.findByRooms_Id(anyLong())).thenReturn(List.of());

        boolean result = assessmentUnitService.areRoomsAvailable(List.of(1L, 2L), startTime, endTime);

        assertThat(result).isTrue();
        verify(assessmentUnitRepository, times(2)).findByRooms_Id(anyLong());
    }
}
