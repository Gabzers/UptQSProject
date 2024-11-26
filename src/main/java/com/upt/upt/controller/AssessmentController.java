package com.upt.upt.controller;

import com.upt.upt.entity.AssessmentUnit;
import com.upt.upt.service.AssessmentService;
import com.upt.upt.service.CurricularUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class AssessmentController {

    private final AssessmentService assessmentService;
    private final CurricularUnitService curricularUnitService;

    @Autowired
    public AssessmentController(AssessmentService assessmentService, CurricularUnitService curricularUnitService) {
        this.assessmentService = assessmentService;
        this.curricularUnitService = curricularUnitService;
    }

    // Mapeamento da URL "/assessments"
    @GetMapping("/assessments")
    public String showAssessmentList(Model model) {
        model.addAttribute("assessments", assessmentService.getAllAssessments());
        return "assessment_index"; // Retorna o nome do arquivo HTML "assessment_index.html"
    }

    // Remover a avaliação
    @PostMapping("/remove-assessment/{id}")
    public String removeAssessment(@PathVariable("id") Long id) {
        assessmentService.deleteAssessment(id); // Remove a avaliação do banco de dados
        return "redirect:/assessments"; // Redireciona para a lista de avaliações
    }

    // Página de edição de avaliação
    @GetMapping("/assessment_edit")
    public String editAssessment(@RequestParam("id") Long id, Model model) {
        Optional<AssessmentUnit> assessment = assessmentService.getAssessmentById(id);
        if (assessment.isPresent()) {
            model.addAttribute("assessment", assessment.get()); // Passa a avaliação para o modelo
            model.addAttribute("curricularUnits", curricularUnitService.getAllCurricularUnits()); // Passa todas as unidades curriculares
            return "assessment_edit"; // Retorna a página de edição
        } else {
            return "redirect:/assessments"; // Caso não encontre a avaliação, redireciona para a lista
        }
    }

    // Atualizar uma avaliação
    @PostMapping("/assessment_edit/{id}")
    public String updateAssessment(
            @PathVariable("id") Long id,
            @RequestParam("assessmentType") String type,
            @RequestParam("assessmentWeight") Integer weight,
            @RequestParam("assessmentExamPeriod") String examPeriod,
            @RequestParam("assessmentComputerRequired") Boolean computerRequired,
            @RequestParam("assessmentClassTime") Boolean classTime,
            @RequestParam("assessmentStartTime") String startTime,
            @RequestParam("assessmentEndTime") String endTime,
            @RequestParam("assessmentRoom") String room,
            @RequestParam("curricularUnitId") Long curricularUnitId) {

        try {
            AssessmentUnit assessment = assessmentService.getAssessmentById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid assessment ID: " + id));

            // Atualizar os campos da avaliação
            assessment.setType(type);
            assessment.setWeight(weight);
            assessment.setExamPeriod(examPeriod);
            assessment.setComputerRequired(computerRequired);
            assessment.setClassTime(classTime);
            assessment.setStartTime(LocalDateTime.parse(startTime)); // Convertendo de String para LocalDateTime
            assessment.setEndTime(LocalDateTime.parse(endTime)); // Convertendo de String para LocalDateTime
            assessment.setRoom(room);

            // Definir a CurricularUnit com base no ID
            curricularUnitService.getCurricularUnitById(curricularUnitId)
                    .ifPresent(assessment::setCurricularUnit);

            // Salvar a avaliação atualizada
            assessmentService.saveAssessment(assessment);

            // Redirecionar para a lista de avaliações após a atualização
            return "redirect:/assessments";
        } catch (Exception e) {
            // Logar o erro e retornar para a página de edição
            e.printStackTrace();
            return "redirect:/assessment_edit?id=" + id + "&error=true";
        }
    }

    // Criar nova avaliação
    @PostMapping("/create-assessment")
    public String createAssessment(
            @RequestParam("assessmentType") String type,
            @RequestParam("assessmentWeight") Integer weight,
            @RequestParam("assessmentExamPeriod") String examPeriod,
            @RequestParam("assessmentComputerRequired") Boolean computerRequired,
            @RequestParam("assessmentClassTime") Boolean classTime,
            @RequestParam("assessmentStartTime") String startTime,
            @RequestParam("assessmentEndTime") String endTime,
            @RequestParam("assessmentRoom") String room,
            @RequestParam("curricularUnitId") Long curricularUnitId) {

        // Criar a nova Assessment com os dados do formulário
        AssessmentUnit assessment = new AssessmentUnit();
        assessment.setType(type);
        assessment.setWeight(weight);
        assessment.setExamPeriod(examPeriod);
        assessment.setComputerRequired(computerRequired);
        assessment.setClassTime(classTime);
        assessment.setStartTime(LocalDateTime.parse(startTime)); // Convertendo de String para LocalDateTime
        assessment.setEndTime(LocalDateTime.parse(endTime)); // Convertendo de String para LocalDateTime
        assessment.setRoom(room);

        // Definir a CurricularUnit com base no ID
        curricularUnitService.getCurricularUnitById(curricularUnitId)
                .ifPresent(assessment::setCurricularUnit);

        // Salvar a Assessment no banco de dados
        assessmentService.saveAssessment(assessment);

        return "redirect:/assessments"; // Redirecionar para a página de avaliações após criar
    }
}
