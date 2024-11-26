package com.upt.upt.controller;

import com.upt.upt.entity.CurricularUnit;
import com.upt.upt.service.CurricularUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class CurricularUnitController {

    private final CurricularUnitService curricularUnitService;

    @Autowired
    public CurricularUnitController(CurricularUnitService curricularUnitService) {
        this.curricularUnitService = curricularUnitService;
    }

    // Mapeamento da URL "/user"
    @GetMapping("/user")
    public String showCourseList(Model model) {
        model.addAttribute("curricularUnits", curricularUnitService.getAllCurricularUnits());
        return "user_index"; // Retorna o nome do arquivo HTML "user_index.html"
    }

    // Remover a UC
    @PostMapping("/remove-uc/{id}")
    public String removeCurricularUnit(@PathVariable("id") Long id) {
        curricularUnitService.deleteCurricularUnit(id); // Remove a UC do banco de dados
        return "redirect:/user"; // Redireciona para a lista de UCs
    }

    // Página de edição de UC
    @GetMapping("/user_editUC")
    public String editUC(@RequestParam("id") Long id, Model model) {
        Optional<CurricularUnit> curricularUnit = curricularUnitService.getCurricularUnitById(id);
        if (curricularUnit.isPresent()) {
            model.addAttribute("uc", curricularUnit.get()); // Passa a UC para o modelo
            return "user_editUC"; // Retorna a página de edição
        } else {
            return "redirect:/user"; // Caso não encontre a UC, redireciona para a lista
        }
    }

    // Atualizar uma UC
    @PostMapping("/user_editUC/{id}")
    public String updateCurricularUnit(
            @PathVariable("id") Long id,
            @RequestParam("ucName") String nameUC,
            @RequestParam("ucNumStudents") Integer studentsNumber,
            @RequestParam("ucEvaluationType") String evaluationType,
            @RequestParam(value = "ucAttendance", defaultValue = "false") Boolean attendance, // Novo campo
            @RequestParam("ucEvaluationsCount") Integer evaluationsCount,
            @RequestParam("ucYear") Integer year,
            @RequestParam("ucSemester") Integer semester) {
        try {
            CurricularUnit uc = curricularUnitService.getCurricularUnitById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid UC ID: " + id));

            // Atualizar os campos da UC
            uc.setNameUC(nameUC);
            uc.setStudentsNumber(studentsNumber);
            uc.setEvaluationType(evaluationType);
            uc.setAttendance(attendance); // Atualiza o campo attendance
            uc.setEvaluationsCount(evaluationsCount);
            uc.setYear(year);
            uc.setSemester(semester);

            // Salvar a UC atualizada
            curricularUnitService.saveCurricularUnit(uc);

            // Redirecionar para a lista de UCs após a atualização
            return "redirect:/user";
        } catch (Exception e) {
            // Logar o erro e retornar para a página de edição
            e.printStackTrace();
            return "redirect:/user_editUC/" + id + "?error=true";
        }
    }

    // Criar nova UC
    @PostMapping("/create-uc")
public String createUC(
        @RequestParam("ucName") String nameUC,
        @RequestParam("ucNumStudents") Integer studentsNumber,
        @RequestParam("ucEvaluationType") String evaluationType,
        @RequestParam(value = "ucAttendance", defaultValue = "false") Boolean attendance, // Valor padrão
        @RequestParam("ucEvaluationsCount") Integer evaluationsCount,
        @RequestParam("ucYear") Integer year,
        @RequestParam("ucSemester") Integer semester) {
    // Criar a nova CurricularUnit com os dados do formulário
    CurricularUnit curricularUnit = new CurricularUnit();
    curricularUnit.setNameUC(nameUC);
    curricularUnit.setStudentsNumber(studentsNumber);
    curricularUnit.setEvaluationType(evaluationType);
    curricularUnit.setAttendance(attendance); // Definir o campo attendance
    curricularUnit.setEvaluationsCount(evaluationsCount);
    curricularUnit.setYear(year);
    curricularUnit.setSemester(semester);

    // Salvar a CurricularUnit no banco de dados
    curricularUnitService.saveCurricularUnit(curricularUnit);

    return "redirect:/user"; // Redirecionar para a página de usuário após criar a UC
}

}
