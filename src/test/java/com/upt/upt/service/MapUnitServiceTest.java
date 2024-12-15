package com.upt.upt.service;

import com.upt.upt.entity.MapUnit;
import com.upt.upt.repository.MapUnitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MapUnitServiceTest {

    @Mock
    private MapUnitRepository mapUnitRepository;

    @InjectMocks
    private MapUnitService mapUnitService;

    private MapUnit mapUnit;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mapUnit = new MapUnit();
        mapUnit.setId(1L);
    }

    @Test
    public void testGetAllMapUnits() {
        when(mapUnitRepository.findAll()).thenReturn(Arrays.asList(mapUnit));

        List<MapUnit> mapUnits = mapUnitService.getAllMapUnits();

        assertThat(mapUnits).isNotEmpty();
        assertThat(mapUnits).hasSize(1);
        verify(mapUnitRepository, times(1)).findAll();
    }

    @Test
    public void testGetMapUnitById() {
        when(mapUnitRepository.findById(1L)).thenReturn(Optional.of(mapUnit));

        Optional<MapUnit> foundMapUnit = mapUnitService.getMapUnitById(1L);

        assertThat(foundMapUnit).isPresent();
        assertThat(foundMapUnit.get().getId()).isEqualTo(1L);
        verify(mapUnitRepository, times(1)).findById(1L);
    }

    @Test
    public void testSaveMapUnit() {
        when(mapUnitRepository.save(any(MapUnit.class))).thenReturn(mapUnit);

        MapUnit savedMapUnit = mapUnitService.saveMapUnit(mapUnit);

        assertThat(savedMapUnit).isNotNull();
        assertThat(savedMapUnit.getId()).isEqualTo(1L);
        verify(mapUnitRepository, times(1)).save(mapUnit);
    }

    @Test
    public void testUpdateMapUnit() {
        when(mapUnitRepository.existsById(1L)).thenReturn(true);
        when(mapUnitRepository.save(any(MapUnit.class))).thenReturn(mapUnit);

        MapUnit updatedMapUnit = mapUnitService.updateMapUnit(1L, mapUnit);

        assertThat(updatedMapUnit).isNotNull();
        assertThat(updatedMapUnit.getId()).isEqualTo(1L);
        verify(mapUnitRepository, times(1)).existsById(1L);
        verify(mapUnitRepository, times(1)).save(mapUnit);
    }

    @Test
    public void testUpdateMapUnitNotFound() {
        when(mapUnitRepository.existsById(1L)).thenReturn(false);

        MapUnit updatedMapUnit = mapUnitService.updateMapUnit(1L, mapUnit);

        assertThat(updatedMapUnit).isNull();
        verify(mapUnitRepository, times(1)).existsById(1L);
        verify(mapUnitRepository, times(0)).save(mapUnit);
    }

    @Test
    public void testDeleteMapUnit() {
        doNothing().when(mapUnitRepository).deleteById(1L);

        mapUnitService.deleteMapUnit(1L);

        verify(mapUnitRepository, times(1)).deleteById(1L);
    }
}
