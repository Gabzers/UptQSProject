package com.upt.upt.controller;

import com.upt.upt.entity.CoordinatorUnit;
import com.upt.upt.entity.CurricularUnit;
import com.upt.upt.entity.DirectorUnit;
import com.upt.upt.entity.UserType;
import com.upt.upt.entity.YearUnit;
import com.upt.upt.service.CurricularUnitService;
import com.upt.upt.service.CoordinatorUnitService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Comparator;

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

    private Optional<CoordinatorUnit> verifyCoordinator(HttpSession session) {
        Long coordinatorId = (Long) session.getAttribute("userId");
        if (coordinatorId == null) {
            return Optional.empty();
        }
        return coordinatorUnitService.getCoordinatorById(coordinatorId);
    }

    private boolean isCoordinator(HttpSession session) {
        UserType userType = (UserType) session.getAttribute("userType");
        return userType == UserType.COORDINATOR;
    }

    // Mapeamento da URL "/coordinator"
    @GetMapping("")
    public String showCourseList(Model model, HttpSession session) {
        if (!isCoordinator(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        Optional<CoordinatorUnit> coordinatorOpt = verifyCoordinator(session);
        if (coordinatorOpt.isEmpty()) {
            return "redirect:/login?error=Unauthorized access";
        }
        CoordinatorUnit coordinator = coordinatorOpt.get();
        DirectorUnit director = coordinator.getDirectorUnit();
        YearUnit currentYear = director.getCurrentYear();
        if (currentYear != null) {
            List<CurricularUnit> firstSemesterUnits = currentYear.getFirstSemester().getCurricularUnits().stream()
                    .filter(uc -> uc.getCoordinator().equals(coordinator) && uc.getSemester() == 1)
                    .sorted(Comparator.comparing(CurricularUnit::getYear))
                    .collect(Collectors.toList());
            List<CurricularUnit> secondSemesterUnits = currentYear.getSecondSemester().getCurricularUnits().stream()
                    .filter(uc -> uc.getCoordinator().equals(coordinator) && uc.getSemester() == 2)
                    .sorted(Comparator.comparing(CurricularUnit::getYear))
                    .collect(Collectors.toList());
            model.addAttribute("firstSemesterUnits", firstSemesterUnits);
            model.addAttribute("secondSemesterUnits", secondSemesterUnits);
        } else {
            model.addAttribute("firstSemesterUnits", List.of());
            model.addAttribute("secondSemesterUnits", List.of());
        }
        return "coordinator_index"; // Retorna o nome do arquivo HTML "coordinator_index.html"
    }

    // Remover a UC
    @PostMapping("/remove-uc/{id}")
    public String removeCurricularUnit(@PathVariable("id") Long id, Model model, HttpSession session) {
        if (!isCoordinator(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        model.addAttribute("warning", "Are you sure you want to remove this UC?");
        model.addAttribute("ucId", id);
        return "coordinator_confirmRemoveUC";
    }

    @PostMapping("/confirm-remove-uc/{id}")
    public String confirmRemoveCurricularUnit(@PathVariable("id") Long id, HttpSession session) {
        if (!isCoordinator(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        curricularUnitService.deleteCurricularUnit(id);
        return "redirect:/coordinator";
    }

    // Página de edição de UC
    @GetMapping("/coordinator_editUC")
    public String editUCForm(@RequestParam("id") Long id, @RequestParam("semester") Integer semester, HttpSession session, Model model) {
        if (!isCoordinator(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        Optional<CurricularUnit> uc = curricularUnitService.getCurricularUnitById(id);
        Optional<CoordinatorUnit> coordinator = verifyCoordinator(session);
        if (uc.isPresent() && coordinator.isPresent()) {
            model.addAttribute("uc", uc.get());
            model.addAttribute("coordinator", coordinator.get());
            return "coordinator_editUC";
        } else {
            return "redirect:/coordinator";
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
            @RequestParam("ucSemester") Integer semester,
            HttpSession session,
            Model model) {
        if (!isCoordinator(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        return curricularUnitService.updateCurricularUnit(id, nameUC, studentsNumber, evaluationType, attendance, evaluationsCount, year, semester, session, model);
    }

    // Página de criação de UC
    @GetMapping("/create-uc")
    public String createUC(HttpSession session, Model model) {
        if (!isCoordinator(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        Long coordinatorId = (Long) session.getAttribute("userId");
        Optional<CoordinatorUnit> coordinator = coordinatorUnitService.getCoordinatorById(coordinatorId);
        if (coordinator.isPresent()) {
            model.addAttribute("coordinator", coordinator.get());
            model.addAttribute("uc", new CurricularUnit());
            return "coordinator_createUC";
        } else {
            return "redirect:/login?error=Unauthorized access";
        }
    }

    @GetMapping("/get-semester-id")
    @ResponseBody
    public ResponseEntity<?> getSemesterId(@RequestParam("semester") Integer semester, HttpSession session) {
        if (!isCoordinator(session)) {
            return ResponseEntity.status(401).body("Unauthorized access");
        }
        return curricularUnitService.getSemesterId(semester);
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
            HttpSession session,
            Model model) {
        if (!isCoordinator(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        return curricularUnitService.createCurricularUnit(nameUC, studentsNumber, evaluationType, attendance, evaluationsCount, year, semester, session, model);
    }

    @GetMapping("/coordinator_evaluationsUC")
    public String evaluationsUCCurricular(@RequestParam("id") Long id, Model model, HttpSession session) {
        if (!isCoordinator(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        return curricularUnitService.prepareEvaluationsUCPage(id, model);
    }

}
