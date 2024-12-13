package com.upt.upt.service;

import com.upt.upt.entity.CoordinatorUnit;
import com.upt.upt.entity.DirectorUnit;
import com.upt.upt.repository.CoordinatorUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing CoordinatorUnit entities.
 */
@Service
public class CoordinatorUnitService {

    private final CoordinatorUnitRepository coordinatorRepository;

    @Autowired
    private DirectorUnitService directorUnitService;

    @Autowired
    private UserService userService;

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

    /**
     * Checks if a CoordinatorUnit with the given username already exists.
     *
     * @param username The username to check
     * @return true if the username exists, false otherwise
     */
    public boolean usernameExists(String username) {
        return coordinatorRepository.findByUsername(username).isPresent();
    }

    /**
     * Saves a CoordinatorUnit with the associated DirectorUnit.
     *
     * @param coordinator The CoordinatorUnit to save
     * @param session The current HTTP session
     */
    public void saveCoordinatorWithDirector(CoordinatorUnit coordinator, HttpSession session) {
        if (userService.usernameExists(coordinator.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        Long directorId = (Long) session.getAttribute("userId");
        Optional<DirectorUnit> directorOpt = directorUnitService.getDirectorById(directorId);
        if (directorOpt.isPresent()) {
            DirectorUnit director = directorOpt.get();
            director.addCoordinator(coordinator);
            saveCoordinator(coordinator);
        } else {
            throw new IllegalArgumentException("Director not found");
        }
    }

    /**
     * Updates an existing CoordinatorUnit.
     *
     * @param id The ID of the CoordinatorUnit to update
     * @param coordinator The updated CoordinatorUnit data
     */
    public void updateCoordinator(Long id, CoordinatorUnit coordinator) {
        Optional<CoordinatorUnit> existingCoordinator = getCoordinatorById(id);
        if (existingCoordinator.isPresent()) {
            CoordinatorUnit updatedCoordinator = existingCoordinator.get();
            updatedCoordinator.setName(coordinator.getName());
            updatedCoordinator.setCourse(coordinator.getCourse());
            updatedCoordinator.setDuration(coordinator.getDuration());
            updatedCoordinator.setUsername(coordinator.getUsername());
            if (coordinator.getPassword() != null && !coordinator.getPassword().isEmpty()) {
                updatedCoordinator.setPassword(coordinator.getPassword());
            }
            saveCoordinator(updatedCoordinator);
        } else {
            throw new IllegalArgumentException("Coordinator not found");
        }
    }
}