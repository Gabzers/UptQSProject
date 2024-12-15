package com.upt.upt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.upt.upt.entity.MasterUnit;
import com.upt.upt.entity.DirectorUnit;
import com.upt.upt.entity.CoordinatorUnit;
import com.upt.upt.repository.MasterUnitRepository;
import com.upt.upt.repository.DirectorUnitRepository;
import com.upt.upt.repository.CoordinatorUnitRepository;
import com.upt.upt.entity.UserType;

import java.util.Optional;

/**
 * Service class for managing user authentication and retrieval.
 * 
 * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
 */
@Service
public class UserService {

    @Autowired
    private MasterUnitRepository masterUnitRepository;

    @Autowired
    private DirectorUnitRepository directorUnitRepository;

    @Autowired
    private CoordinatorUnitRepository coordinatorUnitRepository;

    /**
     * Validates the user credentials and returns the user type.
     * 
     * @param username the username entered by the user
     * @param password the password entered by the user
     * @return the user type if credentials are valid, null otherwise
     */
    public UserType validateUser(String username, String password) {
        MasterUnit master = masterUnitRepository.findByUsernameAndPassword(username, password);
        if (master != null) {
            return UserType.MASTER;
        }

        DirectorUnit director = directorUnitRepository.findByUsernameAndPassword(username, password);
        if (director != null) {
            return UserType.DIRECTOR;
        }

        CoordinatorUnit coordinator = coordinatorUnitRepository.findByUsernameAndPassword(username, password);
        if (coordinator != null) {
            return UserType.COORDINATOR;
        }

        return null;
    }

    /**
     * Retrieves the user ID by username and user type.
     * 
     * @param username the username of the user
     * @param userType the type of the user
     * @return the user ID if found, null otherwise
     */
    public Long getUserIdByUsername(String username, UserType userType) {
        switch (userType) {
            case MASTER:
                return masterUnitRepository.findByUsername(username)
                        .map(MasterUnit::getId)
                        .orElse(null);
            case DIRECTOR:
                return directorUnitRepository.findByUsername(username)
                        .map(DirectorUnit::getId)
                        .orElse(null);
            case COORDINATOR:
                return coordinatorUnitRepository.findByUsername(username)
                        .map(CoordinatorUnit::getId)
                        .orElse(null);
            default:
                return null;
        }
    }

    /**
     * Retrieves a DirectorUnit by its ID.
     * 
     * @param id the ID of the DirectorUnit
     * @return an Optional containing the DirectorUnit if found
     */
    public Optional<DirectorUnit> getDirectorById(Long id) {
        return directorUnitRepository.findById(id);
    }

    /**
     * Checks if a username already exists in any user repository.
     * 
     * @param username the username to check
     * @return true if the username exists, false otherwise
     */
    public boolean usernameExists(String username) {
        return masterUnitRepository.findByUsername(username).isPresent() ||
               directorUnitRepository.findByUsername(username).isPresent() ||
               coordinatorUnitRepository.findByUsername(username).isPresent();
    }
}