package com.upt.upt.service;

import com.upt.upt.entity.AssessmentUnit;
import com.upt.upt.entity.CurricularUnit;
import com.upt.upt.entity.MapUnit;
import com.upt.upt.entity.RoomUnit;
import com.upt.upt.entity.SemesterUnit;
import com.upt.upt.entity.YearUnit;
import com.upt.upt.repository.AssessmentUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import com.upt.upt.repository.RoomUnitRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AssessmentUnitService {

    private final AssessmentUnitRepository assessmentRepository;
    private final RoomUnitRepository roomUnitRepository;

    @Autowired
    public AssessmentUnitService(AssessmentUnitRepository assessmentRepository, RoomUnitRepository roomUnitRepository) {
        this.assessmentRepository = assessmentRepository;
        this.roomUnitRepository = roomUnitRepository;
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
    public AssessmentUnit saveAssessment(AssessmentUnit assessment, List<Long> roomIds) {
        List<RoomUnit> rooms = roomUnitRepository.findAllById(roomIds);
        assessment.setRooms(rooms);
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
        List<AssessmentUnit> assessments = assessmentRepository.findByRooms_Id(roomId);
        for (AssessmentUnit assessment : assessments) {
            if (startTime.isEqual(assessment.getStartTime()) || 
                (startTime.isBefore(assessment.getEndTime()) && endTime.isAfter(assessment.getStartTime()))) {
                return false; // Sala não está disponível
            }
        }
        return true; // Sala está disponível
    }

    // Método para verificar a disponibilidade das salas
    public boolean areRoomsAvailable(List<Long> roomIds, LocalDateTime startTime, LocalDateTime endTime) {
        for (Long roomId : roomIds) {
            if (!isRoomAvailable(roomId, startTime, endTime)) {
                return false;
            }
        }
        return true;
    }

    // Método para validar as datas de avaliação
    public boolean validateAssessmentDates(String assessmentExamPeriod, LocalDateTime startTime, LocalDateTime endTime, SemesterUnit semesterUnit, YearUnit currentYear, Model model, Long curricularUnitId) {
        LocalDate semesterStart, semesterEnd;
        switch (assessmentExamPeriod) {
            case "Teaching Period":
            case "Exam Period":
                semesterStart = LocalDate.parse(semesterUnit.getStartDate());
                semesterEnd = LocalDate.parse(semesterUnit.getEndDate());
                if (startTime.toLocalDate().isBefore(semesterStart) || endTime.toLocalDate().isAfter(semesterEnd)) {
                    model.addAttribute("error", "Assessment dates must be within the semester dates.");
                    return false;
                }
                break;
            case "Resource Period":
                semesterStart = LocalDate.parse(semesterUnit.getResitPeriodStart());
                semesterEnd = LocalDate.parse(semesterUnit.getResitPeriodEnd());
                if (startTime.toLocalDate().isBefore(semesterStart) || endTime.toLocalDate().isAfter(semesterEnd)) {
                    model.addAttribute("error", "Assessment dates must be within the resit period dates.");
                    return false;
                }
                break;
            case "Special Period":
                semesterStart = LocalDate.parse(currentYear.getSpecialExamStart());
                semesterEnd = LocalDate.parse(currentYear.getSpecialExamEnd());
                if (startTime.toLocalDate().isBefore(semesterStart) || endTime.toLocalDate().isAfter(semesterEnd)) {
                    model.addAttribute("error", "Assessment dates must be within the special period dates.");
                    return false;
                }
                break;
            default:
                model.addAttribute("error", "Invalid exam period.");
                return false;
        }
        return true;
    }

    public int calculatePeriodTotalWeight(CurricularUnit uc, String assessmentExamPeriod, int newAssessmentWeight) {
        return uc.getAssessments().stream()
                .filter(e -> assessmentExamPeriod.equals(e.getExamPeriod()))
                .mapToInt(AssessmentUnit::getWeight)
                .sum() + newAssessmentWeight;
    }

    public boolean noAssessmentsForPeriod(List<AssessmentUnit> assessments, String period) {
        return assessments.stream().noneMatch(a -> period.equals(a.getExamPeriod()));
    }

    public Map<String, String> getValidDateRanges(String examPeriod, CurricularUnit curricularUnit, YearUnit currentYear) {
        SemesterUnit semesterUnit = curricularUnit.getSemester() == 1 ? currentYear.getFirstSemester() : currentYear.getSecondSemester();

        Map<String, String> validDateRanges = new HashMap<>();
        switch (examPeriod) {
            case "Teaching Period":
                validDateRanges.put("start", semesterUnit.getStartDate());
                validDateRanges.put("end", semesterUnit.getEndDate());
                break;
            case "Exam Period":
                validDateRanges.put("start", semesterUnit.getExamPeriodStart());
                validDateRanges.put("end", semesterUnit.getExamPeriodEnd());
                break;
            case "Resource Period":
                validDateRanges.put("start", semesterUnit.getResitPeriodStart());
                validDateRanges.put("end", semesterUnit.getResitPeriodEnd());
                break;
            case "Special Period":
                validDateRanges.put("start", currentYear.getSpecialExamStart());
                validDateRanges.put("end", currentYear.getSpecialExamEnd());
                break;
            default:
                throw new IllegalArgumentException("Invalid exam period");
        }

        return validDateRanges;
    }

    public List<AssessmentUnit> getAssessmentsByYear(int year) {
        return assessmentRepository.findByCurricularUnit_Year(year);
    }

    // Método para buscar avaliações de diferentes anos, mas da mesma UC
    public List<AssessmentUnit> getAssessmentsByDifferentYearsSameUC(Long curricularUnitId) {
        List<AssessmentUnit> allAssessments = assessmentRepository.findByCurricularUnitId(curricularUnitId);
        return allAssessments.stream()
                .filter(assessment -> !assessment.getCurricularUnit().getYear().equals(assessment.getCurricularUnit().getYear()))
                .collect(Collectors.toList());
    }

    // Método para buscar avaliações por ano, semestre e coordenador
    public List<AssessmentUnit> getAssessmentsByYearAndSemesterAndCoordinator(int year, int semester, Long coordinatorId) {
        return assessmentRepository.findByCurricularUnit_YearAndCurricularUnit_SemesterAndCurricularUnit_CoordinatorId(year, semester, coordinatorId);
    }


    // Método para buscar avaliações de diferentes anos, mas do mesmo semestre e coordenador
    public List<AssessmentUnit> getAssessmentsByDifferentYearsSameSemesterAndCoordinator(int semester, Long coordinatorId, int year) {
        return assessmentRepository.findByCurricularUnit_SemesterAndCurricularUnit_CoordinatorIdAndCurricularUnit_YearNot(semester, coordinatorId, year);
    }

    // Atualiza uma avaliação existente
    public AssessmentUnit updateAssessment(Long id, AssessmentUnit updatedAssessment, List<Long> roomIds) {
        Optional<AssessmentUnit> existingAssessment = assessmentRepository.findById(id);
        if (existingAssessment.isPresent()) {
            AssessmentUnit assessment = existingAssessment.get();
            // Atualizar os campos necessários
            assessment.setType(updatedAssessment.getType());
            assessment.setWeight(updatedAssessment.getWeight());
            // Atualizar as salas
            List<RoomUnit> rooms = roomUnitRepository.findAllById(roomIds);
            assessment.setRooms(rooms);
            // Outros campos...
            return assessmentRepository.save(assessment);
        } else {
            throw new RuntimeException("Assessment not found");
        }
    }
}
