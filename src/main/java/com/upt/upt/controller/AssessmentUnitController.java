package com.upt.upt.controller;

import com.upt.upt.entity.AssessmentUnit;
import com.upt.upt.entity.CoordinatorUnit;
import com.upt.upt.entity.CurricularUnit;
import com.upt.upt.entity.DirectorUnit;
import com.upt.upt.entity.MapUnit;
import com.upt.upt.entity.SemesterUnit;
import com.upt.upt.entity.YearUnit;
import com.upt.upt.service.AssessmentUnitService;
import com.upt.upt.service.CoordinatorUnitService;
import com.upt.upt.service.CurricularUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import com.upt.upt.service.YearUnitService;
import com.upt.upt.service.SemesterUnitService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import com.upt.upt.entity.RoomUnit;
import com.upt.upt.service.RoomUnitService;

@Controller
@RequestMapping("/coordinator")
public class AssessmentUnitController {

    @Autowired
    private AssessmentUnitService assessmentService;

    @Autowired
    private CurricularUnitService curricularUnitService;
    @Autowired
    private SemesterUnitService semesterUnitService;
    @Autowired
    private CoordinatorUnitService coordinatorUnitService;

    @Autowired
    private YearUnitService yearUnitService;

    @Autowired
    private RoomUnitService roomUnitService;

    // Página para criar nova avaliação
    @GetMapping("/coordinator_create_evaluation")
    public String createEvaluationPage(@RequestParam("curricularUnitId") Long curricularUnitId, Model model, @RequestParam(value = "error", required = false) String error) {
        Optional<CurricularUnit> curricularUnit = curricularUnitService.getCurricularUnitById(curricularUnitId);
        if (curricularUnit.isPresent()) {
            CurricularUnit uc = curricularUnit.get();
            if (uc.getEvaluationsCount() == uc.getAssessments().size()) {
                return "redirect:/coordinator/coordinator_evaluationsUC?id=" + curricularUnitId + "&error=Evaluations already complete";
            }
            model.addAttribute("uc", uc);
            List<RoomUnit> rooms = roomUnitService.getAllRooms();
            model.addAttribute("rooms", rooms); // Add rooms to the model
            if (error != null) {
                model.addAttribute("error", error); // Add error message to the model
            }
            return "coordinator_addEvaluations";
        } else {
            return "redirect:/coordinator";
        }
    }

    @PostMapping("/coordinator_addEvaluation")
    public String saveEvaluation(@RequestParam("curricularUnitId") Long curricularUnitId,
            @RequestParam("assessmentType") String assessmentType,
            @RequestParam("assessmentWeight") Integer assessmentWeight,
            @RequestParam("assessmentExamPeriod") String assessmentExamPeriod,
            @RequestParam(value = "assessmentComputerRequired", required = false) Boolean computerRequired,
            @RequestParam(value = "assessmentClassTime", required = false) Boolean classTime,
            @RequestParam("assessmentStartTime") String assessmentStartTime,
            @RequestParam("assessmentEndTime") String assessmentEndTime,
            @RequestParam("assessmentRoomId") Long assessmentRoomId,
            @RequestParam("assessmentMinimumGrade") Double assessmentMinimumGrade,
            @RequestParam("ucEvaluationType") String ucEvaluationType,
            HttpSession session,
            Model model) {

        if (assessmentExamPeriod == null || assessmentExamPeriod.isEmpty() || assessmentExamPeriod.equals("Select Exam Period")) {
            model.addAttribute("error", "Exam Period is required.");
            return "redirect:/coordinator/coordinator_create_evaluation?curricularUnitId=" + curricularUnitId;
        }

        if (computerRequired == null) {
            computerRequired = false;
        }
        if (classTime == null) {
            classTime = false;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime startTime = LocalDateTime.parse(assessmentStartTime, formatter);
        LocalDateTime endTime = LocalDateTime.parse(assessmentEndTime, formatter);

        Optional<CurricularUnit> curricularUnit = curricularUnitService.getCurricularUnitById(curricularUnitId);
        if (!curricularUnit.isPresent()) {
            return "redirect:/coordinator";
        }

        CurricularUnit uc = curricularUnit.get();
        if (uc.getEvaluationsCount() == uc.getAssessments().size()) {
            model.addAttribute("error", "Evaluations already complete.");
            return "redirect:/coordinator/coordinator_create_evaluation?curricularUnitId=" + curricularUnitId;
        }

        // Fetch the current year and the corresponding semester
        Long coordinatorId = (Long) session.getAttribute("userId");
        CoordinatorUnit coordinator = coordinatorUnitService.getCoordinatorById(coordinatorId)
                .orElseThrow(() -> new IllegalArgumentException("Coordinator not found"));
        DirectorUnit director = coordinator.getDirectorUnit();
        YearUnit currentYear = director.getCurrentYear();

        SemesterUnit semesterUnit = uc.getSemester() == 1 ? currentYear.getFirstSemester() : currentYear.getSecondSemester();

        // Validate assessment dates based on the exam period
        if ("Teaching Period".equals(assessmentExamPeriod) || "Exam Period".equals(assessmentExamPeriod)) {
            LocalDate semesterStart = LocalDate.parse(semesterUnit.getStartDate());
            LocalDate semesterEnd = LocalDate.parse(semesterUnit.getEndDate());
            if (startTime.toLocalDate().isBefore(semesterStart) || endTime.toLocalDate().isAfter(semesterEnd)) {
                model.addAttribute("error", "Assessment dates must be within the semester dates.");
                return "redirect:/coordinator/coordinator_create_evaluation?curricularUnitId=" + curricularUnitId;
            }
        } else if ("Resource Period".equals(assessmentExamPeriod)) {
            LocalDate resitStart = LocalDate.parse(semesterUnit.getResitPeriodStart());
            LocalDate resitEnd = LocalDate.parse(semesterUnit.getResitPeriodEnd());
            if (startTime.toLocalDate().isBefore(resitStart) || endTime.toLocalDate().isAfter(resitEnd)) {
                model.addAttribute("error", "Assessment dates must be within the resit period dates.");
                return "redirect:/coordinator/coordinator_create_evaluation?curricularUnitId=" + curricularUnitId;
            }
        } else if ("Special Period".equals(assessmentExamPeriod)) {
            LocalDate specialStart = LocalDate.parse(currentYear.getSpecialExamStart());
            LocalDate specialEnd = LocalDate.parse(currentYear.getSpecialExamEnd());
            if (startTime.toLocalDate().isBefore(specialStart) || endTime.toLocalDate().isAfter(specialEnd)) {
                model.addAttribute("error", "Assessment dates must be within the special period dates.");
                return "redirect:/coordinator/coordinator_create_evaluation?curricularUnitId=" + curricularUnitId;
            }
        }

        int periodTotalWeight = uc.getAssessments().stream()
                .filter(e -> assessmentExamPeriod.equals(e.getExamPeriod()))
                .mapToInt(AssessmentUnit::getWeight)
                .sum() + assessmentWeight;

        if (periodTotalWeight > 100) {
            model.addAttribute("error", "The total weight of evaluations for this period must not exceed 100%.");
            return "redirect:/coordinator/coordinator_create_evaluation?curricularUnitId=" + curricularUnitId;
        }

        if ("Mixed".equals(ucEvaluationType) && uc.getAssessments().size() == uc.getEvaluationsCount() - 1 && !uc.hasExamPeriodEvaluation() && !"Exam Period".equals(assessmentExamPeriod)) {
            model.addAttribute("error", "For Mixed evaluation type, at least one evaluation must be of type 'Exam Period'.");
            return "redirect:/coordinator/coordinator_create_evaluation?curricularUnitId=" + curricularUnitId;
        }

        RoomUnit room = roomUnitService.getRoomById(assessmentRoomId);
        if (room == null) {
            model.addAttribute("error", "Room not found.");
            return "redirect:/coordinator/coordinator_create_evaluation?curricularUnitId=" + curricularUnitId;
        }

        // Verificar a disponibilidade da sala
        if (!assessmentService.isRoomAvailable(assessmentRoomId, startTime, endTime)) {
            model.addAttribute("error", "The selected room is not available at the specified time.");
            return "redirect:/coordinator/coordinator_create_evaluation?curricularUnitId=" + curricularUnitId;
        }

        // Cria e configura a nova avaliação
        AssessmentUnit assessmentUnit = new AssessmentUnit();
        assessmentUnit.setType(assessmentType);
        assessmentUnit.setWeight(assessmentWeight);
        assessmentUnit.setExamPeriod(assessmentExamPeriod);
        assessmentUnit.setComputerRequired(computerRequired);
        assessmentUnit.setClassTime(classTime);
        assessmentUnit.setStartTime(startTime);
        assessmentUnit.setEndTime(endTime);
        assessmentUnit.setRoom(room);
        assessmentUnit.setCurricularUnit(curricularUnit.get());
        assessmentUnit.setMinimumGrade(assessmentMinimumGrade);

        // Fetch the semester of the UC and add the assessment to the map of that semester
        MapUnit mapUnit = semesterUnit.getMapUnit();
        mapUnit.getAssessments().add(assessmentUnit);
        assessmentUnit.setMap(mapUnit);

        assessmentService.saveAssessment(assessmentUnit); // Salva no banco de dados

        return "redirect:/coordinator/coordinator_evaluationsUC?id=" + curricularUnitId;
    }

    @GetMapping("/coordinator_editEvaluations/{assessmentId}")
    public String editEvaluation(@PathVariable("assessmentId") Long assessmentId,
                                 @RequestParam("curricularUnitId") Long curricularUnitId,
                                 Model model) {
        // Recupera a unidade curricular
        Optional<CurricularUnit> curricularUnit = curricularUnitService.getCurricularUnitById(curricularUnitId);

        if (curricularUnit.isPresent()) {
            // Recupera a avaliação correspondente ao assessmentId
            Optional<AssessmentUnit> assessment = assessmentService.getAssessmentByUnitAndId(curricularUnitId, assessmentId);

            if (assessment.isPresent()) {
                AssessmentUnit assessmentUnit = assessment.get();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                String formattedStartTime = assessmentUnit.getStartTime().format(formatter);
                String formattedEndTime = assessmentUnit.getEndTime().format(formatter);

                model.addAttribute("assessment", assessmentUnit);  // Adiciona o objeto assessment ao modelo
                model.addAttribute("formattedStartTime", formattedStartTime); // Adiciona a data formatada ao modelo
                model.addAttribute("formattedEndTime", formattedEndTime); // Adiciona a data formatada ao modelo
                model.addAttribute("uc", curricularUnit.get());      // Adiciona a unidade curricular ao modelo
                return "coordinator_editEvaluations"; // Página para adicionar/editar avaliação
            } else {
                return "redirect:/coordinator"; // Redireciona caso a avaliação não exista
            }
        } else {
            return "redirect:/coordinator"; // Caso a unidade curricular não exista, redireciona
        }
    }

    @PostMapping("/coordinator_editEvaluations/{id}")
    public String updateEvaluation(
            @PathVariable("id") Long id,
            @RequestParam("curricularUnitId") Long curricularUnitId,
            @RequestParam("assessmentType") String assessmentType,
            @RequestParam("assessmentWeight") Integer assessmentWeight,
            @RequestParam("assessmentExamPeriod") String assessmentExamPeriod,
            @RequestParam(value = "assessmentComputerRequired", required = false) Boolean computerRequired,
            @RequestParam(value = "assessmentClassTime", required = false) Boolean classTime,
            @RequestParam("assessmentStartTime") String assessmentStartTime,
            @RequestParam("assessmentEndTime") String assessmentEndTime,
            @RequestParam("assessmentRoomId") Long assessmentRoomId,
            @RequestParam("assessmentMinimumGrade") Double assessmentMinimumGrade,
            Model model) {

        if (assessmentExamPeriod == null || assessmentExamPeriod.isEmpty() || assessmentExamPeriod.equals("Select Exam Period")) {
            model.addAttribute("error", "Exam Period is required.");
            return "redirect:/coordinator/coordinator_editEvaluations/" + id + "?curricularUnitId=" + curricularUnitId;
        }

        if (computerRequired == null) {
            computerRequired = false;
        }
        if (classTime == null) {
            classTime = false;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime startTime = LocalDateTime.parse(assessmentStartTime, formatter);
        LocalDateTime endTime = LocalDateTime.parse(assessmentEndTime, formatter);

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
        int periodTotalWeight = uc.getAssessments().stream()
                .filter(e -> assessmentExamPeriod.equals(e.getExamPeriod()))
                .mapToInt(AssessmentUnit::getWeight)
                .sum() - assessmentUnit.getWeight() + assessmentWeight;

        if (periodTotalWeight > 100) {
            model.addAttribute("uc", uc);
            model.addAttribute("assessment", assessmentUnit);
            model.addAttribute("error", "The total weight of evaluations for this period must not exceed 100%.");
            return "redirect:/coordinator/coordinator_editEvaluations/" + id + "?curricularUnitId=" + curricularUnitId;
        }

        RoomUnit room = roomUnitService.getRoomById(assessmentRoomId);
        if (room == null) {
            model.addAttribute("error", "Room not found.");
            return "redirect:/coordinator/coordinator_editEvaluations/" + id + "?curricularUnitId=" + curricularUnitId;
        }

        assessmentUnit.setType(assessmentType);
        assessmentUnit.setWeight(assessmentWeight);
        assessmentUnit.setExamPeriod(assessmentExamPeriod);
        assessmentUnit.setComputerRequired(computerRequired);
        assessmentUnit.setClassTime(classTime);
        assessmentUnit.setStartTime(startTime);
        assessmentUnit.setEndTime(endTime);
        assessmentUnit.setRoom(room);
        assessmentUnit.setMinimumGrade(assessmentMinimumGrade);

        assessmentService.saveAssessment(assessmentUnit); // Atualiza a avaliação no banco

        return "redirect:/coordinator/coordinator_evaluationsUC?id=" + curricularUnitId;
    }

    @PostMapping("/coordinator_delete_evaluation/{id}")
    public String deleteEvaluation(@PathVariable("id") Long id,
                                   @RequestParam("curricularUnitId") Long curricularUnitId) {
        assessmentService.deleteAssessment(curricularUnitId, id); // Deleta a avaliação
        return "redirect:/coordinator/coordinator_evaluationsUC?id=" + curricularUnitId;
    }

    private boolean noAssessmentsForPeriod(List<AssessmentUnit> assessments, String period) {
        return assessments.stream().noneMatch(a -> period.equals(a.getExamPeriod()));
    }

    @GetMapping("/map")
    public String showAssessmentMap(Model model, HttpSession session) {
        Long coordinatorId = (Long) session.getAttribute("userId");
        if (coordinatorId != null) {
            Optional<CoordinatorUnit> coordinatorOpt = coordinatorUnitService.getCoordinatorById(coordinatorId);
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
                    model.addAttribute("noNormalPeriodFirstSemester", noAssessmentsForPeriod(firstSemesterAssessments, "Teaching Period") && noAssessmentsForPeriod(firstSemesterAssessments, "Exam Period"));
                    model.addAttribute("noResourcePeriodFirstSemester", noAssessmentsForPeriod(firstSemesterAssessments, "Resource Period"));
                    model.addAttribute("noSpecialPeriodFirstSemester", noAssessmentsForPeriod(firstSemesterAssessments, "Special Period"));
                    model.addAttribute("noNormalPeriodSecondSemester", noAssessmentsForPeriod(secondSemesterAssessments, "Teaching Period") && noAssessmentsForPeriod(secondSemesterAssessments, "Exam Period"));
                    model.addAttribute("noResourcePeriodSecondSemester", noAssessmentsForPeriod(secondSemesterAssessments, "Resource Period"));
                    model.addAttribute("noSpecialPeriodSecondSemester", noAssessmentsForPeriod(secondSemesterAssessments, "Special Period"));
                } else {
                    return "redirect:/login?error=Current year not found";
                }
            } else {
                return "redirect:/login?error=Coordinator not found";
            }
        } else {
            return "redirect:/login?error=Session expired";
        }
        return "coordinator_map";
    }

    @GetMapping("/getValidDateRanges")
    @ResponseBody
    public Map<String, String> getValidDateRanges(@RequestParam("examPeriod") String examPeriod, HttpSession session) {
        Long coordinatorId = (Long) session.getAttribute("userId");
        if (coordinatorId == null) {
            throw new IllegalArgumentException("User ID is not present in the session");
        }

        CoordinatorUnit coordinator = coordinatorUnitService.getCoordinatorById(coordinatorId)
                .orElseThrow(() -> new IllegalArgumentException("Coordinator not found"));
        DirectorUnit director = coordinator.getDirectorUnit();
        YearUnit currentYear = director.getCurrentYear();

        SemesterUnit firstSemester = currentYear.getFirstSemester();
        SemesterUnit secondSemester = currentYear.getSecondSemester();

        Map<String, String> validDateRanges = new HashMap<>();
        switch (examPeriod) {
            case "Teaching Period":
                validDateRanges.put("start", firstSemester.getStartDate());
                validDateRanges.put("end", firstSemester.getEndDate());
                break;
            case "Exam Period":
                validDateRanges.put("start", firstSemester.getExamPeriodStart());
                validDateRanges.put("end", firstSemester.getExamPeriodEnd());
                break;
            case "Resource Period":
                validDateRanges.put("start", firstSemester.getResitPeriodStart());
                validDateRanges.put("end", firstSemester.getResitPeriodEnd());
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
}
