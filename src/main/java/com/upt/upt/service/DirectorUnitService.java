package com.upt.upt.service;

import com.upt.upt.entity.DirectorUnit;
import com.upt.upt.entity.YearUnit;
import com.upt.upt.repository.DirectorUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

/**
 * Service class for managing DirectorUnit entities.
 * Provides basic CRUD operations for DirectorUnit.
 */
@Service
public class DirectorUnitService {

    private final DirectorUnitRepository directorUnitRepository;

    @Autowired
    public DirectorUnitService(DirectorUnitRepository directorUnitRepository) {
        this.directorUnitRepository = directorUnitRepository;
    }

    /**
     * Save a director unit.
     *
     * @param directorUnit the entity to save
     * @return the persisted entity
     */
    public DirectorUnit saveDirector(DirectorUnit directorUnit) {
        return directorUnitRepository.save(directorUnit);
    }

    /**
     * Get all the director units.
     *
     * @return the list of entities
     */
    public List<DirectorUnit> getAllDirectors() {
        return directorUnitRepository.findAll();
    }

    /**
     * Get one director unit by ID.
     *
     * @param id the ID of the entity
     * @return the entity as an Optional
     */
    public Optional<DirectorUnit> getDirectorById(Long id) {
        return directorUnitRepository.findById(id);
    }

    /**
     * Update a director unit.
     *
     * @param id the ID of the entity to update
     * @param updatedDirectorUnit the updated entity
     * @return the updated entity
     */
    public DirectorUnit updateDirector(Long id, DirectorUnit updatedDirectorUnit) {
        Optional<DirectorUnit> existingDirectorUnit = directorUnitRepository.findById(id);
        if (existingDirectorUnit.isPresent()) {
            DirectorUnit directorUnit = existingDirectorUnit.get();
            directorUnit.setName(updatedDirectorUnit.getName());
            directorUnit.setDepartment(updatedDirectorUnit.getDepartment());
            directorUnit.setUsername(updatedDirectorUnit.getUsername());
            directorUnit.setPassword(updatedDirectorUnit.getPassword());
            return directorUnitRepository.save(directorUnit);
        } else {
            throw new RuntimeException("Director unit not found");
        }
    }

    /**
     * Delete a director unit.
     *
     * @param id the ID of the director to delete
     */
    public void deleteDirector(Long id) {
        directorUnitRepository.deleteById(id); // Remove o diretor pelo ID
    }

    /**
     * Get the most recent year created by the director.
     *
     * @param director the director entity
     * @return the most recent year as a YearUnit
     */
    public YearUnit getMostRecentYear(DirectorUnit director) {
        return director.getPastYears().stream()
            .max(Comparator.comparing(y -> LocalDate.parse(y.getFirstSemester().getStartDate())))
            .orElse(null);
    }
}
