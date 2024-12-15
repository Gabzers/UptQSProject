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

/**
 * Service class for managing AssessmentUnit entities.
 * 
 * @autor Grupo 5 - 47719, 47713, 46697, 47752, 47004
 */
@Service
public class AssessmentUnitService {

    private final AssessmentUnitRepository assessmentRepository;
    private final RoomUnitRepository roomUnitRepository;

    @Autowired
    public AssessmentUnitService(AssessmentUnitRepository assessmentRepository, RoomUnitRepository roomUnitRepository) {
        this.assessmentRepository = assessmentRepository;
        this.roomUnitRepository = roomUnitRepository;
    }

    /**
     * Gets assessments by curricular unit ID.
     * 
     * @param curricularUnitId the ID of the curricular unit
     * @return the list of assessments
     */
    public List<AssessmentUnit> getAssessmentsByCurricularUnit(Long curricularUnitId) {
        return assessmentRepository.findByCurricularUnitId(curricularUnitId);
    }

    /**
     * Gets an assessment by curricular unit ID and assessment ID.
     * 
     * @param curricularUnitId the ID of the curricular unit
     * @param assessmentId the ID of the assessment
     * @return the optional assessment
     */
    public Optional<AssessmentUnit> getAssessmentByUnitAndId(Long curricularUnitId, Long assessmentId) {
        return assessmentRepository.findByCurricularUnitIdAndId(curricularUnitId, assessmentId);
    }

    /**
     * Finds an assessment by ID.
     * 
     * @param id the ID of the assessment
     * @return the optional assessment
     */
    public Optional<AssessmentUnit> findById(Long id) {
        return assessmentRepository.findById(id);
    }

    /**
     * Saves an assessment.
     * 
     * @param assessment the assessment to save
     * @param roomIds the IDs of the rooms
     * @return the saved assessment
     */
    public AssessmentUnit saveAssessment(AssessmentUnit assessment, List<Long> roomIds) {
        List<RoomUnit> rooms = roomUnitRepository.findAllById(roomIds);
        assessment.setRooms(rooms);
        return assessmentRepository.save(assessment);
    }

    /**
     * Gets all assessments.
     * 
     * @return the list of all assessments
     */
    public List<AssessmentUnit> getAllAssessments() {
        return assessmentRepository.findAll();
    }

    /**
     * Gets assessments by coordinator ID.
     * 
     * @param coordinatorId the ID of the coordinator
     * @return the list of assessments
     */
    public List<AssessmentUnit> getAssessmentsByCoordinator(Long coordinatorId) {
        List<AssessmentUnit> assessments = assessmentRepository.findByCurricularUnitCoordinatorId(coordinatorId);
        assessments.sort(Comparator.comparing(AssessmentUnit::getStartTime));
        return assessments;
    }

    /**
     * Gets assessments by semester ID.
     * 
     * @param semesterId the ID of the semester
     * @return the list of assessments
     */
    public List<AssessmentUnit> getAssessmentsBySemester(Long semesterId) {
        return assessmentRepository.findByCurricularUnit_SemesterUnit_Id(semesterId);
    }

    /**
     * Updates an existing assessment.
     * 
     * @param id the ID of the assessment to update
     * @param updatedAssessment the updated assessment
     * @return the updated assessment
     */
    public AssessmentUnit updateAssessment(Long id, AssessmentUnit updatedAssessment) {
        Optional<AssessmentUnit> existingAssessment = assessmentRepository.findById(id);
        if (existingAssessment.isPresent()) {
            AssessmentUnit assessment = existingAssessment.get();
            assessment.setType(updatedAssessment.getType());
            assessment.setWeight(updatedAssessment.getWeight());
            return assessmentRepository.save(assessment);
        } else {
            throw new RuntimeException("Assessment not found");
        }
    }

    /**
     * Deletes an assessment.
     * 
     * @param curricularUnitId the ID of the curricular unit
     * @param assessmentId the ID of the assessment
     */
    public void deleteAssessment(Long curricularUnitId, Long assessmentId) {
        Optional<AssessmentUnit> optionalAssessment = getAssessmentByUnitAndId(curricularUnitId, assessmentId);
        if (optionalAssessment.isPresent()) {
            AssessmentUnit assessment = optionalAssessment.get();
            assessment.getRooms().clear();
            assessmentRepository.delete(assessment);
        } else {
            throw new RuntimeException("Assessment not found");
        }
    }

    /**
     * Gets assessments from a map unit.
     * 
     * @param mapUnit the map unit
     * @return the list of assessments
     */
    public List<AssessmentUnit> getAssessmentsFromMapUnit(MapUnit mapUnit) {
        return mapUnit.getAssessments();
    }

    /**
     * Checks if a room is available for a given time range.
     * 
     * @param roomId the ID of the room
     * @param startTime the start time
     * @param endTime the end time
     * @return true if the room is available, false otherwise
     */
    public boolean isRoomAvailable(Long roomId, LocalDateTime startTime, LocalDateTime endTime) {
        List<AssessmentUnit> assessments = assessmentRepository.findByRooms_Id(roomId);
        for (AssessmentUnit assessment : assessments) {
            if (startTime.isEqual(assessment.getStartTime()) || 
                (startTime.isBefore(assessment.getEndTime()) && endTime.isAfter(assessment.getStartTime()))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if rooms are available for a given time range.
     * 
     * @param roomIds the IDs of the rooms
     * @param startTime the start time
     * @param endTime the end time
     * @return true if the rooms are available, false otherwise
     */
    public boolean areRoomsAvailable(List<Long> roomIds, LocalDateTime startTime, LocalDateTime endTime) {
        for (Long roomId : roomIds) {
            if (!isRoomAvailable(roomId, startTime, endTime)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Validates the assessment dates.
     * 
     * @param assessmentExamPeriod the exam period
     * @param startTime the start time
     * @param endTime the end time
     * @param semesterUnit the semester unit
     * @param currentYear the current year
     * @param model the model to add attributes to
     * @param curricularUnitId the ID of the curricular unit
     * @return true if the dates are valid, false otherwise
     */
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

    /**
     * Calculates the total weight of assessments for a period.
     * 
     * @param uc the curricular unit
     * @param assessmentExamPeriod the exam period
     * @param newAssessmentWeight the weight of the new assessment
     * @return the total weight
     */
    public int calculatePeriodTotalWeight(CurricularUnit uc, String assessmentExamPeriod, int newAssessmentWeight) {
        return uc.getAssessments().stream()
                .filter(e -> assessmentExamPeriod.equals(e.getExamPeriod()))
                .mapToInt(AssessmentUnit::getWeight)
                .sum() + newAssessmentWeight;
    }

    /**
     * Checks if there are no assessments for a period.
     * 
     * @param assessments the list of assessments
     * @param period the period
     * @return true if there are no assessments, false otherwise
     */
    public boolean noAssessmentsForPeriod(List<AssessmentUnit> assessments, String period) {
        return assessments.stream().noneMatch(a -> period.equals(a.getExamPeriod()));
    }

    /**
     * Gets the valid date ranges for an exam period.
     * 
     * @param examPeriod the exam period
     * @param curricularUnit the curricular unit
     * @param currentYear the current year
     * @return the valid date ranges
     */
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

    /**
     * Gets assessments by year.
     * 
     * @param year the year
     * @return the list of assessments
     */
    public List<AssessmentUnit> getAssessmentsByYear(int year) {
        return assessmentRepository.findByCurricularUnit_Year(year);
    }

    /**
     * Gets assessments by different years but same curricular unit.
     * 
     * @param curricularUnitId the ID of the curricular unit
     * @return the list of assessments
     */
    public List<AssessmentUnit> getAssessmentsByDifferentYearsSameUC(Long curricularUnitId) {
        List<AssessmentUnit> allAssessments = assessmentRepository.findByCurricularUnitId(curricularUnitId);
        return allAssessments.stream()
                .filter(assessment -> !assessment.getCurricularUnit().getYear().equals(assessment.getCurricularUnit().getYear()))
                .collect(Collectors.toList());
    }

    /**
     * Gets assessments by year, semester, and coordinator.
     * 
     * @param year the year
     * @param semester the semester
     * @param coordinatorId the ID of the coordinator
     * @return the list of assessments
     */
    public List<AssessmentUnit> getAssessmentsByYearAndSemesterAndCoordinator(int year, int semester, Long coordinatorId) {
        return assessmentRepository.findByCurricularUnit_YearAndCurricularUnit_SemesterAndCurricularUnit_CoordinatorId(year, semester, coordinatorId);
    }

    /**
     * Gets assessments by different years but same semester and coordinator.
     * 
     * @param semester the semester
     * @param coordinatorId the ID of the coordinator
     * @param year the year
     * @return the list of assessments
     */
    public List<AssessmentUnit> getAssessmentsByDifferentYearsSameSemesterAndCoordinator(int semester, Long coordinatorId, int year) {
        return assessmentRepository.findByCurricularUnit_SemesterAndCurricularUnit_CoordinatorIdAndCurricularUnit_YearNot(semester, coordinatorId, year);
    }

    /**
     * Updates an existing assessment.
     * 
     * @param id the ID of the assessment to update
     * @param updatedAssessment the updated assessment
     * @param roomIds the IDs of the rooms
     * @return the updated assessment
     */
    public AssessmentUnit updateAssessment(Long id, AssessmentUnit updatedAssessment, List<Long> roomIds) {
        Optional<AssessmentUnit> existingAssessment = assessmentRepository.findById(id);
        if (existingAssessment.isPresent()) {
            AssessmentUnit assessment = existingAssessment.get();
            assessment.setType(updatedAssessment.getType());
            assessment.setWeight(updatedAssessment.getWeight());
            List<RoomUnit> rooms = roomUnitRepository.findAllById(roomIds);
            assessment.setRooms(rooms);
            return assessmentRepository.save(assessment);
        } else {
            throw new RuntimeException("Assessment not found");
        }
    }

    /**
     * Gets assessments by room ID.
     * 
     * @param roomId the ID of the room
     * @return the list of assessments
     */
    public List<AssessmentUnit> getAssessmentsByRoomId(Long roomId) {
        return assessmentRepository.findByRooms_Id(roomId);
    }
}
