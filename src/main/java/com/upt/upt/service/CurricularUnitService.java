package com.upt.upt.service;

import com.upt.upt.entity.*;
import com.upt.upt.repository.CurricularUnitRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service class for managing CurricularUnit entities.
 * Provides basic CRUD operations.
 */

@Service
public class CurricularUnitService {

    private final CurricularUnitRepository curricularUnitRepository;
    private final CoordinatorUnitService coordinatorUnitService;

    @Autowired
    public CurricularUnitService(CurricularUnitRepository curricularUnitRepository, CoordinatorUnitService coordinatorUnitService) {
        this.curricularUnitRepository = curricularUnitRepository;
        this.coordinatorUnitService = coordinatorUnitService;
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

    public boolean validateEvaluationsCount(CurricularUnit uc, int evaluationsCount, String evaluationType, Model model) {
        if (uc != null && evaluationsCount < uc.getAssessments().size()) {
            model.addAttribute("uc", uc);
            model.addAttribute("error", "You must remove some evaluations before reducing the evaluations count.");
            return false;
        }

        if (evaluationType.equals("Mixed") && evaluationsCount < 2) {
            model.addAttribute("uc", uc);
            model.addAttribute("error", "For Mixed evaluation type, at least 2 evaluations are required.");
            return false;
        } else if (evaluationType.equals("Continuous") && evaluationsCount < 3) {
            model.addAttribute("uc", uc);
            model.addAttribute("error", "For Continuous evaluation type, at least 3 evaluations are required.");
            return false;
        }

        return true;
    }

    public void updateCurricularUnitFields(CurricularUnit uc, Map<String, String> params) {
        uc.setNameUC(params.get("ucName"));
        uc.setStudentsNumber(Integer.parseInt(params.get("ucNumStudents")));
        uc.setEvaluationType(params.get("ucEvaluationType"));
        uc.setAttendance(Boolean.parseBoolean(params.get("ucAttendance")));
        uc.setEvaluationsCount(Integer.parseInt(params.get("ucEvaluationsCount")));
        uc.setYear(Integer.parseInt(params.get("ucYear")));
        uc.setSemester(Integer.parseInt(params.get("ucSemester")));
    }

    public void updateCurricularUnitSemester(CurricularUnit uc, int semester, HttpSession session) {
        SemesterUnit oldSemesterUnit = uc.getSemesterUnit();
        oldSemesterUnit.removeCurricularUnit(uc);

        Long coordinatorId = (Long) session.getAttribute("userId");
        CoordinatorUnit coordinator = coordinatorUnitService.getCoordinatorById(coordinatorId)
                .orElseThrow(() -> new IllegalArgumentException("Coordinator not found"));
        DirectorUnit director = coordinator.getDirectorUnit();
        YearUnit currentYear = director.getCurrentYear();

        SemesterUnit newSemesterUnit = semester == 1 ? currentYear.getFirstSemester() : currentYear.getSecondSemester();
        newSemesterUnit.addCurricularUnit(uc);
        uc.setSemesterUnit(newSemesterUnit);
    }

    public CurricularUnit createCurricularUnit(Map<String, String> params, HttpSession session, Model model) {
        CurricularUnit curricularUnit = new CurricularUnit();
        curricularUnit.setNameUC(params.get("ucName"));
        curricularUnit.setStudentsNumber(Integer.parseInt(params.get("ucNumStudents")));
        curricularUnit.setEvaluationType(params.get("ucEvaluationType"));
        curricularUnit.setAttendance(Boolean.parseBoolean(params.get("ucAttendance")));
        curricularUnit.setEvaluationsCount(Integer.parseInt(params.get("ucEvaluationsCount")));
        curricularUnit.setYear(Integer.parseInt(params.get("ucYear")));
        curricularUnit.setSemester(Integer.parseInt(params.get("ucSemester")));

        Long coordinatorId = (Long) session.getAttribute("userId");
        Optional<CoordinatorUnit> coordinatorUnit = coordinatorUnitService.getCoordinatorById(coordinatorId);
        if (coordinatorUnit.isPresent()) {
            CoordinatorUnit coordinator = coordinatorUnit.get();
            coordinator.addCurricularUnit(curricularUnit);

            DirectorUnit director = coordinator.getDirectorUnit();
            YearUnit currentYear = director.getCurrentYear();
            if (currentYear != null) {
                SemesterUnit semesterUnit = curricularUnit.getSemester() == 1 ? currentYear.getFirstSemester() : currentYear.getSecondSemester();
                semesterUnit.addCurricularUnit(curricularUnit);
            } else {
                model.addAttribute("error", "Current year not found");
                return null;
            }
        } else {
            return null;
        }

        return curricularUnit;
    }
}
