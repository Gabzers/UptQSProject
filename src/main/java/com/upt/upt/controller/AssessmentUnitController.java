package com.upt.upt.controller;

import com.upt.upt.entity.AssessmentUnit;
import com.upt.upt.entity.CurricularUnit;
import com.upt.upt.entity.CoordinatorUnit;
import com.upt.upt.entity.DirectorUnit;
import com.upt.upt.entity.MapUnit;
import com.upt.upt.entity.RoomUnit;
import com.upt.upt.entity.SemesterUnit;
import com.upt.upt.entity.UserType;
import com.upt.upt.entity.YearUnit;
import com.upt.upt.service.AssessmentUnitService;
import com.upt.upt.service.CurricularUnitService;
import com.upt.upt.service.CoordinatorUnitService;
import com.upt.upt.service.RoomUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.time.LocalTime;

/**
 * Controller for handling requests related to AssessmentUnit entities.
 */
@Controller
@RequestMapping("/coordinator")
public class AssessmentUnitController {

    @Autowired
    private AssessmentUnitService assessmentUnitService;

    @Autowired
    private CurricularUnitService curricularUnitService;

    @Autowired
    private CoordinatorUnitService coordinatorUnitService;

    @Autowired
    private RoomUnitService roomUnitService;

    private Optional<CoordinatorUnit> verifyCoordinator(HttpSession session) {
        Long coordinatorId = (Long) session.getAttribute("userId");
        if (coordinatorId == null) {
            return Optional.empty();
        }
        return coordinatorUnitService.getCoordinatorById(coordinatorId);
    }

    private boolean isCoordinator(HttpSession session) {
        UserType userType = (UserType) session.getAttribute("userType");
        return userType == UserType.COORDINATOR;
    }

    @GetMapping("/coordinator_create_evaluation")
    public String createEvaluationPage(@RequestParam("curricularUnitId") Long curricularUnitId, Model model, @RequestParam(value = "error", required = false) String error, HttpSession session) {
        if (!isCoordinator(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        Optional<CurricularUnit> curricularUnit = curricularUnitService.getCurricularUnitById(curricularUnitId);
        if (curricularUnit.isPresent()) {
            CurricularUnit uc = curricularUnit.get();
            if (uc.getEvaluationsCount() == uc.getAssessments().size()) {
                return "redirect:/coordinator/coordinator_evaluationsUC?id=" + curricularUnitId + "&error=Evaluations already complete";
            }
            model.addAttribute("uc", uc);
            model.addAttribute("rooms", roomUnitService.getAllRooms());
            if (error != null) {
                model.addAttribute("error", error);
            }
            return "coordinator_addEvaluations";
        } else {
            return "redirect:/coordinator";
        }
    }

    @PostMapping("/coordinator_addEvaluation")
public String saveEvaluation(@RequestParam Map<String, String> params, HttpSession session, Model model) {
    if (!isCoordinator(session)) {
        return "redirect:/login?error=Unauthorized access";
    }

    Long curricularUnitId = Long.parseLong(params.get("curricularUnitId"));
    String assessmentExamPeriod = params.get("assessmentExamPeriod");

    if (assessmentExamPeriod == null || assessmentExamPeriod.isEmpty() || assessmentExamPeriod.equals("Select Exam Period")) {
        model.addAttribute("error", "Exam Period is required.");
        return "redirect:/coordinator/coordinator_create_evaluation?curricularUnitId=" + curricularUnitId + "&error=Exam Period is required.";
    }

    Boolean computerRequired = params.get("assessmentComputerRequired") != null;
    Boolean classTime = params.get("assessmentClassTime") != null;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    String startTimeStr = params.get("assessmentStartTime");
    String endTimeStr = params.get("assessmentEndTime");

    if (startTimeStr.isEmpty() || endTimeStr.isEmpty()) {
        model.addAttribute("error", "Start and end date and time are required.");
        return "redirect:/coordinator/coordinator_create_evaluation?curricularUnitId=" + curricularUnitId + "&error=Start and end date and time are required.";
    }

    LocalDateTime startTime = LocalDateTime.parse(startTimeStr, formatter);
    LocalDateTime endTime = LocalDateTime.parse(endTimeStr, formatter);

    // Validate time range
    if (startTime.toLocalTime().isBefore(LocalTime.of(8, 0)) || endTime.toLocalTime().isAfter(LocalTime.of(23, 59))) {
        model.addAttribute("error", "Assessment times must be between 08:00 and 23:59.");
        return "redirect:/coordinator/coordinator_create_evaluation?curricularUnitId=" + curricularUnitId + "&error=Assessment times must be between 08:00 and 23:59.";
    }

    // Validate start time is not after or equal to end time
    if (!startTime.isBefore(endTime)) {
        model.addAttribute("error", "Start time cannot be after or equal to end time.");
        return "redirect:/coordinator/coordinator_create_evaluation?curricularUnitId=" + curricularUnitId + "&error=Start time cannot be after or equal to end time.";
    }

    Optional<CurricularUnit> curricularUnit = curricularUnitService.getCurricularUnitById(curricularUnitId);
    if (!curricularUnit.isPresent()) {
        return "redirect:/coordinator";
    }

    CurricularUnit uc = curricularUnit.get();
    if (uc.getEvaluationsCount() == uc.getAssessments().size()) {
        model.addAttribute("error", "Evaluations already complete.");
        return "redirect:/coordinator/coordinator_create_evaluation?curricularUnitId=" + curricularUnitId + "&error=Evaluations already complete.";
    }

    Long coordinatorId = (Long) session.getAttribute("userId");
    CoordinatorUnit coordinator = coordinatorUnitService.getCoordinatorById(coordinatorId)
            .orElseThrow(() -> new IllegalArgumentException("Coordinator not found"));
    DirectorUnit director = coordinator.getDirectorUnit();
    YearUnit currentYear = director.getCurrentYear();
    SemesterUnit semesterUnit = uc.getSemester() == 1 ? currentYear.getFirstSemester() : currentYear.getSecondSemester();

    if (!assessmentUnitService.validateAssessmentDates(assessmentExamPeriod, startTime, endTime, semesterUnit, currentYear, model, curricularUnitId)) {
        return "redirect:/coordinator/coordinator_create_evaluation?curricularUnitId=" + curricularUnitId + "&error=Assessment dates must be within the valid period.";
    }

    int periodTotalWeight = assessmentUnitService.calculatePeriodTotalWeight(uc, assessmentExamPeriod, Integer.parseInt(params.get("assessmentWeight")));

    if (periodTotalWeight > 100) {
        model.addAttribute("error", "The total weight of evaluations for this period must not exceed 100%.");
        return "redirect:/coordinator/coordinator_create_evaluation?curricularUnitId=" + curricularUnitId + "&error=The total weight of evaluations for this period must not exceed 100%.";
    }

    if ("Mixed".equals(params.get("ucEvaluationType")) && uc.getAssessments().size() == uc.getEvaluationsCount() - 1 && !uc.hasExamPeriodEvaluation() && !"Exam Period".equals(assessmentExamPeriod)) {
        model.addAttribute("error", "For Mixed evaluation type, at least one evaluation must be of type 'Exam Period'.");
        return "redirect:/coordinator/coordinator_create_evaluation?curricularUnitId=" + curricularUnitId + "&error=For Mixed evaluation type, at least one evaluation must be of type 'Exam Period'.";
    }

    List<RoomUnit> selectedRooms;
    String assessmentType = params.get("assessmentType");
    if ("Work Developed Throughout the Semester".equals(assessmentType) || "Work Submission".equals(assessmentType)) {
        selectedRooms = List.of(roomUnitService.getOrCreateOnlineRoom());
    } else if (classTime) {
        selectedRooms = List.of(roomUnitService.getOrCreateClassTimeRoom());
    } else {
        try {
            selectedRooms = roomUnitService.getAvailableRooms(uc.getStudentsNumber(), computerRequired, startTime, endTime);
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/coordinator/coordinator_create_evaluation?curricularUnitId=" + curricularUnitId + "&error=" + e.getMessage();
        }
    }

    // Validate no overlap with other assessments in the same year/semester within the same coordinator, except for "Work Presentation" and "Group Work Presentation"
    List<AssessmentUnit> assessmentsInSameYearSemester = assessmentUnitService.getAssessmentsByYearAndSemesterAndCoordinator(uc.getYear(), uc.getSemester(), coordinatorId);
    for (AssessmentUnit assessment : assessmentsInSameYearSemester) {
        if (!assessment.getType().equals("Work Presentation") && !assessment.getType().equals("Group Work Presentation") &&
            !params.get("assessmentType").equals("Work Presentation") && !params.get("assessmentType").equals("Group Work Presentation") &&
            assessment.getStartTime().isBefore(endTime.plusHours(24)) && assessment.getEndTime().isAfter(startTime.minusHours(24))) {
            model.addAttribute("error", "There must be at least 24 hours between assessments in the same year/semester.");
            return "redirect:/coordinator/coordinator_create_evaluation?curricularUnitId=" + curricularUnitId + "&error=There must be at least 24 hours between assessments in the same year/semester.";
        }
        if ((assessment.getType().equals("Work Presentation") || assessment.getType().equals("Group Work Presentation")) &&
            (params.get("assessmentType").equals("Work Presentation") || params.get("assessmentType").equals("Group Work Presentation")) &&
            assessment.getStartTime().isBefore(endTime.plusHours(24)) && assessment.getEndTime().isAfter(startTime.minusHours(24))) {
            model.addAttribute("error", "Only one 'Work Presentation' or 'Group Work Presentation' can be scheduled within the same 24-hour period.");
            return "redirect:/coordinator/coordinator_create_evaluation?curricularUnitId=" + curricularUnitId + "&error=Only one 'Work Presentation' or 'Group Work Presentation' can be scheduled within the same 24-hour period.";
        }
        if ((assessment.getType().equals("Work Presentation") || assessment.getType().equals("Group Work Presentation") ||
            params.get("assessmentType").equals("Work Presentation") || params.get("assessmentType").equals("Group Work Presentation")) &&
            (assessment.getStartTime().isEqual(startTime) || assessment.getEndTime().isEqual(endTime) ||
            (startTime.isBefore(assessment.getEndTime()) && endTime.isAfter(assessment.getStartTime())))) {
            model.addAttribute("error", "No other assessments can be scheduled at the same time as a 'Work Presentation' or 'Group Work Presentation'.");
            return "redirect:/coordinator/coordinator_create_evaluation?curricularUnitId=" + curricularUnitId + "&error=No other assessments can be scheduled at the same time as a 'Work Presentation' or 'Group Work Presentation'.";
        }
    }

    // Validate no overlap with other assessments of different years but same UC within the same coordinator
    List<AssessmentUnit> assessmentsInDifferentYearsSameUC = assessmentUnitService.getAssessmentsByDifferentYearsSameSemesterAndCoordinator(uc.getSemester(), coordinatorId, uc.getYear());
    for (AssessmentUnit assessment : assessmentsInDifferentYearsSameUC) {
        if (assessment.getStartTime().isBefore(endTime.plusHours(24)) && assessment.getEndTime().isAfter(startTime.minusHours(24))) {
            model.addAttribute("error", "Avoid scheduling assessments of different years but same UC on overlapping dates.");
            return "redirect:/coordinator/coordinator_create_evaluation?curricularUnitId=" + curricularUnitId + "&error=Avoid scheduling assessments of different years but same UC on overlapping dates.";
        }
    }

    AssessmentUnit assessmentUnit = new AssessmentUnit();
    assessmentUnit.setType(params.get("assessmentType"));
    assessmentUnit.setWeight(Integer.parseInt(params.get("assessmentWeight")));
    assessmentUnit.setExamPeriod(assessmentExamPeriod);
    assessmentUnit.setComputerRequired(computerRequired);
    assessmentUnit.setClassTime(classTime);
    assessmentUnit.setStartTime(startTime);
    assessmentUnit.setEndTime(endTime);
    assessmentUnit.setRooms(selectedRooms);
    assessmentUnit.setCurricularUnit(uc);
    assessmentUnit.setMinimumGrade(Double.parseDouble(params.get("assessmentMinimumGrade")));

    // Assign the assessment to the map of the current semester
    MapUnit map = semesterUnit.getMapUnit();
    assessmentUnit.setMap(map);

    assessmentUnitService.saveAssessment(assessmentUnit, selectedRooms.stream().map(RoomUnit::getId).collect(Collectors.toList()));

    return "redirect:/coordinator/coordinator_evaluationsUC?id=" + curricularUnitId;
}



    @GetMapping("/coordinator_editEvaluations/{assessmentId}")
    public String editEvaluation(@PathVariable("assessmentId") Long assessmentId, @RequestParam("curricularUnitId") Long curricularUnitId, Model model, HttpSession session) {
        if (!isCoordinator(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        Optional<CurricularUnit> curricularUnit = curricularUnitService.getCurricularUnitById(curricularUnitId);
        if (curricularUnit.isPresent()) {
            Optional<AssessmentUnit> assessment = assessmentUnitService.getAssessmentByUnitAndId(curricularUnitId, assessmentId);
            if (assessment.isPresent()) {
                AssessmentUnit assessmentUnit = assessment.get();
                model.addAttribute("assessment", assessmentUnit);
                model.addAttribute("formattedStartTime", assessmentUnit.getStartTime().toString());
                model.addAttribute("formattedEndTime", assessmentUnit.getEndTime().toString());
                model.addAttribute("uc", curricularUnit.get());
                return "coordinator_editEvaluations";
            }
        }
        return "redirect:/coordinator";
    }

    @PostMapping("/coordinator_editEvaluations/{id}")
    public String updateEvaluation(@PathVariable("id") Long id, @RequestParam Map<String, String> params, @RequestParam List<Long> roomIds, Model model, HttpSession session) {
        if (!isCoordinator(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        Long curricularUnitId = Long.parseLong(params.get("curricularUnitId"));
        String assessmentExamPeriod = params.get("assessmentExamPeriod");
        if (assessmentExamPeriod == null || assessmentExamPeriod.isEmpty() || assessmentExamPeriod.equals("Select Exam Period")) {
            model.addAttribute("error", "Exam Period is required.");
            return "redirect:/coordinator/coordinator_editEvaluations/" + id + "?curricularUnitId=" + curricularUnitId;
        }

        Boolean computerRequired = params.get("assessmentComputerRequired") != null;
        Boolean classTime = params.get("assessmentClassTime") != null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        String startTimeStr = params.get("assessmentStartTime");
        String endTimeStr = params.get("assessmentEndTime");

        if (startTimeStr.isEmpty() || endTimeStr.isEmpty()) {
            model.addAttribute("error", "Start and end date and time are required.");
            return "redirect:/coordinator/coordinator_editEvaluations/" + id + "?curricularUnitId=" + curricularUnitId;
        }

        LocalDateTime startTime = LocalDateTime.parse(startTimeStr, formatter);
        LocalDateTime endTime = LocalDateTime.parse(endTimeStr, formatter);

        // Validate time range
        if (startTime.toLocalTime().isBefore(LocalTime.of(8, 0)) || endTime.toLocalTime().isAfter(LocalTime.of(23, 59))) {
            model.addAttribute("error", "Assessment times must be between 08:00 and 23:59.");
            return "redirect:/coordinator/coordinator_editEvaluations/" + id + "?curricularUnitId=" + curricularUnitId + "&error=Assessment times must be between 08:00 and 23:59.";
        }

        // Validate start time is not after or equal to end time
        if (!startTime.isBefore(endTime)) {
            model.addAttribute("error", "Start time cannot be after or equal to end time.");
            return "redirect:/coordinator/coordinator_editEvaluations/" + id + "?curricularUnitId=" + curricularUnitId + "&error=Start time cannot be after or equal to end time.";
        }

        Optional<CurricularUnit> curricularUnit = curricularUnitService.getCurricularUnitById(curricularUnitId);
        if (!curricularUnit.isPresent()) {
            return "redirect:/coordinator";
        }

        CurricularUnit uc = curricularUnit.get();
        Optional<AssessmentUnit> assessmentUnitOptional = assessmentUnitService.findById(id);
        if (!assessmentUnitOptional.isPresent()) {
            return "redirect:/coordinator";
        }

        AssessmentUnit assessmentUnit = assessmentUnitOptional.get();
        int periodTotalWeight = assessmentUnitService.calculatePeriodTotalWeight(uc, assessmentExamPeriod, Integer.parseInt(params.get("assessmentWeight"))) - assessmentUnit.getWeight();

        if (periodTotalWeight > 100) {
            model.addAttribute("uc", uc);
            model.addAttribute("assessment", assessmentUnit);
            model.addAttribute("error", "The total weight of evaluations for this period must not exceed 100%.");
            return "redirect:/coordinator/coordinator_editEvaluations/" + id + "?curricularUnitId=" + curricularUnitId;
        }

        List<RoomUnit> selectedRooms = new ArrayList<>();
        int remainingStudents = uc.getStudentsNumber();

        // Fetch available rooms based on the number of students and computer requirement
        List<RoomUnit> availableRooms = new ArrayList<>(roomUnitService.getAvailableRooms(remainingStudents, computerRequired, startTime, endTime));
        if (availableRooms.isEmpty()) {
            model.addAttribute("error", "No available rooms found for the specified criteria.");
            return "redirect:/coordinator/coordinator_editEvaluations/" + id + "?curricularUnitId=" + curricularUnitId + "&error=No available rooms found for the specified criteria.";
        }

        while (remainingStudents > 0 && !availableRooms.isEmpty()) {
            final int students = remainingStudents; // Use a final variable within the lambda expression
            // Sort rooms by the number of seats closest to the number of remaining students
            availableRooms.sort((r1, r2) -> {
                int diff1 = Math.abs(r1.getSeatsCount() - students);
                int diff2 = Math.abs(r2.getSeatsCount() - students);
                return Integer.compare(diff1, diff2);
            });

            RoomUnit bestRoom = availableRooms.get(0);
            if (assessmentUnitService.isRoomAvailable(bestRoom.getId(), startTime, endTime)) {
                selectedRooms.add(bestRoom);
                remainingStudents -= bestRoom.getSeatsCount();
                availableRooms.remove(bestRoom);
            } else {
                availableRooms.remove(bestRoom);
            }
        }

        if (remainingStudents > 0) {
            model.addAttribute("error", "Not enough available rooms to accommodate all students.");
            return "redirect:/coordinator/coordinator_editEvaluations/" + id + "?curricularUnitId=" + curricularUnitId + "&error=Not enough available rooms to accommodate all students.";
        }

        assessmentUnit.setType(params.get("assessmentType"));
        assessmentUnit.setWeight(Integer.parseInt(params.get("assessmentWeight")));
        assessmentUnit.setExamPeriod(assessmentExamPeriod);
        assessmentUnit.setComputerRequired(computerRequired);
        assessmentUnit.setClassTime(classTime);
        assessmentUnit.setStartTime(startTime);
        assessmentUnit.setEndTime(endTime);
        assessmentUnit.setRooms(selectedRooms);
        assessmentUnit.setMinimumGrade(Double.parseDouble(params.get("assessmentMinimumGrade")));

        assessmentUnitService.updateAssessment(id, assessmentUnit, roomIds);

        return "redirect:/coordinator/coordinator_evaluationsUC?id=" + curricularUnitId;
    }

    @PostMapping("/delete-assessment/{id}")
    public String deleteAssessment(@PathVariable("id") Long id, @RequestParam("curricularUnitId") Long curricularUnitId, Model model, HttpSession session) {
        if (!isCoordinator(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        model.addAttribute("warning", "Are you sure you want to remove this assessment?");
        model.addAttribute("assessmentId", id);
        model.addAttribute("curricularUnitId", curricularUnitId);
        return "coordinator_confirmRemoveAssessment";
    }

    @PostMapping("/confirm-remove-assessment/{id}")
    public String confirmRemoveAssessment(@PathVariable("id") Long id, @RequestParam("curricularUnitId") Long curricularUnitId, HttpSession session) {
        if (!isCoordinator(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        assessmentUnitService.deleteAssessment(curricularUnitId, id);
        return "redirect:/coordinator/coordinator_evaluationsUC?id=" + curricularUnitId;
    }

    @PostMapping("/coordinator_delete_evaluation/{id}")
    public String deleteEvaluation(@PathVariable("id") Long id, @RequestParam("curricularUnitId") Long curricularUnitId, HttpSession session) {
        if (!isCoordinator(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        assessmentUnitService.deleteAssessment(curricularUnitId, id);
        return "redirect:/coordinator/coordinator_evaluationsUC?id=" + curricularUnitId;
    }

    @GetMapping("/getValidDateRanges")
    @ResponseBody
    public Map<String, String> getValidDateRanges(@RequestParam("examPeriod") String examPeriod, @RequestParam("curricularUnitId") Long curricularUnitId, HttpSession session) {
        if (!isCoordinator(session)) {
            throw new IllegalArgumentException("Unauthorized access");
        }

        CoordinatorUnit coordinator = verifyCoordinator(session)
                .orElseThrow(() -> new IllegalArgumentException("Coordinator not found"));
        DirectorUnit director = coordinator.getDirectorUnit();
        YearUnit currentYear = director.getCurrentYear();

        CurricularUnit curricularUnit = curricularUnitService.getCurricularUnitById(curricularUnitId)
                .orElseThrow(() -> new IllegalArgumentException("Curricular Unit not found"));

        return assessmentUnitService.getValidDateRanges(examPeriod, curricularUnit, currentYear);
    }

    @GetMapping("/availableRooms")
    @ResponseBody
    public List<RoomUnit> getAvailableRooms(@RequestParam("startTime") String startTimeStr, @RequestParam("endTime") String endTimeStr, @RequestParam("computerRequired") boolean computerRequired, @RequestParam("numStudents") int numStudents, HttpSession session) {
        if (!isCoordinator(session)) {
            throw new IllegalArgumentException("Unauthorized access");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime startTime = LocalDateTime.parse(startTimeStr, formatter);
        LocalDateTime endTime = LocalDateTime.parse(endTimeStr, formatter);

        return roomUnitService.getAvailableRooms(numStudents, computerRequired, startTime, endTime);
    }
}
