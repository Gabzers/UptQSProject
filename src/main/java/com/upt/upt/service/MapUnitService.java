package com.upt.upt.service;

import com.upt.upt.entity.MapUnit;
import com.upt.upt.repository.MapUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for MapUnit.
 * 
 * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
 */
@Service
public class MapUnitService {

    @Autowired
    private MapUnitRepository mapUnitRepository;

    /**
     * Retrieve all MapUnits.
     * 
     * @return a list of all MapUnits
     */
    public List<MapUnit> getAllMapUnits() {
        return mapUnitRepository.findAll();
    }

    /**
     * Retrieve a MapUnit by its ID.
     * 
     * @param id the ID of the MapUnit
     * @return an Optional containing the MapUnit if found, or empty if not found
     */
    public Optional<MapUnit> getMapUnitById(Long id) {
        return mapUnitRepository.findById(id);
    }

    /**
     * Save a new MapUnit.
     * 
     * @param mapUnit the MapUnit to be saved
     * @return the saved MapUnit
     */
    public MapUnit saveMapUnit(MapUnit mapUnit) {
        return mapUnitRepository.save(mapUnit);
    }

    /**
     * Update an existing MapUnit.
     * 
     * @param id the ID of the MapUnit to be updated
     * @param mapUnit the MapUnit with updated information
     * @return the updated MapUnit, or null if the MapUnit with the given ID is not found
     */
    public MapUnit updateMapUnit(Long id, MapUnit mapUnit) {
        if (mapUnitRepository.existsById(id)) {
            mapUnit.setId(id);
            return mapUnitRepository.save(mapUnit);
        } else {
            return null;
        }
    }

    /**
     * Delete a MapUnit by its ID.
     * 
     * @param id the ID of the MapUnit to be deleted
     */
    public void deleteMapUnit(Long id) {
        mapUnitRepository.deleteById(id);
    }
}
