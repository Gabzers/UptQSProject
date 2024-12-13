package com.upt.upt.controller;

import com.upt.upt.entity.*;
import com.upt.upt.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/coordinator")
public class AssessmentUnitController {

    @Autowired
    private AssessmentUnitService assessmentService;

    @Autowired
    private CurricularUnitService curricularUnitService;

    @Autowired
    private CoordinatorUnitService coordinatorUnitService;

    @Autowired
    private YearUnitService yearUnitService;

    @Autowired
    private RoomUnitService roomUnitService;

    private Optional<CoordinatorUnit> verifyCoordinator(HttpSession session) {
        Long coordinatorId = (Long) session.getAttribute("userId");
        if (coordinatorId == null) {
            return Optional.empty();
        }
        return coordinatorUnitService.getCoordinatorById(coordinatorId);
    }

    @GetMapping("/coordinator_create_evaluation")
    public String createEvaluationPage(@RequestParam("curricularUnitId") Long curricularUnitId, Model model, @RequestParam(value = "error", required = false) String error, HttpSession session) {
        if (verifyCoordinator(session).isEmpty()) {
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
        if (verifyCoordinator(session).isEmpty()) {
            return "redirect:/login?error=Unauthorized access";
        }
        Long curricularUnitId = Long.parseLong(params.get("curricularUnitId"));
        String assessmentExamPeriod = params.get("assessmentExamPeriod");
        if (assessmentExamPeriod == null || assessmentExamPeriod.isEmpty() || assessmentExamPeriod.equals("Select Exam Period")) {
            model.addAttribute("error", "Exam Period is required.");
            return "redirect:/coordinator/coordinator_create_evaluation?curricularUnitId=" + curricularUnitId;
        }

        Boolean computerRequired = params.get("assessmentComputerRequired") != null;
        Boolean classTime = params.get("assessmentClassTime") != null;
        LocalDateTime startTime = LocalDateTime.parse(params.get("assessmentStartTime"));
        LocalDateTime endTime = LocalDateTime.parse(params.get("assessmentEndTime"));

        Optional<CurricularUnit> curricularUnit = curricularUnitService.getCurricularUnitById(curricularUnitId);
        if (!curricularUnit.isPresent()) {
            return "redirect:/coordinator";
        }

        CurricularUnit uc = curricularUnit.get();
        if (uc.getEvaluationsCount() == uc.getAssessments().size()) {
            model.addAttribute("error", "Evaluations already complete.");
            return "redirect:/coordinator/coordinator_create_evaluation?curricularUnitId=" + curricularUnitId;
        }

        Long coordinatorId = (Long) session.getAttribute("userId");
        CoordinatorUnit coordinator = coordinatorUnitService.getCoordinatorById(coordinatorId)
                .orElseThrow(() -> new IllegalArgumentException("Coordinator not found"));
        DirectorUnit director = coordinator.getDirectorUnit();
        YearUnit currentYear = director.getCurrentYear();
        SemesterUnit semesterUnit = uc.getSemester() == 1 ? currentYear.getFirstSemester() : currentYear.getSecondSemester();

        if (!assessmentService.validateAssessmentDates(assessmentExamPeriod, startTime, endTime, semesterUnit, currentYear, model, curricularUnitId)) {
            return "redirect:/coordinator/coordinator_create_evaluation?curricularUnitId=" + curricularUnitId;
        }

        int periodTotalWeight = assessmentService.calculatePeriodTotalWeight(uc, assessmentExamPeriod, Integer.parseInt(params.get("assessmentWeight")));

        if (periodTotalWeight > 100) {
            model.addAttribute("error", "The total weight of evaluations for this period must not exceed 100%.");
            return "redirect:/coordinator/coordinator_create_evaluation?curricularUnitId=" + curricularUnitId;
        }

        if ("Mixed".equals(params.get("ucEvaluationType")) && uc.getAssessments().size() == uc.getEvaluationsCount() - 1 && !uc.hasExamPeriodEvaluation() && !"Exam Period".equals(assessmentExamPeriod)) {
            model.addAttribute("error", "For Mixed evaluation type, at least one evaluation must be of type 'Exam Period'.");
            return "redirect:/coordinator/coordinator_create_evaluation?curricularUnitId=" + curricularUnitId;
        }

        RoomUnit room = roomUnitService.getRoomById(Long.parseLong(params.get("assessmentRoomId")));
        if (room == null) {
            model.addAttribute("error", "Room not found.");
            return "redirect:/coordinator/coordinator_create_evaluation?curricularUnitId=" + curricularUnitId;
        }

        if (!assessmentService.isRoomAvailable(room.getId(), startTime, endTime)) {
            model.addAttribute("error", "The selected room is not available at the specified time.");
            return "redirect:/coordinator/coordinator_create_evaluation?curricularUnitId=" + curricularUnitId;
        }

        AssessmentUnit assessmentUnit = new AssessmentUnit();
        assessmentUnit.setType(params.get("assessmentType"));
        assessmentUnit.setWeight(Integer.parseInt(params.get("assessmentWeight")));
        assessmentUnit.setExamPeriod(assessmentExamPeriod);
        assessmentUnit.setComputerRequired(computerRequired);
        assessmentUnit.setClassTime(classTime);
        assessmentUnit.setStartTime(startTime);
        assessmentUnit.setEndTime(endTime);
        assessmentUnit.setRoom(room);
        assessmentUnit.setCurricularUnit(uc);
        assessmentUnit.setMinimumGrade(Double.parseDouble(params.get("assessmentMinimumGrade")));

        assessmentService.saveAssessment(assessmentUnit);

        return "redirect:/coordinator/coordinator_evaluationsUC?id=" + curricularUnitId;
    }

    @GetMapping("/coordinator_editEvaluations/{assessmentId}")
    public String editEvaluation(@PathVariable("assessmentId") Long assessmentId, @RequestParam("curricularUnitId") Long curricularUnitId, Model model, HttpSession session) {
        if (verifyCoordinator(session).isEmpty()) {
            return "redirect:/login?error=Unauthorized access";
        }
        Optional<CurricularUnit> curricularUnit = curricularUnitService.getCurricularUnitById(curricularUnitId);
        if (curricularUnit.isPresent()) {
            Optional<AssessmentUnit> assessment = assessmentService.getAssessmentByUnitAndId(curricularUnitId, assessmentId);
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
    public String updateEvaluation(@PathVariable("id") Long id, @RequestParam Map<String, String> params, Model model, HttpSession session) {
        if (verifyCoordinator(session).isEmpty()) {
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
        LocalDateTime startTime = LocalDateTime.parse(params.get("assessmentStartTime"));
        LocalDateTime endTime = LocalDateTime.parse(params.get("assessmentEndTime"));

        Optional<CurricularUnit> curricularUnit = curricularUnitService.getCurricularUnitById(curricularUnitId);
        if (!curricularUnit.isPresent()) {
            return "redirect:/coordinator";
        }

        CurricularUnit uc = curricularUnit.get();
        Optional<AssessmentUnit> assessmentUnitOptional = assessmentService.findById(id);
        if (!assessmentUnitOptional.isPresent()) {
            return "redirect:/coordinator";
        }

        AssessmentUnit assessmentUnit = assessmentUnitOptional.get();
        int periodTotalWeight = assessmentService.calculatePeriodTotalWeight(uc, assessmentExamPeriod, Integer.parseInt(params.get("assessmentWeight"))) - assessmentUnit.getWeight();

        if (periodTotalWeight > 100) {
            model.addAttribute("uc", uc);
            model.addAttribute("assessment", assessmentUnit);
            model.addAttribute("error", "The total weight of evaluations for this period must not exceed 100%.");
            return "redirect:/coordinator/coordinator_editEvaluations/" + id + "?curricularUnitId=" + curricularUnitId;
        }

        RoomUnit room = roomUnitService.getRoomById(Long.parseLong(params.get("assessmentRoomId")));
        if (room == null) {
            model.addAttribute("error", "Room not found.");
            return "redirect:/coordinator/coordinator_editEvaluations/" + id + "?curricularUnitId=" + curricularUnitId;
        }

        assessmentUnit.setType(params.get("assessmentType"));
        assessmentUnit.setWeight(Integer.parseInt(params.get("assessmentWeight")));
        assessmentUnit.setExamPeriod(assessmentExamPeriod);
        assessmentUnit.setComputerRequired(computerRequired);
        assessmentUnit.setClassTime(classTime);
        assessmentUnit.setStartTime(startTime);
        assessmentUnit.setEndTime(endTime);
        assessmentUnit.setRoom(room);
        assessmentUnit.setMinimumGrade(Double.parseDouble(params.get("assessmentMinimumGrade")));

        assessmentService.saveAssessment(assessmentUnit);

        return "redirect:/coordinator/coordinator_evaluationsUC?id=" + curricularUnitId;
    }

    @PostMapping("/coordinator_delete_evaluation/{id}")
    public String deleteEvaluation(@PathVariable("id") Long id, @RequestParam("curricularUnitId") Long curricularUnitId, HttpSession session) {
        if (verifyCoordinator(session).isEmpty()) {
            return "redirect:/login?error=Unauthorized access";
        }
        assessmentService.deleteAssessment(curricularUnitId, id);
        return "redirect:/coordinator/coordinator_evaluationsUC?id=" + curricularUnitId;
    }

    @GetMapping("/map")
    public String showAssessmentMap(Model model, HttpSession session) {
        Optional<CoordinatorUnit> coordinatorOpt = verifyCoordinator(session);
        if (coordinatorOpt.isPresent()) {
            CoordinatorUnit coordinator = coordinatorOpt.get();
            DirectorUnit director = coordinator.getDirectorUnit();
            YearUnit currentYear = director.getCurrentYear();
            if (currentYear != null) {
                List<CurricularUnit> coordinatorUnits = coordinator.getCurricularUnits();
                List<AssessmentUnit> firstSemesterAssessments = assessmentService.getAssessmentsBySemester(currentYear.getFirstSemester().getId()).stream()
                        .filter(assessment -> coordinatorUnits.contains(assessment.getCurricularUnit()))
                        .collect(Collectors.toList());
                List<AssessmentUnit> secondSemesterAssessments = assessmentService.getAssessmentsBySemester(currentYear.getSecondSemester().getId()).stream()
                        .filter(assessment -> coordinatorUnits.contains(assessment.getCurricularUnit()))
                        .collect(Collectors.toList());
                model.addAttribute("firstSemesterAssessments", firstSemesterAssessments);
                model.addAttribute("secondSemesterAssessments", secondSemesterAssessments);
                model.addAttribute("noNormalPeriodFirstSemester", assessmentService.noAssessmentsForPeriod(firstSemesterAssessments, "Teaching Period") && assessmentService.noAssessmentsForPeriod(firstSemesterAssessments, "Exam Period"));
                model.addAttribute("noResourcePeriodFirstSemester", assessmentService.noAssessmentsForPeriod(firstSemesterAssessments, "Resource Period"));
                model.addAttribute("noSpecialPeriodFirstSemester", assessmentService.noAssessmentsForPeriod(firstSemesterAssessments, "Special Period"));
                model.addAttribute("noNormalPeriodSecondSemester", assessmentService.noAssessmentsForPeriod(secondSemesterAssessments, "Teaching Period") && assessmentService.noAssessmentsForPeriod(secondSemesterAssessments, "Exam Period"));
                model.addAttribute("noResourcePeriodSecondSemester", assessmentService.noAssessmentsForPeriod(secondSemesterAssessments, "Resource Period"));
                model.addAttribute("noSpecialPeriodSecondSemester", assessmentService.noAssessmentsForPeriod(secondSemesterAssessments, "Special Period"));
            } else {
                return "redirect:/login?error=Current year not found";
            }
        } else {
            return "redirect:/login?error=Coordinator not found";
        }
        return "coordinator_map";
    }

    @GetMapping("/getValidDateRanges")
    @ResponseBody
    public Map<String, String> getValidDateRanges(@RequestParam("examPeriod") String examPeriod, @RequestParam("curricularUnitId") Long curricularUnitId, HttpSession session) {
        if (verifyCoordinator(session).isEmpty()) {
            throw new IllegalArgumentException("Unauthorized access");
        }

        CoordinatorUnit coordinator = verifyCoordinator(session)
                .orElseThrow(() -> new IllegalArgumentException("Coordinator not found"));
        DirectorUnit director = coordinator.getDirectorUnit();
        YearUnit currentYear = director.getCurrentYear();

        CurricularUnit curricularUnit = curricularUnitService.getCurricularUnitById(curricularUnitId)
                .orElseThrow(() -> new IllegalArgumentException("Curricular Unit not found"));

        return assessmentService.getValidDateRanges(examPeriod, curricularUnit, currentYear);
    }
}
