package com.upt.upt.controller;

import com.upt.upt.entity.DirectorUnit;
import com.upt.upt.entity.SemesterUnit;
import com.upt.upt.entity.UserType;
import com.upt.upt.entity.YearUnit;
import com.upt.upt.service.DirectorUnitService;
import com.upt.upt.service.YearUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import jakarta.servlet.http.HttpSession;

/**
 * Controller for handling requests related to YearUnit entities.
 * 
 * @autor Grupo 5 - 47719, 47713, 46697, 47752, 47004
 */
@Controller
@RequestMapping("/director")
public class YearUnitController {

    @Autowired
    private YearUnitService yearUnitService;

    @Autowired
    private DirectorUnitService directorUnitService;

    private Optional<DirectorUnit> verifyDirector(HttpSession session) {
        Long directorId = (Long) session.getAttribute("userId");
        if (directorId == null) {
            return Optional.empty();
        }
        return directorUnitService.getDirectorById(directorId);
    }

    private boolean isDirector(HttpSession session) {
        UserType userType = (UserType) session.getAttribute("userType");
        return userType == UserType.DIRECTOR;
    }

    /**
     * Handles the request to get the director's years.
     * 
     * @param session the HTTP session
     * @param model the model to add attributes to
     * @return the view name
     */
    @GetMapping("/years")
    public String getDirectorYears(HttpSession session, Model model) {
        if (!isDirector(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        Optional<DirectorUnit> directorOpt = verifyDirector(session);
        if (directorOpt.isPresent()) {
            DirectorUnit director = directorOpt.get();
            model.addAttribute("loggedInDirector", director);
            List<YearUnit> directorYears = director.getAcademicYears();
            if (!directorYears.isEmpty()) {
                YearUnit currentYear = director.getCurrentYear();
                model.addAttribute("currentYear", currentYear);
                model.addAttribute("pastYears", director.getPastYears());
            } else {
                model.addAttribute("currentYear", null);
                model.addAttribute("pastYears", List.of());
            }
        } else {
            return "redirect:/login?error=Session expired";
        }
        return "director_index";
    }

    /**
     * Shows the form to create a new year.
     * 
     * @param session the HTTP session
     * @param model the model to add attributes to
     * @return the view name
     */
    @GetMapping("/create-year")
    public String newYearForm(HttpSession session, Model model) {
        if (!isDirector(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        model.addAttribute("yearUnit", new YearUnit());
        model.addAttribute("firstSemester", new SemesterUnit());
        model.addAttribute("secondSemester", new SemesterUnit());
        return "director_newYear";
    }

    /**
     * Saves a new year.
     * 
     * @param yearUnit the year unit to save
     * @param params the request parameters
     * @param session the HTTP session
     * @param model the model to add attributes to
     * @return the view name
     */
    @PostMapping("/save-year")
    public String saveNewYear(@ModelAttribute YearUnit yearUnit, @RequestParam Map<String, String> params, HttpSession session, Model model) {
        if (!isDirector(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        Optional<DirectorUnit> directorOpt = verifyDirector(session);
        if (directorOpt.isEmpty()) {
            return "redirect:/login?error=Unauthorized access";
        }
        Long directorId = directorOpt.get().getId();
        if (!yearUnitService.validateNewYearDates(params, model, directorId)) {
            return "director_newYear";
        }
        yearUnitService.saveNewYear(yearUnit, params, session);
        return "redirect:/director";
    }

    /**
     * Shows the form to edit an existing year.
     * 
     * @param id the ID of the year to edit
     * @param session the HTTP session
     * @param model the model to add attributes to
     * @return the view name
     */
    @GetMapping("/edit-year")
    public String editYearForm(@RequestParam("id") Long id, HttpSession session, Model model) {
        if (!isDirector(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        Optional<YearUnit> yearUnit = yearUnitService.getYearUnitById(id);
        if (yearUnit.isPresent()) {
            model.addAttribute("yearUnit", yearUnit.get());
            model.addAttribute("firstSemester", yearUnit.get().getFirstSemester());
            model.addAttribute("secondSemester", yearUnit.get().getSecondSemester());
            return "director_editYear";
        }
        return "redirect:/director?error=Year not found";
    }

    /**
     * Updates an existing year.
     * 
     * @param id the ID of the year to update
     * @param yearUnit the updated year unit
     * @param params the request parameters
     * @param session the HTTP session
     * @param model the model to add attributes to
     * @return the view name
     */
    @PostMapping("/update-year/{id}")
    public String updateYear(@PathVariable("id") Long id, @ModelAttribute YearUnit yearUnit, @RequestParam Map<String, String> params, HttpSession session, Model model) {
        if (!isDirector(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        Optional<DirectorUnit> directorOpt = verifyDirector(session);
        if (directorOpt.isEmpty()) {
            return "redirect:/login?error=Unauthorized access";
        }
        Long directorId = directorOpt.get().getId();
        if (!yearUnitService.validateYearDates(params, model, directorId)) {
            return "director_editYear";
        }
        yearUnitService.updateYear(id, yearUnit, params, session);
        return "redirect:/director";
    }

    /**
     * Shows the confirmation page to delete a year.
     * 
     * @param id the ID of the year to delete
     * @param model the model to add attributes to
     * @param session the HTTP session
     * @return the view name
     */
    @PostMapping("/delete-year/{id}")
    public String deleteYear(@PathVariable("id") Long id, Model model, HttpSession session) {
        if (!isDirector(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        model.addAttribute("warning", "Are you sure you want to remove this year?");
        model.addAttribute("yearId", id);
        return "director_confirmRemoveYear";
    }

    /**
     * Confirms the removal of a year.
     * 
     * @param id the ID of the year to remove
     * @param session the HTTP session
     * @return the view name
     */
    @PostMapping("/confirm-remove-year/{id}")
    public String confirmRemoveYear(@PathVariable("id") Long id, HttpSession session) {
        if (!isDirector(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        yearUnitService.deleteYearUnit(id);
        return "redirect:/director";
    }

    /**
     * Gets the current year.
     * 
     * @param session the HTTP session
     * @param model the model to add attributes to
     * @return the view name
     */
    @GetMapping("/current-year")
    public String getCurrentYear(HttpSession session, Model model) {
        if (!isDirector(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        if (verifyDirector(session).isEmpty()) {
            return "redirect:/login?error=Unauthorized access";
        }
        Optional<YearUnit> currentYear = yearUnitService.getMostRecentYearUnit();
        model.addAttribute("currentYear", currentYear.orElse(null));
        return "director_currentYear";
    }
}