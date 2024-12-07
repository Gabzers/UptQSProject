package com.upt.upt.controller;

import com.upt.upt.entity.DirectorUnit;
import com.upt.upt.entity.SemesterUnit;
import com.upt.upt.entity.YearUnit;
import com.upt.upt.service.SemesterUnitService;
import com.upt.upt.service.YearUnitService;
import com.upt.upt.service.DirectorUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/director")
public class YearUnitController {

    @Autowired
    private YearUnitService yearUnitService;

    @Autowired
    private SemesterUnitService semesterUnitService;

    @Autowired
    private DirectorUnitService directorUnitService;

    @GetMapping("/years")
    public String getDirectorYears(HttpSession session, Model model) {
        Long directorId = (Long) session.getAttribute("userId");
        if (directorId != null) {
            Optional<DirectorUnit> directorOpt = directorUnitService.getDirectorById(directorId);
            if (directorOpt.isPresent()) {
                DirectorUnit director = directorOpt.get();
                model.addAttribute("loggedInDirector", director);
            } else {
                return "redirect:/login?error=Director not found";
            }
        } else {
            return "redirect:/login?error=Session expired";
        }
        return "director_index";
    }

    @GetMapping("/create-year")
    public String newYearForm(Model model) {
        model.addAttribute("yearUnit", new YearUnit());
        model.addAttribute("firstSemester", new SemesterUnit());
        model.addAttribute("secondSemester", new SemesterUnit());
        return "director_newYear";
    }

    @PostMapping("/save-year")
    public String saveNewYear(@ModelAttribute YearUnit yearUnit,
                              @RequestParam("firstSemester.startDate") String firstStartDate,
                              @RequestParam("firstSemester.endDate") String firstEndDate,
                              @RequestParam("firstSemester.examPeriodStart") String firstExamPeriodStart,
                              @RequestParam("firstSemester.examPeriodEnd") String firstExamPeriodEnd,
                              @RequestParam("firstSemester.resitPeriodStart") String firstResitPeriodStart,
                              @RequestParam("firstSemester.resitPeriodEnd") String firstResitPeriodEnd,
                              @RequestParam("secondSemester.startDate") String secondStartDate,
                              @RequestParam("secondSemester.endDate") String secondEndDate,
                              @RequestParam("secondSemester.examPeriodStart") String secondExamPeriodStart,
                              @RequestParam("secondSemester.examPeriodEnd") String secondExamPeriodEnd,
                              @RequestParam("secondSemester.resitPeriodStart") String secondResitPeriodStart,
                              @RequestParam("secondSemester.resitPeriodEnd") String secondResitPeriodEnd,
                              @RequestParam("specialExamStart") String specialExamStart,
                              @RequestParam("specialExamEnd") String specialExamEnd,
                              HttpSession session) {
        // Create and set the first semester attributes
        SemesterUnit firstSemester = new SemesterUnit();
        firstSemester.setStartDate(firstStartDate);
        firstSemester.setEndDate(firstEndDate);
        firstSemester.setExamPeriodStart(firstExamPeriodStart);
        firstSemester.setExamPeriodEnd(firstExamPeriodEnd);
        firstSemester.setResitPeriodStart(firstResitPeriodStart);
        firstSemester.setResitPeriodEnd(firstResitPeriodEnd);
        SemesterUnit savedFirstSemester = semesterUnitService.saveSemesterUnit(firstSemester);

        // Create and set the second semester attributes
        SemesterUnit secondSemester = new SemesterUnit();
        secondSemester.setStartDate(secondStartDate);
        secondSemester.setEndDate(secondEndDate);
        secondSemester.setExamPeriodStart(secondExamPeriodStart);
        secondSemester.setExamPeriodEnd(secondExamPeriodEnd);
        secondSemester.setResitPeriodStart(secondResitPeriodStart);
        secondSemester.setResitPeriodEnd(secondResitPeriodEnd);
        SemesterUnit savedSecondSemester = semesterUnitService.saveSemesterUnit(secondSemester);

        // Set the saved semesters to the year unit
        yearUnit.setFirstSemester(savedFirstSemester);
        yearUnit.setSecondSemester(savedSecondSemester);
        yearUnit.setSpecialExamStart(specialExamStart);
        yearUnit.setSpecialExamEnd(specialExamEnd);

        Long directorId = (Long) session.getAttribute("userId");
        Optional<DirectorUnit> directorUnit = directorUnitService.getDirectorById(directorId);
        if (directorUnit.isPresent()) {
            DirectorUnit director = directorUnit.get();
            director.addAcademicYear(yearUnit);
        } else {
            // Handle the case where the director unit is not found
            return "redirect:/director?error=Director not found";
        }

        // Save the year unit
        yearUnitService.saveYearUnit(yearUnit);
        return "redirect:/director";
    }

    @GetMapping("/edit-year")
    public String editYearForm(@RequestParam("id") Long id, Model model) {
        Optional<YearUnit> yearUnit = yearUnitService.getYearUnitById(id);
        Optional<YearUnit> mostRecentYear = yearUnitService.getMostRecentYearUnit();
        if (yearUnit.isPresent() && mostRecentYear.isPresent() && yearUnit.get().getId().equals(mostRecentYear.get().getId())) {
            model.addAttribute("yearUnit", yearUnit.get());
            model.addAttribute("firstSemester", yearUnit.get().getFirstSemester());
            model.addAttribute("secondSemester", yearUnit.get().getSecondSemester());
            return "director_editYear";
        }
        return "redirect:/director?error=Year not found or not the most recent year";
    }

    @PostMapping("/update-year/{id}")
    public String updateYear(@PathVariable("id") Long id,
                             @ModelAttribute YearUnit yearUnit,
                             @RequestParam("firstSemester.startDate") String firstStartDate,
                             @RequestParam("firstSemester.endDate") String firstEndDate,
                             @RequestParam("firstSemester.examPeriodStart") String firstExamPeriodStart,
                             @RequestParam("firstSemester.examPeriodEnd") String firstExamPeriodEnd,
                             @RequestParam("firstSemester.resitPeriodStart") String firstResitPeriodStart,
                             @RequestParam("firstSemester.resitPeriodEnd") String firstResitPeriodEnd,
                             @RequestParam("secondSemester.startDate") String secondStartDate,
                             @RequestParam("secondSemester.endDate") String secondEndDate,
                             @RequestParam("secondSemester.examPeriodStart") String secondExamPeriodStart,
                             @RequestParam("secondSemester.examPeriodEnd") String secondExamPeriodEnd,
                             @RequestParam("secondSemester.resitPeriodStart") String secondResitPeriodStart,
                             @RequestParam("secondSemester.resitPeriodEnd") String secondResitPeriodEnd,
                             @RequestParam("specialExamStart") String specialExamStart,
                             @RequestParam("specialExamEnd") String specialExamEnd,
                             HttpSession session) {
        Optional<YearUnit> existingYearUnit = yearUnitService.getYearUnitById(id);
        Optional<YearUnit> mostRecentYear = yearUnitService.getMostRecentYearUnit();
        if (existingYearUnit.isPresent() && mostRecentYear.isPresent() && existingYearUnit.get().getId().equals(mostRecentYear.get().getId())) {
            YearUnit updatedYearUnit = existingYearUnit.get();

            // Update first semester attributes
            SemesterUnit firstSemester = updatedYearUnit.getFirstSemester();
            firstSemester.setStartDate(firstStartDate);
            firstSemester.setEndDate(firstEndDate);
            firstSemester.setExamPeriodStart(firstExamPeriodStart);
            firstSemester.setExamPeriodEnd(firstExamPeriodEnd);
            firstSemester.setResitPeriodStart(firstResitPeriodStart);
            firstSemester.setResitPeriodEnd(firstResitPeriodEnd);
            SemesterUnit savedFirstSemester = semesterUnitService.saveSemesterUnit(firstSemester);

            // Update second semester attributes
            SemesterUnit secondSemester = updatedYearUnit.getSecondSemester();
            secondSemester.setStartDate(secondStartDate);
            secondSemester.setEndDate(secondEndDate);
            secondSemester.setExamPeriodStart(secondExamPeriodStart);
            secondSemester.setExamPeriodEnd(secondExamPeriodEnd);
            secondSemester.setResitPeriodStart(secondResitPeriodStart);
            secondSemester.setResitPeriodEnd(secondResitPeriodEnd);
            SemesterUnit savedSecondSemester = semesterUnitService.saveSemesterUnit(secondSemester);

            updatedYearUnit.setFirstSemester(savedFirstSemester);
            updatedYearUnit.setSecondSemester(savedSecondSemester);
            updatedYearUnit.setSpecialExamStart(specialExamStart);
            updatedYearUnit.setSpecialExamEnd(specialExamEnd);

            Long directorId = (Long) session.getAttribute("userId");
            Optional<DirectorUnit> directorUnit = directorUnitService.getDirectorById(directorId);
            if (directorUnit.isPresent()) {
                updatedYearUnit.setDirectorUnit(directorUnit.get());
            } else {
                // Handle the case where the director unit is not found
                return "redirect:/director?error=Director not found";
            }

            yearUnitService.saveYearUnit(updatedYearUnit);
        } else {
            return "redirect:/director?error=Year not found or not the most recent year";
        }
        return "redirect:/director";
    }

    @PostMapping("/delete-year/{id}")
    public String deleteYear(@PathVariable("id") Long id) {
        yearUnitService.deleteYearUnit(id);
        return "redirect:/director";
    }

    @GetMapping("/current-year")
    public String getCurrentYear(Model model) {
        Optional<YearUnit> currentYear = yearUnitService.getMostRecentYearUnit();
        if (currentYear.isPresent()) {
            model.addAttribute("currentYear", currentYear.get());
        } else {
            model.addAttribute("currentYear", null);
        }
        return "director_currentYear";
    }
}