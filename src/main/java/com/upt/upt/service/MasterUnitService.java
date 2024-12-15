package com.upt.upt.service;

import com.upt.upt.entity.MasterUnit;
import com.upt.upt.repository.MasterUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing MasterUnit entities.
 * 
 * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
 */
@Service
public class MasterUnitService {

    @Autowired
    private MasterUnitRepository masterUnitRepository;

    @Autowired
    private UserService userService;

    /**
     * Fetches all MasterUnit entities from the database.
     *
     * @return A list of MasterUnit entities
     */
    public List<MasterUnit> getAllMasters() {
        return masterUnitRepository.findAll();
    }

    /**
     * Saves a MasterUnit entity to the database.
     *
     * @param masterUnit The MasterUnit entity to save
     */
    public void saveMaster(MasterUnit masterUnit) {
        masterUnitRepository.save(masterUnit);
    }

    /**
     * Fetches a MasterUnit entity by its ID.
     *
     * @param id The ID of the MasterUnit
     * @return An Optional containing the MasterUnit if found
     */
    public Optional<MasterUnit> getMasterById(Long id) {
        return masterUnitRepository.findById(id);
    }

    /**
     * Deletes a MasterUnit entity by its ID.
     *
     * @param id The ID of the MasterUnit to delete
     */
    public void deleteMaster(Long id) {
        masterUnitRepository.deleteById(id);
    }

    /**
     * Checks if a MasterUnit with the given username already exists.
     *
     * @param username The username to check
     * @return true if the username exists, false otherwise
     */
    public boolean usernameExists(String username) {
        return masterUnitRepository.findByUsername(username).isPresent();
    }

    /**
     * Creates a new MasterUnit entity.
     *
     * @param name The name of the master unit
     * @param username The username for the master unit
     * @param password The password for the master unit
     * @return The created MasterUnit entity
     */
    public MasterUnit createMaster(String name, String username, String password) {
        if (userService.usernameExists(username)) {
            throw new IllegalArgumentException("Username already exists");
        }
        MasterUnit newMaster = new MasterUnit();
        newMaster.setName(name);
        newMaster.setUsername(username);
        newMaster.setPassword(password);
        return newMaster;
    }

    /**
     * Updates an existing MasterUnit entity.
     *
     * @param id The ID of the master unit to be updated
     * @param name The updated name of the master unit
     * @param username The updated username of the master unit
     * @param password The updated password of the master unit
     * @return The updated MasterUnit entity
     */
    public MasterUnit updateMaster(Long id, String name, String username, String password) {
        MasterUnit master = getMasterById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Master ID: " + id));

        master.setName(name);
        master.setUsername(username);
        if (password != null && !password.isEmpty()) {
            master.setPassword(password);
        }

        return master;
    }
}
