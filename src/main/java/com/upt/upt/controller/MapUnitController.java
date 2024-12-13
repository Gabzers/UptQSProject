package com.upt.upt.controller;

import com.upt.upt.entity.*;
import com.upt.upt.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import com.upt.upt.service.CoordinatorUnitService;

@Controller
public class MapUnitController {

    @Autowired
    private CoordinatorUnitService coordinatorUnitService;

    @Autowired
    private AssessmentUnitService assessmentService;

    private Optional<CoordinatorUnit> verifyCoordinator(HttpSession session) {
        Long coordinatorId = (Long) session.getAttribute("userId");
        if (coordinatorId == null) {
            return Optional.empty();
        }
        return coordinatorUnitService.getCoordinatorById(coordinatorId);
    }

    @GetMapping("/coordinator/map")
    public String showAssessmentMap(Model model, HttpSession session) {
        Optional<CoordinatorUnit> coordinatorOpt = verifyCoordinator(session);
        if (coordinatorOpt.isPresent()) {
            CoordinatorUnit coordinator = coordinatorOpt.get();
            DirectorUnit director = coordinator.getDirectorUnit();
            YearUnit currentYear = director.getCurrentYear();
            if (currentYear != null) {
                List<CurricularUnit> coordinatorUnits = coordinator.getCurricularUnits();
                List<AssessmentUnit> firstSemesterAssessments = assessmentService.getAssessmentsBySemester(currentYear.getFirstSemester().getId()).stream()
                        .filter(assessment -> coordinatorUnits.contains(assessment.getCurricularUnit()))
                        .collect(Collectors.toList());
                List<AssessmentUnit> secondSemesterAssessments = assessmentService.getAssessmentsBySemester(currentYear.getSecondSemester().getId()).stream()
                        .filter(assessment -> coordinatorUnits.contains(assessment.getCurricularUnit()))
                        .collect(Collectors.toList());
                model.addAttribute("firstSemesterAssessments", firstSemesterAssessments);
                model.addAttribute("secondSemesterAssessments", secondSemesterAssessments);
                model.addAttribute("noNormalPeriodFirstSemester", assessmentService.noAssessmentsForPeriod(firstSemesterAssessments, "Teaching Period") && assessmentService.noAssessmentsForPeriod(firstSemesterAssessments, "Exam Period"));
                model.addAttribute("noResourcePeriodFirstSemester", assessmentService.noAssessmentsForPeriod(firstSemesterAssessments, "Resource Period"));
                model.addAttribute("noSpecialPeriodFirstSemester", assessmentService.noAssessmentsForPeriod(firstSemesterAssessments, "Special Period"));
                model.addAttribute("noNormalPeriodSecondSemester", assessmentService.noAssessmentsForPeriod(secondSemesterAssessments, "Teaching Period") && assessmentService.noAssessmentsForPeriod(secondSemesterAssessments, "Exam Period"));
                model.addAttribute("noResourcePeriodSecondSemester", assessmentService.noAssessmentsForPeriod(secondSemesterAssessments, "Resource Period"));
                model.addAttribute("noSpecialPeriodSecondSemester", assessmentService.noAssessmentsForPeriod(secondSemesterAssessments, "Special Period"));
            } else {
                return "redirect:/login?error=Current year not found";
            }
        } else {
            return "redirect:/login?error=Coordinator not found";
        }
        return "coordinator_map";
    }
}
