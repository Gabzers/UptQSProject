package com.upt.upt.service;

import com.upt.upt.entity.Assessment;
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
     * Save an assessment.
     *
     * @param assessment the entity to save
     * @return the persisted entity
     */
    public Assessment saveAssessment(Assessment assessment) {
        return assessmentRepository.save(assessment);
    }

    /**
     * Get all the assessments.
     *
     * @return the list of entities
     */
    public List<Assessment> getAllAssessments() {
        return assessmentRepository.findAll();
    }

    /**
     * Get one assessment by ID.
     *
     * @param id the ID of the entity
     * @return the entity as an Optional
     */
    public Optional<Assessment> getAssessmentById(Long id) {
        return assessmentRepository.findById(id);
    }

    /**
     * Update an assessment.
     *
     * @param id the ID of the entity to update
     * @param updatedAssessment the updated entity
     * @return the updated entity
     */
    public Assessment updateAssessment(Long id, Assessment updatedAssessment) {
        Optional<Assessment> existingAssessment = assessmentRepository.findById(id);
        if (existingAssessment.isPresent()) {
            Assessment assessment = existingAssessment.get();
            assessment.setType(updatedAssessment.getType());
            assessment.setWeight(updatedAssessment.getWeight());
            assessment.setExamPeriod(updatedAssessment.getExamPeriod());
            assessment.setComputerRequired(updatedAssessment.getComputerRequired());
            assessment.setClassTime(updatedAssessment.getClassTime());
            assessment.setStartTime(updatedAssessment.getStartTime()); // Adaptar conforme necessário
            assessment.setEndTime(updatedAssessment.getEndTime()); // Adaptar conforme necessário
            assessment.setRoom(updatedAssessment.getRoom());
            return assessmentRepository.save(assessment);
        } else {
            throw new RuntimeException("Assessment not found");
        }
    }

    /**
     * Delete the assessment by ID.
     *
     * @param id the ID of the entity to delete
     */
    public void deleteAssessment(Long id) {
        assessmentRepository.deleteById(id);
    }
}
