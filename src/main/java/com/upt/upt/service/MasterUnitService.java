package com.upt.upt.service;

import com.upt.upt.entity.MasterUnit;
import com.upt.upt.repository.MasterUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MasterUnitService {

    @Autowired
    private MasterUnitRepository masterUnitRepository;

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
}
