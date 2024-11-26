package com.upt.upt.controller;

import com.upt.upt.entity.AssessmentUnit;
import com.upt.upt.entity.CurricularUnit;
import com.upt.upt.entity.MapUnit;
import com.upt.upt.service.AssessmentService;
import com.upt.upt.service.CurricularUnitService;
import com.upt.upt.service.MapUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class AssessmentController {

    @Autowired
    private AssessmentService assessmentService;

    @Autowired
    private CurricularUnitService curricularUnitService;

    @Autowired
    private MapUnitService mapUnitService; // Para associar o MapUnit às avaliações

    // Página de avaliações da UC
    @GetMapping("/user_evaluationsUC")
    public String evaluationsUC(@RequestParam("id") Long id, Model model) {
        // Busca a UC pelo ID
        Optional<CurricularUnit> curricularUnit = curricularUnitService.getCurricularUnitById(id);
        if (curricularUnit.isPresent()) {
            // Passa a UC e as avaliações para o modelo
            model.addAttribute("uc", curricularUnit.get());
            model.addAttribute("evaluations", assessmentService.getAssessmentsByCurricularUnit(id)); // Supondo que você
                                                                                                     // tenha um método
                                                                                                     // para pegar as
                                                                                                     // avaliações
            return "user_evaluationsUC"; // Retorna a página de avaliações
        } else {
            return "redirect:/user"; // Caso não encontre a UC, redireciona para a lista de UCs
        }
    }

    @GetMapping("/user_create_evaluation")
    public String createEvaluationPage(@RequestParam("curricularUnitId") Long curricularUnitId, Model model) {
        // Busca a UC para associar à avaliação
        Optional<CurricularUnit> curricularUnit = curricularUnitService.getCurricularUnitById(curricularUnitId);
        if (curricularUnit.isPresent()) {
            model.addAttribute("uc", curricularUnit.get());
            // Recuperar todos os MapUnits (caso queira associar)
            model.addAttribute("mapUnits", mapUnitService.getAllMapUnits());
            return "user_addEvaluations"; // Formulário de criação de avaliação
        } else {
            return "redirect:/user"; // Caso não encontre a UC, redireciona
        }
    }

    // Salvar nova avaliação
    @PostMapping("/user_addEvaluation")
    public String saveEvaluation(@ModelAttribute AssessmentUnit assessmentUnit,
            @RequestParam("curricularUnitId") Long curricularUnitId,
            @RequestParam("mapUnitId") Long mapUnitId) {
        // Busca a UC e o MapUnit para associar à avaliação
        Optional<CurricularUnit> curricularUnit = curricularUnitService.getCurricularUnitById(curricularUnitId);
        Optional<MapUnit> mapUnit = mapUnitService.getMapUnitById(mapUnitId);

        if (curricularUnit.isPresent() && mapUnit.isPresent()) {
            assessmentUnit.setCurricularUnit(curricularUnit.get());
            assessmentUnit.setMap(mapUnit.get());
            assessmentService.saveAssessment(assessmentUnit); // Salva a avaliação
            return "redirect:/user_evaluationsUC?id=" + curricularUnitId; // Redireciona para a lista de avaliações da
                                                                          // UC
        } else {
            return "redirect:/user"; // Caso não encontre a UC ou o MapUnit, redireciona
        }
    }

    // Página para editar uma avaliação
    @GetMapping("/user_edit_evaluation")
    public String editEvaluationPage(@RequestParam("id") Long id, Model model) {
        Optional<AssessmentUnit> assessment = assessmentService.getAssessmentById(id);
        if (assessment.isPresent()) {
            model.addAttribute("assessment", assessment.get());
            model.addAttribute("mapUnits", mapUnitService.getAllMapUnits()); // Para editar MapUnit se necessário
            return "user_edit_evaluation"; // Formulário de edição
        } else {
            return "redirect:/user"; // Caso não encontre a avaliação, redireciona
        }
    }

    // Atualizar avaliação
    @PostMapping("/user_edit_evaluation")
    public String updateEvaluation(@ModelAttribute AssessmentUnit updatedAssessment, @RequestParam("id") Long id,
            @RequestParam("mapUnitId") Long mapUnitId) {
        Optional<MapUnit> mapUnit = mapUnitService.getMapUnitById(mapUnitId);
        if (mapUnit.isPresent()) {
            updatedAssessment.setMap(mapUnit.get());
            assessmentService.updateAssessment(id, updatedAssessment); // Atualiza a avaliação
            return "redirect:/user_evaluationsUC?id=" + updatedAssessment.getCurricularUnit().getId(); // Redireciona
                                                                                                       // para as
                                                                                                       // avaliações da
                                                                                                       // UC
        } else {
            return "redirect:/user"; // Caso não encontre o MapUnit, redireciona
        }
    }

    // Deletar avaliação
    @GetMapping("/user_delete_evaluation")
    public String deleteEvaluation(@RequestParam("id") Long id,
            @RequestParam("curricularUnitId") Long curricularUnitId) {
        assessmentService.deleteAssessment(id); // Deleta a avaliação
        return "redirect:/user_evaluationsUC?id=" + curricularUnitId; // Redireciona para a lista de avaliações da UC
    }
}
