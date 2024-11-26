package com.upt.upt.service;

import com.upt.upt.entity.AssessmentUnit;
import com.upt.upt.repository.AssessmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing Assessment entities.
 * Provides basic CRUD operations.
 */
@Service
public class AssessmentService {

    private final AssessmentRepository assessmentRepository;

    @Autowired
    public AssessmentService(AssessmentRepository assessmentRepository) {
        this.assessmentRepository = assessmentRepository;
    }

    /**
     * Get all assessments for a specific curricular unit.
     *
     * @param curricularUnitId the ID of the curricular unit
     * @return a list of assessments
     */
    public List<AssessmentUnit> getAssessmentsByCurricularUnit(Long curricularUnitId) {
        return assessmentRepository.findByCurricularUnitId(curricularUnitId);  // Assumindo que o método exista no repositório
    }

    /**
     * Save a new assessment.
     *
     * @param assessment the entity to save
     * @return the saved assessment
     */
    public AssessmentUnit saveAssessment(AssessmentUnit assessment) {
        return assessmentRepository.save(assessment);
    }

    /**
     * Get all assessments in the system.
     *
     * @return a list of all assessments
     */
    public List<AssessmentUnit> getAllAssessments() {
        return assessmentRepository.findAll();
    }

    /**
     * Get a specific assessment by ID.
     *
     * @param id the ID of the assessment
     * @return the assessment as an Optional
     */
    public Optional<AssessmentUnit> getAssessmentById(Long id) {
        return assessmentRepository.findById(id);
    }

    /**
     * Update an existing assessment by ID.
     *
     * @param id the ID of the assessment to update
     * @param updatedAssessment the updated assessment data
     * @return the updated assessment
     */
    public AssessmentUnit updateAssessment(Long id, AssessmentUnit updatedAssessment) {
        Optional<AssessmentUnit> existingAssessment = assessmentRepository.findById(id);
        if (existingAssessment.isPresent()) {
            AssessmentUnit assessment = existingAssessment.get();
            assessment.setType(updatedAssessment.getType());
            assessment.setWeight(updatedAssessment.getWeight());
            assessment.setExamPeriod(updatedAssessment.getExamPeriod());
            assessment.setComputerRequired(updatedAssessment.getComputerRequired());
            assessment.setClassTime(updatedAssessment.getClassTime());
            assessment.setStartTime(updatedAssessment.getStartTime());
            assessment.setEndTime(updatedAssessment.getEndTime());
            assessment.setRoom(updatedAssessment.getRoom());
            // Atualize as associações conforme necessário
            assessment.setCurricularUnit(updatedAssessment.getCurricularUnit());
            assessment.setMap(updatedAssessment.getMap());

            return assessmentRepository.save(assessment);
        } else {
            throw new RuntimeException("Assessment not found");
        }
    }

    /**
     * Delete an assessment by ID.
     *
     * @param id the ID of the assessment to delete
     */
    public void deleteAssessment(Long id) {
        assessmentRepository.deleteById(id);
    }
}
