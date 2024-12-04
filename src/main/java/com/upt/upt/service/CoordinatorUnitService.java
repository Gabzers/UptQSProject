package com.upt.upt.service;

import com.upt.upt.entity.CoordinatorUnit;
import com.upt.upt.repository.CoordinatorUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing CoordinatorUnit entities.
 */
@Service
public class CoordinatorUnitService {

    private final CoordinatorUnitRepository coordinatorRepository;

    @Autowired
    public CoordinatorUnitService(CoordinatorUnitRepository coordinatorRepository) {
        this.coordinatorRepository = coordinatorRepository;
    }

    /**
     * Retrieves all CoordinatorUnit entities.
     *
     * @return List of CoordinatorUnit
     */
    public List<CoordinatorUnit> getAllCoordinators() {
        return coordinatorRepository.findAll();
    }

    /**
     * Retrieves a CoordinatorUnit by its ID.
     *
     * @param id The ID of the CoordinatorUnit
     * @return Optional containing the CoordinatorUnit if found
     */
    public Optional<CoordinatorUnit> getCoordinatorById(Long id) {
        return coordinatorRepository.findById(id);
    }

    /**
     * Saves a new or existing CoordinatorUnit entity.
     *
     * @param coordinator The CoordinatorUnit to save
     * @return The saved CoordinatorUnit
     */
    public CoordinatorUnit saveCoordinator(CoordinatorUnit coordinator) {
        return coordinatorRepository.save(coordinator);
    }

    /**
     * Deletes a CoordinatorUnit by its ID.
     *
     * @param id The ID of the CoordinatorUnit to delete
     */
    public void deleteCoordinator(Long id) {
        coordinatorRepository.deleteById(id);
    }
}