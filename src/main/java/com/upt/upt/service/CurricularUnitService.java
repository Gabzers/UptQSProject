package com.upt.upt.service;

import com.upt.upt.entity.CurricularUnit;
import com.upt.upt.repository.CurricularUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing CurricularUnit entities.
 * Provides basic CRUD operations.
 */

@Service
public class CurricularUnitService {

    private final CurricularUnitRepository curricularUnitRepository;

    @Autowired
    public CurricularUnitService(CurricularUnitRepository curricularUnitRepository) {
        this.curricularUnitRepository = curricularUnitRepository;
    }

    /**
     * Save a curricular unit.
     *
     * @param curricularUnit the entity to save
     * @return the persisted entity
     */
    public CurricularUnit saveCurricularUnit(CurricularUnit curricularUnit) {
        return curricularUnitRepository.save(curricularUnit);
    }

    /**
     * Get all the curricular units.
     *
     * @return the list of entities
     */
    public List<CurricularUnit> getAllCurricularUnits() {
        return curricularUnitRepository.findAll();
    }

    /**
     * Get one curricular unit by ID.
     *
     * @param id the ID of the entity
     * @return the entity as an Optional
     */
    public Optional<CurricularUnit> getCurricularUnitById(Long id) {
        return curricularUnitRepository.findById(id);
    }

    /**
     * Update a curricular unit.
     *
     * @param id the ID of the entity to update
     * @param updatedCurricularUnit the updated entity
     * @return the updated entity
     */
    public CurricularUnit updateCurricularUnit(Long id, CurricularUnit updatedCurricularUnit) {
        Optional<CurricularUnit> existingCurricularUnit = curricularUnitRepository.findById(id);
        if (existingCurricularUnit.isPresent()) {
            CurricularUnit curricularUnit = existingCurricularUnit.get();
            curricularUnit.setNameUC(updatedCurricularUnit.getNameUC());
            curricularUnit.setStudentsNumber(updatedCurricularUnit.getStudentsNumber());
            curricularUnit.setEvaluationType(updatedCurricularUnit.getEvaluationType());
            curricularUnit.setAttendance(updatedCurricularUnit.getAttendance()); // Atualizar o campo attendance
            curricularUnit.setEvaluationsCount(updatedCurricularUnit.getEvaluationsCount());
            curricularUnit.setYear(updatedCurricularUnit.getYear());
            curricularUnit.setSemester(updatedCurricularUnit.getSemester());
            // curricularUnit.setAssessments(updatedCurricularUnit.getAssessments());
            return curricularUnitRepository.save(curricularUnit);
        } else {
            throw new RuntimeException("Curricular unit not found");
        }
    }

    /**
     * Delete the curricular unit by ID.
     *
     * @param id the ID of the entity to delete
     */
    public void deleteCurricularUnit(Long id) {
        curricularUnitRepository.deleteById(id);
    }
}
