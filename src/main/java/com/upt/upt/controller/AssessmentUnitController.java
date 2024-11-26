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
            model.addAttribute("evaluations", assessmentService.getAssessmentsByCurricularUnit(id));
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
            @RequestParam("assessmentRoom") String assessmentRoom) {

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

        assessmentService.saveAssessment(assessmentUnit);

        return "redirect:/user_evaluationsUC?id=" + curricularUnitId;
    }

    @GetMapping("/user_editEvaluations/{id}")
public String editEvaluationPage(@PathVariable("id") Long id,
        @RequestParam("curricularUnitId") Long curricularUnitId, Model model) {
    Optional<AssessmentUnit> assessment = assessmentService.getAssessmentById(id);
    if (assessment.isPresent()) {
        model.addAttribute("assessment", assessment.get());
        model.addAttribute("curricularUnitId", curricularUnitId);  // Passando curricularUnitId para o formulário
        return "user_editEvaluations"; // Retorna a página de edição da avaliação
    } else {
        return "redirect:/user"; // Se a avaliação não existir, redireciona para a página de usuários
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
        @RequestParam("assessmentRoom") String assessmentRoom) {

    // Se os valores de boolean forem nulos, defina como falso
    if (computerRequired == null) {
        computerRequired = false;
    }
    if (classTime == null) {
        classTime = false;
    }

    // Converte as strings de data e hora para LocalDateTime
    LocalDateTime startTime = LocalDateTime.parse(assessmentStartTime);
    LocalDateTime endTime = LocalDateTime.parse(assessmentEndTime);

    // Recupera a avaliação existente
    Optional<AssessmentUnit> assessmentUnitOptional = assessmentService.getAssessmentById(id);
    if (!assessmentUnitOptional.isPresent()) {
        return "redirect:/user"; // Se a avaliação não for encontrada, redireciona para a página de usuários
    }

    // Atualiza os dados da avaliação
    AssessmentUnit assessmentUnit = assessmentUnitOptional.get();
    assessmentUnit.setType(assessmentType);
    assessmentUnit.setWeight(assessmentWeight);
    assessmentUnit.setExamPeriod(assessmentExamPeriod);
    assessmentUnit.setComputerRequired(computerRequired);
    assessmentUnit.setClassTime(classTime);
    assessmentUnit.setStartTime(startTime);
    assessmentUnit.setEndTime(endTime);
    assessmentUnit.setRoom(assessmentRoom);

    // Salva a avaliação atualizada
    assessmentService.saveAssessment(assessmentUnit);

    // Redireciona para a página de avaliações da UC
    return "redirect:/user_evaluationsUC?id=" + curricularUnitId;
}



    // Remover avaliação
    @PostMapping("/user_delete_evaluation/{id}")
    public String deleteEvaluation(@PathVariable("id") Long id,
            @RequestParam("curricularUnitId") Long curricularUnitId) {
        assessmentService.deleteAssessment(curricularUnitId, id);
        return "redirect:/user_evaluationsUC?id=" + curricularUnitId;
    }

}
