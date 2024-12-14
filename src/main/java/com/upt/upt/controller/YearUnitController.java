package com.upt.upt.controller;

import com.upt.upt.entity.*;
import com.upt.upt.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import jakarta.servlet.http.HttpSession;

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

    @PostMapping("/delete-year/{id}")
    public String deleteYear(@PathVariable("id") Long id, Model model, HttpSession session) {
        if (!isDirector(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        model.addAttribute("warning", "Are you sure you want to remove this year?");
        model.addAttribute("yearId", id);
        return "director_confirmRemoveYear";
    }

    @PostMapping("/remove-year/{id}")
    public String removeYear(@PathVariable("id") Long id, Model model, HttpSession session) {
        if (!isDirector(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        model.addAttribute("warning", "Are you sure you want to remove this year?");
        model.addAttribute("yearId", id);
        return "director_confirmRemoveYear";
    }

    @PostMapping("/confirm-remove-year/{id}")
    public String confirmRemoveYear(@PathVariable("id") Long id, HttpSession session) {
        if (!isDirector(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        yearUnitService.deleteYearUnit(id);
        return "redirect:/director";
    }

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