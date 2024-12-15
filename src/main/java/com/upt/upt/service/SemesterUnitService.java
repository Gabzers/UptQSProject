package com.upt.upt.service;

import com.upt.upt.entity.SemesterUnit;
import com.upt.upt.repository.SemesterUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class for managing SemesterUnit entities.
 * 
 * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
 */
@Service
public class SemesterUnitService {

    @Autowired
    private SemesterUnitRepository semesterUnitRepository;

    /**
     * Saves a SemesterUnit entity.
     * 
     * @param semesterUnit the SemesterUnit to save
     * @return the saved SemesterUnit
     */
    public SemesterUnit saveSemesterUnit(SemesterUnit semesterUnit) {
        return semesterUnitRepository.save(semesterUnit);
    }

    /**
     * Retrieves a SemesterUnit by its ID.
     * 
     * @param id the ID of the SemesterUnit
     * @return an Optional containing the SemesterUnit if found
     */
    public Optional<SemesterUnit> getSemesterUnitById(Long id) {
        return semesterUnitRepository.findById(id);
    }

    /**
     * Deletes a SemesterUnit by its ID.
     * 
     * @param id the ID of the SemesterUnit to delete
     */
    public void deleteSemesterUnit(Long id) {
        semesterUnitRepository.deleteById(id);
    }
}
