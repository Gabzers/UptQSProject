package com.upt.upt.controller;

import com.upt.upt.entity.*;
import com.upt.upt.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class MapUnitController {

    @Autowired
    private CoordinatorUnitService coordinatorUnitService;

    @Autowired
    private AssessmentUnitService assessmentService;

    @Autowired
    private PdfService pdfService;

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

    @GetMapping("/coordinator/map")
    public String showAssessmentMap(@RequestParam("semester") Integer semester, Model model, HttpSession session) {
        if (!isCoordinator(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        Optional<CoordinatorUnit> coordinatorOpt = verifyCoordinator(session);
        if (coordinatorOpt.isPresent()) {
            CoordinatorUnit coordinator = coordinatorOpt.get();
            DirectorUnit director = coordinator.getDirectorUnit();
            YearUnit currentYear = director.getCurrentYear();
            if (currentYear != null) {
                List<CurricularUnit> coordinatorUnits = coordinator.getCurricularUnits();
                List<AssessmentUnit> firstSemesterAssessments = currentYear.getFirstSemester().getCurricularUnits().stream()
                        .filter(uc -> coordinatorUnits.contains(uc))
                        .flatMap(uc -> uc.getAssessments().stream())
                        .sorted((a1, a2) -> a1.getStartTime().compareTo(a2.getStartTime()))
                        .collect(Collectors.toList());
                List<AssessmentUnit> secondSemesterAssessments = currentYear.getSecondSemester().getCurricularUnits().stream()
                        .filter(uc -> coordinatorUnits.contains(uc))
                        .flatMap(uc -> uc.getAssessments().stream())
                        .sorted((a1, a2) -> a1.getStartTime().compareTo(a2.getStartTime()))
                        .collect(Collectors.toList());
                model.addAttribute("firstSemesterAssessments", firstSemesterAssessments);
                model.addAttribute("secondSemesterAssessments", secondSemesterAssessments);
                model.addAttribute("noNormalPeriodFirstSemester", assessmentService.noAssessmentsForPeriod(firstSemesterAssessments, "Teaching Period") && assessmentService.noAssessmentsForPeriod(firstSemesterAssessments, "Exam Period"));
                model.addAttribute("noResourcePeriodFirstSemester", assessmentService.noAssessmentsForPeriod(firstSemesterAssessments, "Resource Period"));
                model.addAttribute("noSpecialPeriodFirstSemester", assessmentService.noAssessmentsForPeriod(firstSemesterAssessments, "Special Period"));
                model.addAttribute("noNormalPeriodSecondSemester", assessmentService.noAssessmentsForPeriod(secondSemesterAssessments, "Teaching Period") && assessmentService.noAssessmentsForPeriod(secondSemesterAssessments, "Exam Period"));
                model.addAttribute("noResourcePeriodSecondSemester", assessmentService.noAssessmentsForPeriod(secondSemesterAssessments, "Resource Period"));
                model.addAttribute("noSpecialPeriodSecondSemester", assessmentService.noAssessmentsForPeriod(secondSemesterAssessments, "Special Period"));
                List<Integer> years = coordinatorUnits.stream().map(CurricularUnit::getYear).distinct().collect(Collectors.toList());
                model.addAttribute("years", years);
                model.addAttribute("semester", semester);
            } else {
                return "redirect:/login?error=Current year not found";
            }
        } else {
            return "redirect:/login?error=Coordinator not found";
        }
        return "coordinator_map";
    }

    @GetMapping("/coordinator/map/pdf")
    public ResponseEntity<byte[]> generatePdf(@RequestParam("year") Integer year, @RequestParam("semester") Integer semester, HttpSession session) {
        if (!isCoordinator(session)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        Optional<CoordinatorUnit> coordinatorOpt = verifyCoordinator(session);
        if (coordinatorOpt.isPresent()) {
            CoordinatorUnit coordinator = coordinatorOpt.get();
            YearUnit currentYear = coordinator.getDirectorUnit().getCurrentYear();
            List<AssessmentUnit> assessments = (semester == 1 ? currentYear.getFirstSemester().getCurricularUnits() : currentYear.getSecondSemester().getCurricularUnits()).stream()
                    .filter(uc -> uc.getYear().equals(year) && coordinator.getCurricularUnits().contains(uc))
                    .flatMap(uc -> uc.getAssessments().stream())
                    .sorted((a1, a2) -> a1.getStartTime().compareTo(a2.getStartTime()))
                    .collect(Collectors.toList());
            if (assessments.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            }
            String ucName = assessments.get(0).getCurricularUnit().getNameUC();
            byte[] pdfContent = pdfService.generatePdfForYearAndSemester(coordinator, year, semester);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            String semesterText = semester == 1 ? "1st" : "2nd";
            String startDate = currentYear.getFirstSemester().getStartDate();
            headers.setContentDispositionFormData("attachment", "UPT_" + coordinator.getCourse() + "_" + startDate + "_" + year + "_" + semesterText + ".pdf");
            return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
}
