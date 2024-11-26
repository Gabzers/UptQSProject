package com.upt.upt.service;

import com.upt.upt.entity.MapUnit;
import com.upt.upt.repository.MapUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for MapUnit.
 */
@Service
public class MapUnitService {

    @Autowired
    private MapUnitRepository mapUnitRepository;

    // Buscar todos os MapUnits
    public List<MapUnit> getAllMapUnits() {
        return mapUnitRepository.findAll();
    }

    // Buscar um MapUnit pelo ID
    public Optional<MapUnit> getMapUnitById(Long id) {
        return mapUnitRepository.findById(id);
    }

    // Salvar um novo MapUnit
    public MapUnit saveMapUnit(MapUnit mapUnit) {
        return mapUnitRepository.save(mapUnit);
    }

    // Atualizar um MapUnit existente
    public MapUnit updateMapUnit(Long id, MapUnit mapUnit) {
        if (mapUnitRepository.existsById(id)) {
            mapUnit.setId(id);
            return mapUnitRepository.save(mapUnit);
        } else {
            return null;  // Se não encontrar o MapUnit com o ID, retorna null ou você pode lançar uma exceção
        }
    }

    // Deletar um MapUnit
    public void deleteMapUnit(Long id) {
        mapUnitRepository.deleteById(id);
    }
}
