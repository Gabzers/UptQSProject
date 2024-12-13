package com.upt.upt.service;

import com.upt.upt.entity.DirectorUnit;
import com.upt.upt.entity.AssessmentUnit;
import com.upt.upt.entity.YearUnit;
import com.upt.upt.repository.DirectorUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
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
     * @param params the updated entity parameters
     * @return the updated entity
     */
    public DirectorUnit updateDirector(Long id, Map<String, String> params) {
        DirectorUnit director = getDirectorById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Director ID: " + id));

        director.setName(params.get("director-name"));
        director.setDepartment(params.get("director-department"));
        director.setUsername(params.get("director-username"));
        if (params.get("director-password") != null && !params.get("director-password").isEmpty()) {
            director.setPassword(params.get("director-password"));
        }

        return director;
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

    public boolean noAssessmentsForPeriod(List<AssessmentUnit> assessments, String period) {
        return assessments.stream().noneMatch(a -> period.equals(a.getExamPeriod()));
    }

    public DirectorUnit createDirector(Map<String, String> params) {
        DirectorUnit newDirector = new DirectorUnit();
        newDirector.setName(params.get("director-name"));
        newDirector.setDepartment(params.get("director-department"));
        newDirector.setUsername(params.get("director-username"));
        newDirector.setPassword(params.get("director-password"));
        return newDirector;
    }
}
