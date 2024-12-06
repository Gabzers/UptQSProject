package com.upt.upt.controller;

import com.upt.upt.entity.AssessmentUnit;
import com.upt.upt.entity.CurricularUnit;
import com.upt.upt.service.AssessmentUnitService;
import com.upt.upt.service.CurricularUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class AssessmentUnitController {

    @Autowired
    private AssessmentUnitService assessmentService;

    @Autowired
    private CurricularUnitService curricularUnitService;

    // Página de avaliações da UC
    @GetMapping("/user_evaluationsUC")
    public String evaluationsUC(@RequestParam("id") Long id, Model model) {
        Optional<CurricularUnit> curricularUnit = curricularUnitService.getCurricularUnitById(id);
        if (curricularUnit.isPresent()) {
            model.addAttribute("uc", curricularUnit.get());
            model.addAttribute("evaluations", assessmentService.getAssessmentsByCurricularUnit(id)); // Método atualizado
            return "user_evaluationsUC";
        } else {
            return "redirect:/user";
        }
    }

    // Página para criar nova avaliação
    @GetMapping("/user_create_evaluation")
    public String createEvaluationPage(@RequestParam("curricularUnitId") Long curricularUnitId, Model model) {
        Optional<CurricularUnit> curricularUnit = curricularUnitService.getCurricularUnitById(curricularUnitId);
        if (curricularUnit.isPresent()) {
            model.addAttribute("uc", curricularUnit.get());
            return "user_addEvaluations";
        } else {
            return "redirect:/user";
        }
    }

    @PostMapping("/user_addEvaluation")
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
            return "redirect:/user";
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

        assessmentService.saveAssessment(assessmentUnit); // Salva no banco de dados

        return "redirect:/user_evaluationsUC?id=" + curricularUnitId;
    }

    @GetMapping("/user_editEvaluations/{assessmentId}")
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
                return "user_editEvaluations"; // Página para adicionar/editar avaliação
            } else {
                return "redirect:/user"; // Redireciona caso a avaliação não exista
            }
        } else {
            return "redirect:/user"; // Caso a unidade curricular não exista, redireciona
        }
    }

    @PostMapping("/user_editEvaluations/{id}")
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
            return "redirect:/user";
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

        return "redirect:/user_evaluationsUC?id=" + curricularUnitId;
    }

    @PostMapping("/user_delete_evaluation/{id}")
    public String deleteEvaluation(@PathVariable("id") Long id,
                                   @RequestParam("curricularUnitId") Long curricularUnitId) {
        assessmentService.deleteAssessment(curricularUnitId, id); // Deleta a avaliação
        return "redirect:/user_evaluationsUC?id=" + curricularUnitId;
    }
}
