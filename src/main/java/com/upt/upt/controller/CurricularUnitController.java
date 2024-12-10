package com.upt.upt.controller;

import com.upt.upt.entity.CoordinatorUnit;
import com.upt.upt.entity.CurricularUnit;
import com.upt.upt.service.CurricularUnitService;
import com.upt.upt.service.CoordinatorUnitService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/coordinator")
public class CurricularUnitController {

    private final CurricularUnitService curricularUnitService;
    private final CoordinatorUnitService coordinatorUnitService;

    @Autowired
    public CurricularUnitController(CurricularUnitService curricularUnitService, CoordinatorUnitService coordinatorUnitService) {
        this.curricularUnitService = curricularUnitService;
        this.coordinatorUnitService = coordinatorUnitService;
    }

    // Mapeamento da URL "/coordinator"
    @GetMapping("")
    public String showCourseList(Model model, HttpSession session) {
        Long coordinatorId = (Long) session.getAttribute("userId");
        if (coordinatorId != null) {
            Optional<CoordinatorUnit> coordinatorOpt = coordinatorUnitService.getCoordinatorById(coordinatorId);
            if (coordinatorOpt.isPresent()) {
                CoordinatorUnit coordinator = coordinatorOpt.get();
                model.addAttribute("curricularUnits", coordinator.getCurricularUnits());
            } else {
                return "redirect:/login?error=Coordinator not found";
            }
        } else {
            return "redirect:/login?error=Session expired";
        }
        return "coordinator_index"; // Retorna o nome do arquivo HTML "coordinator_index.html"
    }

    // Remover a UC
    @PostMapping("/remove-uc/{id}")
    public String removeCurricularUnit(@PathVariable("id") Long id) {
        curricularUnitService.deleteCurricularUnit(id); // Remove a UC do banco de dados
        return "redirect:/coordinator"; // Redireciona para a lista de UCs
    }

    // Página de edição de UC
    @GetMapping("/coordinator_editUC")
    public String editUC(@RequestParam("id") Long id, Model model) {
        Optional<CurricularUnit> curricularUnit = curricularUnitService.getCurricularUnitById(id);
        if (curricularUnit.isPresent()) {
            model.addAttribute("uc", curricularUnit.get()); // Passa a UC para o modelo
            return "coordinator_editUC"; // Retorna a página de edição
        } else {
            return "redirect:/coordinator"; // Caso não encontre a UC, redireciona para a lista
        }
    }

    // Atualizar uma UC
    @PostMapping("/coordinator_editUC/{id}")
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
            return "redirect:/coordinator";
        } catch (Exception e) {
            // Logar o erro e retornar para a página de edição
            e.printStackTrace();
            return "redirect:/coordinator_editUC/" + id + "?error=true";
        }
    }

    // Página de criação de UC
    @GetMapping("/create-uc")
    public String createUC() {
        return "coordinator_createUC"; // Redireciona para a página coordinator_createUC.html
    }

    // Criar nova UC
    @PostMapping("/create-uc")
    public String createCurricularUnit(
            @RequestParam("ucName") String nameUC,
            @RequestParam("ucNumStudents") Integer studentsNumber,
            @RequestParam("ucEvaluationType") String evaluationType,
            @RequestParam(value = "ucAttendance", defaultValue = "false") Boolean attendance, // Valor padrão
            @RequestParam("ucEvaluationsCount") Integer evaluationsCount,
            @RequestParam("ucYear") Integer year,
            @RequestParam("ucSemester") Integer semester,
            HttpSession session) {
        // Criar a nova CurricularUnit com os dados do formulário
        CurricularUnit curricularUnit = new CurricularUnit();
        curricularUnit.setNameUC(nameUC);
        curricularUnit.setStudentsNumber(studentsNumber);
        curricularUnit.setEvaluationType(evaluationType);
        curricularUnit.setAttendance(attendance); // Definir o campo attendance
        curricularUnit.setEvaluationsCount(evaluationsCount);
        curricularUnit.setYear(year);
        curricularUnit.setSemester(semester);

        // Associar a UC com o coordenador
        Long coordinatorId = (Long) session.getAttribute("userId");
        Optional<CoordinatorUnit> coordinatorUnit = coordinatorUnitService.getCoordinatorById(coordinatorId);
        if (coordinatorUnit.isPresent()) {
            CoordinatorUnit coordinator = coordinatorUnit.get();
            coordinator.addCurricularUnit(curricularUnit); // Adicionar a UC à lista do coordenador
        } else {
            // Handle the case where the coordinator unit is not found
            return "redirect:/coordinator?error=Coordinator not found";
        }

        // Salvar a CurricularUnit no banco de dados
        curricularUnitService.saveCurricularUnit(curricularUnit);

        return "redirect:/coordinator"; // Redirecionar para a página de usuário após criar a UC
    }

}
