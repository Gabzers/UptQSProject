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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    // Página de avaliações da UC
    // Página de avaliações da UC
    @GetMapping("/coordinator_evaluationsUC")
    public String evaluationsUC(@RequestParam("id") Long id, Model model) {
        Optional<CurricularUnit> curricularUnit = curricularUnitService.getCurricularUnitById(id);
        if (curricularUnit.isPresent()) {
            model.addAttribute("uc", curricularUnit.get());
            model.addAttribute("evaluations", assessmentService.getAssessmentsByCurricularUnit(id)); // Método atualizado
            return "coordinator_evaluationsUC";
        } else {
            return "redirect:/coordinator";
        }
    }

    // Página para criar nova avaliação
    @GetMapping("/coordinator_create_evaluation")
    public String createEvaluationPage(@RequestParam("curricularUnitId") Long curricularUnitId, Model model) {
        Optional<CurricularUnit> curricularUnit = curricularUnitService.getCurricularUnitById(curricularUnitId);
        if (curricularUnit.isPresent()) {
            CurricularUnit uc = curricularUnit.get();
            if (uc.getEvaluationsCount() == uc.getAssessments().size()) {
                return "redirect:/coordinator/coordinator_evaluationsUC?id=" + curricularUnitId + "&error=Evaluations already complete";
            }
            model.addAttribute("uc", uc);
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
            @RequestParam("assessmentRoom") String assessmentRoom,
            @RequestParam("assessmentMinimumGrade") Double assessmentMinimumGrade,
            @RequestParam("ucEvaluationType") String ucEvaluationType,
            Model model) {

        if (assessmentExamPeriod == null || assessmentExamPeriod.isEmpty() || assessmentExamPeriod.equals("Select Exam Period")) {
            model.addAttribute("error", "Exam Period is required.");
            return "coordinator_addEvaluations";
        }

        if (computerRequired == null) {
            computerRequired = false;
        }
        if (classTime == null) {
            classTime = false;
        }

        LocalDateTime startTime = LocalDateTime.parse(assessmentStartTime);
        LocalDateTime endTime = LocalDateTime.parse(assessmentEndTime);

        Optional<CurricularUnit> curricularUnit = curricularUnitService.getCurricularUnitById(curricularUnitId);
        if (!curricularUnit.isPresent()) {
            return "redirect:/coordinator";
        }

        CurricularUnit uc = curricularUnit.get();
        if (uc.getEvaluationsCount() == uc.getAssessments().size()) {
            model.addAttribute("uc", uc);
            model.addAttribute("error", "Evaluations already complete.");
            return "coordinator_addEvaluations";
        }

        int totalWeight = uc.getAssessments().stream().mapToInt(AssessmentUnit::getWeight).sum() + assessmentWeight;
        if (totalWeight > 100) {
            model.addAttribute("uc", uc);
            model.addAttribute("error", "The total weight of all evaluations must not exceed 100%.");
            return "coordinator_addEvaluations";
        }

        if ("Mixed".equals(ucEvaluationType) && uc.getAssessments().size() == uc.getEvaluationsCount() - 1 && !uc.hasExamPeriodEvaluation() && !"Exam Period".equals(assessmentExamPeriod)) {
            model.addAttribute("uc", uc);
            model.addAttribute("error", "For Mixed evaluation type, at least one evaluation must be of type 'Exam Period'.");
            return "coordinator_addEvaluations";
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
        assessmentUnit.setRoom(assessmentRoom);
        assessmentUnit.setCurricularUnit(curricularUnit.get());
        assessmentUnit.setMinimumGrade(assessmentMinimumGrade);

        // Fetch the semester of the UC and add the assessment to the map of that semester
        SemesterUnit semesterUnit = curricularUnit.get().getSemesterUnit();
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
                model.addAttribute("assessment", assessment.get());  // Adiciona o objeto assessment ao modelo
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
            @RequestParam("assessmentRoom") String assessmentRoom,
            @RequestParam("assessmentMinimumGrade") Double assessmentMinimumGrade,
            Model model) {

        if (assessmentExamPeriod == null || assessmentExamPeriod.isEmpty() || assessmentExamPeriod.equals("Select Exam Period")) {
            model.addAttribute("error", "Exam Period is required.");
            return "coordinator_editEvaluations";
        }

        if (computerRequired == null) {
            computerRequired = false;
        }
        if (classTime == null) {
            classTime = false;
        }

        LocalDateTime startTime = LocalDateTime.parse(assessmentStartTime);
        LocalDateTime endTime = LocalDateTime.parse(assessmentEndTime);

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
        int totalWeight = uc.getAssessments().stream().mapToInt(AssessmentUnit::getWeight).sum() - assessmentUnit.getWeight() + assessmentWeight;
        if (totalWeight > 100) {
            model.addAttribute("uc", uc);
            model.addAttribute("assessment", assessmentUnit);
            model.addAttribute("error", "The total weight of all evaluations must not exceed 100%.");
            return "coordinator_editEvaluations";
        }
        assessmentUnit.setType(assessmentType);
        assessmentUnit.setWeight(assessmentWeight);
        assessmentUnit.setExamPeriod(assessmentExamPeriod);
        assessmentUnit.setComputerRequired(computerRequired);
        assessmentUnit.setClassTime(classTime);
        assessmentUnit.setStartTime(startTime);
        assessmentUnit.setEndTime(endTime);
        assessmentUnit.setRoom(assessmentRoom);
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

    @GetMapping("/map")
    public String showAssessmentMap(Model model, HttpSession session) {
        Long coordinatorId = (Long) session.getAttribute("userId");
        if (coordinatorId != null) {
            Optional<CoordinatorUnit> coordinatorOpt = coordinatorUnitService.getCoordinatorById(coordinatorId);
            if (coordinatorOpt.isPresent()) {
                CoordinatorUnit coordinator = coordinatorOpt.get();
                DirectorUnit director = coordinator.getDirectorUnit();
                List<YearUnit> directorYears = director.getPastYears();
                Optional<YearUnit> currentYearOpt = directorYears.stream()
                        .max((y1, y2) -> y1.getFirstSemester().getStartDate().compareTo(y2.getFirstSemester().getStartDate()));
                if (currentYearOpt.isPresent()) {
                    YearUnit currentYear = currentYearOpt.get();
                    List<AssessmentUnit> firstSemesterAssessments = assessmentService.getAssessmentsBySemester(currentYear.getFirstSemester().getId());
                    List<AssessmentUnit> secondSemesterAssessments = assessmentService.getAssessmentsBySemester(currentYear.getSecondSemester().getId());
                    model.addAttribute("firstSemesterAssessments", firstSemesterAssessments);
                    model.addAttribute("secondSemesterAssessments", secondSemesterAssessments);
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
}
