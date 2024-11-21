package com.upt.upt.controller;

import com.upt.upt.entity.CurricularUnit;
import com.upt.upt.service.CurricularUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Optional;


@Controller
public class CurricularUnitController {

    private final CurricularUnitService curricularUnitService;

    @Autowired
    public CurricularUnitController(CurricularUnitService curricularUnitService) {
        this.curricularUnitService = curricularUnitService;
    }

    // Mapeamento da URL "/course-list"
    @GetMapping("/course-list")
    public String showCourseList(Model model) {
        model.addAttribute("curricularUnits", curricularUnitService.getAllCurricularUnits());
        return "course-list";  // Retorna o nome do arquivo HTML "course-list.html"
    }

    // Remover a UC
@PostMapping("/remove-uc/{id}")
public String removeCurricularUnit(@PathVariable("id") Long id) {
    curricularUnitService.deleteCurricularUnit(id); // Remove a UC do banco de dados
    return "redirect:/course-list"; // Redireciona para a lista de UCs
}

@GetMapping("/edit-uc")
public String editUC(@RequestParam("id") Long id, Model model) {
    Optional<CurricularUnit> curricularUnit = curricularUnitService.getCurricularUnitById(id);
    if (curricularUnit.isPresent()) {
        model.addAttribute("uc", curricularUnit.get()); // Passa a UC para o modelo
        return "edit-uc"; // Retorna a página de edição
    } else {
        return "redirect:/course-list"; // Caso não encontre a UC, redireciona para a lista
    }
}

@PostMapping("/edit-uc/{id}")
public String updateCurricularUnit(@PathVariable("id") Long id, 
                                   @RequestParam("nameUC") String nameUC, 
                                   @RequestParam("studentsNumber") Integer studentsNumber, 
                                   @RequestParam("typeUC") String typeUC) {
    try {
        CurricularUnit uc = curricularUnitService.getCurricularUnitById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid UC ID: " + id));
        
        // Atualizar os campos da UC
        uc.setNameUC(nameUC);
        uc.setStudentsNumber(studentsNumber);
        uc.setTypeUC(typeUC);

        // Salvar a UC atualizada
        curricularUnitService.saveCurricularUnit(uc);

        // Redirecionar para a lista de UCs após a atualização
        return "redirect:/course-list";
    } catch (Exception e) {
        // Logar o erro e retornar para a página de edição
        e.printStackTrace();
        return "redirect:/edit-uc/" + id + "?error=true";
    }
}
// Método POST para receber os dados do formulário
@PostMapping("/create-uc")
public String createUC(@RequestParam("uc-name") String nameUC,
                       @RequestParam("num-students") Integer studentsNumber,
                       @RequestParam("evaluation-type") String typeUC) {
    // Criar a nova CurricularUnit com os dados do formulário
    CurricularUnit curricularUnit = new CurricularUnit();
    curricularUnit.setNameUC(nameUC);
    curricularUnit.setStudentsNumber(studentsNumber);
    curricularUnit.setTypeUC(typeUC);

    // Salvar a CurricularUnit no banco de dados
    curricularUnitService.saveCurricularUnit(curricularUnit);

    return "redirect:/course-list";  // Redirecionar para a página de usuário após criar a UC
}


}
