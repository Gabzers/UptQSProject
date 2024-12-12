package com.upt.upt.service;

import com.upt.upt.entity.AssessmentUnit;
import com.upt.upt.entity.MapUnit;
import com.upt.upt.repository.AssessmentUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class AssessmentUnitService {

    private final AssessmentUnitRepository assessmentRepository;

    @Autowired
    public AssessmentUnitService(AssessmentUnitRepository assessmentRepository) {
        this.assessmentRepository = assessmentRepository;
    }

    // Método para buscar avaliações pela unidade curricular
    public List<AssessmentUnit> getAssessmentsByCurricularUnit(Long curricularUnitId) {
        return assessmentRepository.findByCurricularUnitId(curricularUnitId);
    }

    // Método para buscar avaliação pela unidade curricular e ID da avaliação
    public Optional<AssessmentUnit> getAssessmentByUnitAndId(Long curricularUnitId, Long assessmentId) {
        return assessmentRepository.findByCurricularUnitIdAndId(curricularUnitId, assessmentId);
    }

    // Busca uma avaliação pelo ID
    public Optional<AssessmentUnit> findById(Long id) {
        return assessmentRepository.findById(id);
    }

    // Salva uma avaliação
    public AssessmentUnit saveAssessment(AssessmentUnit assessment) {
        return assessmentRepository.save(assessment);
    }

    // Busca todas as avaliações
    public List<AssessmentUnit> getAllAssessments() {
        return assessmentRepository.findAll();
    }

    // Método para buscar avaliações por coordenador
    public List<AssessmentUnit> getAssessmentsByCoordinator(Long coordinatorId) {
        List<AssessmentUnit> assessments = assessmentRepository.findByCurricularUnitCoordinatorId(coordinatorId);
        assessments.sort(Comparator.comparing(AssessmentUnit::getStartTime));
        return assessments;
    }

    // Método para buscar avaliações por semestre
    public List<AssessmentUnit> getAssessmentsBySemester(Long semesterId) {
        return assessmentRepository.findByCurricularUnit_SemesterUnit_Id(semesterId);
    }

    // Atualiza uma avaliação existente
    public AssessmentUnit updateAssessment(Long id, AssessmentUnit updatedAssessment) {
        Optional<AssessmentUnit> existingAssessment = assessmentRepository.findById(id);
        if (existingAssessment.isPresent()) {
            AssessmentUnit assessment = existingAssessment.get();
            // Atualizar os campos necessários
            assessment.setType(updatedAssessment.getType());
            assessment.setWeight(updatedAssessment.getWeight());
            // Outros campos...
            return assessmentRepository.save(assessment);
        } else {
            throw new RuntimeException("Assessment not found");
        }
    }

    // Deleta uma avaliação
    public void deleteAssessment(Long curricularUnitId, Long assessmentId) {
        Optional<AssessmentUnit> optionalAssessment = getAssessmentByUnitAndId(curricularUnitId, assessmentId);
        if (optionalAssessment.isPresent()) {
            AssessmentUnit assessment = optionalAssessment.get();
            assessmentRepository.delete(assessment);
        } else {
            throw new RuntimeException("Assessment not found");
        }
    }

    // Método para buscar avaliações de um MapUnit
    public List<AssessmentUnit> getAssessmentsFromMapUnit(MapUnit mapUnit) {
        return mapUnit.getAssessments();
    }

    // Método para verificar a disponibilidade da sala
    public boolean isRoomAvailable(Long roomId, LocalDateTime startTime, LocalDateTime endTime) {
        List<AssessmentUnit> assessments = assessmentRepository.findByRoomId(roomId);
        for (AssessmentUnit assessment : assessments) {
            if (startTime.isEqual(assessment.getStartTime()) || 
                (startTime.isBefore(assessment.getEndTime()) && endTime.isAfter(assessment.getStartTime()))) {
                return false; // Sala não está disponível
            }
        }
        return true; // Sala está disponível
    }
}
