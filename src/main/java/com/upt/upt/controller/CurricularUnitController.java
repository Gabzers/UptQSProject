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

/**
 * Controller class for managing CurricularUnit entities.
 * 
 * @autor Grupo 5 - 47719, 47713, 46697, 47752, 47004
 */
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

    /**
     * Shows the course list for the coordinator.
     * 
     * @param model the model to add attributes to
     * @param session the HTTP session
     * @return the view name
     */
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

    /**
     * Removes a CurricularUnit.
     * 
     * @param id the ID of the CurricularUnit to remove
     * @param model the model to add attributes to
     * @param session the HTTP session
     * @return the view name
     */
    @PostMapping("/remove-uc/{id}")
    public String removeCurricularUnit(@PathVariable("id") Long id, Model model, HttpSession session) {
        if (!isCoordinator(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        model.addAttribute("warning", "Are you sure you want to remove this UC?");
        model.addAttribute("ucId", id);
        return "coordinator_confirmRemoveUC";
    }

    /**
     * Confirms the removal of a CurricularUnit.
     * 
     * @param id the ID of the CurricularUnit to remove
     * @param session the HTTP session
     * @return the view name
     */
    @PostMapping("/confirm-remove-uc/{id}")
    public String confirmRemoveCurricularUnit(@PathVariable("id") Long id, HttpSession session) {
        if (!isCoordinator(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        curricularUnitService.deleteCurricularUnit(id);
        return "redirect:/coordinator";
    }

    /**
     * Shows the form for editing a CurricularUnit.
     * 
     * @param id the ID of the CurricularUnit to edit
     * @param semester the semester of the CurricularUnit
     * @param year the year of the CurricularUnit
     * @param session the HTTP session
     * @param model the model to add attributes to
     * @return the view name
     */
    @GetMapping("/coordinator_editUC")
    public String editUCForm(@RequestParam("id") Long id, @RequestParam("semester") Integer semester, @RequestParam(value = "ucYear", required = false) Integer year, HttpSession session, Model model) {
        if (!isCoordinator(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        Optional<CurricularUnit> uc = curricularUnitService.getCurricularUnitById(id);
        Optional<CoordinatorUnit> coordinator = verifyCoordinator(session);
        if (uc.isPresent() && coordinator.isPresent()) {
            model.addAttribute("uc", uc.get());
            model.addAttribute("coordinator", coordinator.get());
            model.addAttribute("year", year);
            return "coordinator_editUC";
        } else {
            return "redirect:/coordinator";
        }
    }

    /**
     * Updates a CurricularUnit.
     * 
     * @param id the ID of the CurricularUnit to update
     * @param nameUC the name of the CurricularUnit
     * @param studentsNumber the number of students in the CurricularUnit
     * @param evaluationType the evaluation type of the CurricularUnit
     * @param attendance whether attendance is required for the CurricularUnit
     * @param evaluationsCount the number of evaluations for the CurricularUnit
     * @param year the year of the CurricularUnit
     * @param semester the semester of the CurricularUnit
     * @param session the HTTP session
     * @param model the model to add attributes to
     * @return the view name
     */
    @PostMapping("/coordinator_editUC/{id}")
    public String updateCurricularUnit(
            @PathVariable("id") Long id,
            @RequestParam("ucName") String nameUC,
            @RequestParam("ucNumStudents") Integer studentsNumber,
            @RequestParam("ucEvaluationType") String evaluationType,
            @RequestParam(value = "ucAttendance", required = false, defaultValue = "false") Boolean attendance,
            @RequestParam("ucEvaluationsCount") Integer evaluationsCount,
            @RequestParam(value = "ucYear", required = false) Integer year,
            @RequestParam("ucSemester") Integer semester,
            HttpSession session,
            Model model) {
        if (!isCoordinator(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        Optional<CurricularUnit> ucOpt = curricularUnitService.getCurricularUnitById(id);
        if (ucOpt.isPresent()) {
            CurricularUnit uc = ucOpt.get();
            if (!uc.getAssessments().isEmpty()) {
                year = uc.getYear();
                semester = uc.getSemester();
                model.addAttribute("error", "Year and Semester cannot be changed because there are existing assessments.");
            } else {
                // Validate evaluationsCount
                if (evaluationsCount < uc.getAssessments().size()) {
                    model.addAttribute("error", "Evaluations count cannot be less than the number of existing assessments.");
                    model.addAttribute("uc", uc);
                    model.addAttribute("coordinator", verifyCoordinator(session).get());
                    model.addAttribute("year", year);
                    return "coordinator_editUC";
                }
                // Atualize o semestre da UC, mantendo o ano atual
                uc.setSemester(semester);
                curricularUnitService.updateCurricularUnitSemester(uc, semester, session); // Atualize o semester_id
            }
        }
        return curricularUnitService.updateCurricularUnit(id, nameUC, studentsNumber, evaluationType, attendance, evaluationsCount, year, semester, session, model);
    }

    /**
     * Shows the form for creating a new CurricularUnit.
     * 
     * @param session the HTTP session
     * @param model the model to add attributes to
     * @return the view name
     */
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

    /**
     * Gets the semester ID for a given semester.
     * 
     * @param semester the semester
     * @param session the HTTP session
     * @return the ResponseEntity containing the semester ID
     */
    @GetMapping("/get-semester-id")
    @ResponseBody
    public ResponseEntity<?> getSemesterId(@RequestParam("semester") Integer semester, HttpSession session) {
        if (!isCoordinator(session)) {
            return ResponseEntity.status(401).body("Unauthorized access");
        }
        return curricularUnitService.getSemesterId(semester);
    }

    /**
     * Creates a new CurricularUnit.
     * 
     * @param nameUC the name of the CurricularUnit
     * @param studentsNumber the number of students in the CurricularUnit
     * @param evaluationType the evaluation type of the CurricularUnit
     * @param attendance whether attendance is required for the CurricularUnit
     * @param evaluationsCount the number of evaluations for the CurricularUnit
     * @param year the year of the CurricularUnit
     * @param semester the semester of the CurricularUnit
     * @param session the HTTP session
     * @param model the model to add attributes to
     * @return the view name
     */
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

    /**
     * Shows the evaluations for a CurricularUnit.
     * 
     * @param id the ID of the CurricularUnit
     * @param model the model to add attributes to
     * @param session the HTTP session
     * @return the view name
     */
    @GetMapping("/coordinator_evaluationsUC")
    public String evaluationsUCCurricular(@RequestParam("id") Long id, Model model, HttpSession session) {
        if (!isCoordinator(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        return curricularUnitService.prepareEvaluationsUCPage(id, model);
    }

}
