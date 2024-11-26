package com.upt.upt.service;

import com.upt.upt.entity.AssessmentUnit;
import com.upt.upt.repository.AssessmentUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AssessmentUnitService {

    private final AssessmentUnitRepository assessmentRepository;

    @Autowired
    public AssessmentUnitService(AssessmentUnitRepository assessmentRepository) {
        this.assessmentRepository = assessmentRepository;
    }

    public List<AssessmentUnit> getAssessmentsByCurricularUnit(Long curricularUnitId) {
        return assessmentRepository.findByCurricularUnitId(curricularUnitId);
    }

    public AssessmentUnit saveAssessment(AssessmentUnit assessment) {
        return assessmentRepository.save(assessment);
    }

    public List<AssessmentUnit> getAllAssessments() {
        return assessmentRepository.findAll();
    }

    public Optional<AssessmentUnit> getAssessmentById(Long id) {
        return assessmentRepository.findById(id);  // Supondo que você tenha um repositório que fornece esse método
    }

    public Optional<AssessmentUnit> getAssessmentByUnitAndId(Long curricularUnitId, Long assessmentId) {
        return assessmentRepository.findByCurricularUnitIdAndId(curricularUnitId, assessmentId);
    }

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
            assessment.setCurricularUnit(updatedAssessment.getCurricularUnit());

            return assessmentRepository.save(assessment);
        } else {
            throw new RuntimeException("Assessment not found");
        }
    }

    public void deleteAssessment(Long curricularUnitId, Long assessmentId) {
        Optional<AssessmentUnit> optionalAssessment = getAssessmentByUnitAndId(curricularUnitId, assessmentId);
        if (optionalAssessment.isPresent()) {
            AssessmentUnit assessment = optionalAssessment.get();
            // Se necessário, você pode validar que o usuário é o coordenador da UC antes de deletar
            assessmentRepository.delete(assessment);
        } else {
            throw new RuntimeException("Assessment not found");
        }
    }
}
