package com.upt.upt.controller;

import com.upt.upt.entity.AssessmentUnit;
import com.upt.upt.entity.CurricularUnit;
import com.upt.upt.entity.MapUnit;
import com.upt.upt.entity.SemesterUnit;
import com.upt.upt.service.AssessmentUnitService;
import com.upt.upt.service.CurricularUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequestMapping("/coordinator")
public class AssessmentUnitController {

    @Autowired
    private AssessmentUnitService assessmentService;

    @Autowired
    private CurricularUnitService curricularUnitService;

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
            model.addAttribute("uc", curricularUnit.get());
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
            @RequestParam("assessmentMinimumGrade") Double assessmentMinimumGrade) {

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
            @RequestParam("assessmentMinimumGrade") Double assessmentMinimumGrade) {

        if (computerRequired == null) {
            computerRequired = false;
        }
        if (classTime == null) {
            classTime = false;
        }

        LocalDateTime startTime = LocalDateTime.parse(assessmentStartTime);
        LocalDateTime endTime = LocalDateTime.parse(assessmentEndTime);

        Optional<AssessmentUnit> assessmentUnitOptional = assessmentService.findById(id);
        if (!assessmentUnitOptional.isPresent()) {
            return "redirect:/coordinator";
        }

        AssessmentUnit assessmentUnit = assessmentUnitOptional.get();
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
            model.addAttribute("assessments", assessmentService.getAssessmentsByCoordinator(coordinatorId));
            return "coordinator_map";
        } else {
            return "redirect:/login?error=Session expired";
        }
    }
}
