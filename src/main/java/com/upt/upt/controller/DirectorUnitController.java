package com.upt.upt.controller;

import com.upt.upt.entity.AssessmentUnit;
import com.upt.upt.entity.CoordinatorUnit;
import com.upt.upt.entity.DirectorUnit;
import com.upt.upt.entity.SemesterUnit;
import com.upt.upt.entity.UserType;
import com.upt.upt.entity.YearUnit;
import com.upt.upt.service.CoordinatorUnitService;
import com.upt.upt.service.DirectorUnitService;
import com.upt.upt.service.PdfService;
import com.upt.upt.service.UserService;
import com.upt.upt.service.YearUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Comparator;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.util.stream.Collectors;

/**
 * Controller class for managing DirectorUnit entities.
 * 
 * @autor Grupo 5 - 47719, 47713, 46697, 47752, 47004
 */
@Controller
public class DirectorUnitController {

    @Autowired
    private DirectorUnitService directorUnitService;

    @Autowired
    private CoordinatorUnitService coordinatorService;

    @Autowired
    private YearUnitService yearUnitService;

    @Autowired
    private UserService userService;

    @Autowired
    private PdfService pdfService;

    private Optional<DirectorUnit> verifyDirector(HttpSession session) {
        Long directorId = (Long) session.getAttribute("userId");
        if (directorId == null) {
            return Optional.empty();
        }
        return directorUnitService.getDirectorById(directorId);
    }

    private boolean verifyMaster(HttpSession session) {
        UserType userType = (UserType) session.getAttribute("userType");
        return userType == UserType.MASTER;
    }

    private boolean isDirector(HttpSession session) {
        UserType userType = (UserType) session.getAttribute("userType");
        return userType == UserType.DIRECTOR;
    }

    /**
     * Lists all directors.
     * 
     * @param session the HTTP session
     * @param model the model to add attributes to
     * @return the view name
     */
    @GetMapping("/director")
    public String listDirectors(HttpSession session, Model model) {
        if (!isDirector(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        Optional<DirectorUnit> directorOpt = verifyDirector(session);
        if (directorOpt.isPresent()) {
            DirectorUnit director = directorOpt.get();
            model.addAttribute("loggedInDirector", director);
            model.addAttribute("coordinators", director.getCoordinators());
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
     * Views the semesters for a specific year.
     * 
     * @param id the ID of the year
     * @param model the model to add attributes to
     * @param session the HTTP session
     * @return the view name
     */
    @GetMapping("/director/viewSemester/{id}")
    public String viewSemesters(@PathVariable("id") Long id, Model model, HttpSession session) {
        if (!isDirector(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        Optional<YearUnit> yearUnit = yearUnitService.getYearUnitById(id);
        if (yearUnit.isPresent()) {
            model.addAttribute("yearUnit", yearUnit.get());
            model.addAttribute("firstSemester", yearUnit.get().getFirstSemester());
            model.addAttribute("secondSemester", yearUnit.get().getSecondSemester());
            return "director_viewSemester";
        }
        return "redirect:/director?error=Year not found";
    }

    /**
     * Views the assessment map for a specific semester and year.
     * 
     * @param semester the semester
     * @param yearId the ID of the year
     * @param model the model to add attributes to
     * @param session the HTTP session
     * @return the view name
     */
    @GetMapping("/director/map/{semester}/{yearId}")
    public String viewSemesterMap(@PathVariable("semester") String semester, @PathVariable("yearId") Long yearId, Model model, HttpSession session) {
        if (!isDirector(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        Optional<YearUnit> yearUnit = yearUnitService.getYearUnitById(yearId);
        if (yearUnit.isPresent()) {
            SemesterUnit semesterUnit = "1st".equals(semester) ? yearUnit.get().getFirstSemester() : yearUnit.get().getSecondSemester();
            List<AssessmentUnit> assessments = semesterUnit.getMapUnit().getAssessments();
            assessments.sort(Comparator.comparing(AssessmentUnit::getStartTime));
            Map<Integer, List<AssessmentUnit>> assessmentsByYear = assessments.stream()
                    .collect(Collectors.groupingBy(assessment -> assessment.getCurricularUnit().getYear()));
            model.addAttribute("mapUnit", semesterUnit.getMapUnit());
            model.addAttribute("assessmentsByYear", assessmentsByYear);
            model.addAttribute("noNormalPeriod", directorUnitService.noAssessmentsForPeriod(assessments, "Teaching Period") && directorUnitService.noAssessmentsForPeriod(assessments, "Exam Period"));
            model.addAttribute("noResourcePeriod", directorUnitService.noAssessmentsForPeriod(assessments, "Resource Period"));
            model.addAttribute("noSpecialPeriod", directorUnitService.noAssessmentsForPeriod(assessments, "Special Period"));
            model.addAttribute("years", assessmentsByYear.keySet());
            model.addAttribute("semester", semester);
            model.addAttribute("yearId", yearId);
            return "director_viewSemesterMap";
        }
        return "redirect:/director?error=Year not found";
    }

    /**
     * Views the curricular units for a specific semester and year.
     * 
     * @param semester the semester
     * @param yearId the ID of the year
     * @param model the model to add attributes to
     * @param session the HTTP session
     * @return the view name
     */
    @GetMapping("/director/ucs/{semester}/{yearId}")
    public String viewSemesterUcs(@PathVariable("semester") String semester, @PathVariable("yearId") Long yearId, Model model, HttpSession session) {
        if (!isDirector(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        Optional<YearUnit> yearUnit = yearUnitService.getYearUnitById(yearId);
        if (yearUnit.isPresent()) {
            SemesterUnit semesterUnit = "1st".equals(semester) ? yearUnit.get().getFirstSemester() : yearUnit.get().getSecondSemester();
            model.addAttribute("semesterUnit", semesterUnit);
            return "director_viewSemesterUcs";
        }
        return "redirect:/director?error=Year not found";
    }

    /**
     * Shows the form to edit a coordinator.
     * 
     * @param id the ID of the coordinator
     * @param model the model to add attributes to
     * @param session the HTTP session
     * @return the view name
     */
    @GetMapping("/director/edit-coordinator")
    public String showEditCoordinatorForm(@RequestParam("id") Long id, Model model, HttpSession session) {
        if (!isDirector(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        Optional<DirectorUnit> directorOpt = verifyDirector(session);
        if (directorOpt.isPresent()) {
            Optional<CoordinatorUnit> coordinatorOpt = coordinatorService.getCoordinatorById(id);
            if (coordinatorOpt.isPresent()) {
                model.addAttribute("coordinator", coordinatorOpt.get());
                return "director_editCoordinator";
            }
            return "redirect:/director?error=Coordinator not found";
        }
        return "redirect:/login?error=Session expired";
    }

    /**
     * Updates a coordinator.
     * 
     * @param id the ID of the coordinator
     * @param params the request parameters
     * @param session the HTTP session
     * @return the view name
     */
    @PostMapping("/director/update-coordinator/{id}")
    public String updateCoordinator(@PathVariable("id") Long id, @RequestParam Map<String, String> params, HttpSession session) {
        if (!isDirector(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        Optional<DirectorUnit> directorOpt = verifyDirector(session);
        if (directorOpt.isPresent()) {
            try {
                CoordinatorUnit existingCoordinator = coordinatorService.getCoordinatorById(id).orElseThrow(() -> new IllegalArgumentException("Coordinator not found"));
                existingCoordinator.setName(params.get("name"));
                existingCoordinator.setCourse(params.get("course"));
                existingCoordinator.setDuration(Integer.parseInt(params.get("duration")));
                existingCoordinator.setUsername(params.get("username"));
                if (params.get("password") != null && !params.get("password").isEmpty()) {
                    existingCoordinator.setPassword(params.get("password"));
                }
                coordinatorService.saveCoordinator(existingCoordinator);
                return "redirect:/director";
            } catch (Exception e) {
                e.printStackTrace();
                return "redirect:/director/edit-coordinator?id=" + id + "&error=true";
            }
        }
        return "redirect:/login?error=Session expired";
    }

    /**
     * Shows the form to create a new director.
     * 
     * @param session the HTTP session
     * @return the view name
     */
    @GetMapping("/master/create-director")
    public String showCreateDirectorForm(HttpSession session) {
        if (!verifyMaster(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        return "master_addDirector";
    }

    /**
     * Creates a new director.
     * 
     * @param params the request parameters
     * @param session the HTTP session
     * @return the view name
     */
    @PostMapping("/master/create-director")
    public String createDirector(@RequestParam Map<String, String> params, HttpSession session) {
        if (!verifyMaster(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        try {
            if (userService.usernameExists(params.get("director-username"))) {
                throw new IllegalArgumentException("Username already exists");
            }
            DirectorUnit newDirector = directorUnitService.createDirector(params);
            directorUnitService.saveDirector(newDirector);
            return "redirect:/master";
        } catch (IllegalArgumentException e) {
            return "redirect:/master/create-director?error=" + e.getMessage();
        } catch (RuntimeException e) {
            return "redirect:/master/create-director?error=Duplicate entry or integrity constraint violated";
        }
    }

    /**
     * Shows the form to edit an existing director.
     * 
     * @param id the ID of the director
     * @param model the model to add attributes to
     * @param session the HTTP session
     * @return the view name
     */
    @GetMapping("/master/edit-director")
    public String showEditDirectorForm(@RequestParam("id") Long id, Model model, HttpSession session) {
        if (!verifyMaster(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        Optional<DirectorUnit> directorOptional = directorUnitService.getDirectorById(id);
        if (directorOptional.isPresent()) {
            model.addAttribute("director", directorOptional.get());
            return "master_editDirector";
        }
        return "redirect:/master?error=Director not found";
    }

    /**
     * Generates a PDF for the specified year and semester.
     * 
     * @param yearId the ID of the year
     * @param semester the semester number
     * @param session the HTTP session
     * @return the PDF response entity
     */
    @GetMapping("/director/map/pdf")
    public ResponseEntity<byte[]> generatePdf(@RequestParam("yearId") Long yearId, @RequestParam("semester") Integer semester, HttpSession session) {
        if (!isDirector(session)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        Optional<DirectorUnit> directorOpt = verifyDirector(session);
        if (directorOpt.isPresent()) {
            DirectorUnit director = directorOpt.get();
            Optional<YearUnit> yearUnitOpt = yearUnitService.getYearUnitById(yearId);
            if (yearUnitOpt.isPresent()) {
                YearUnit yearUnit = yearUnitOpt.get();
                List<AssessmentUnit> assessments = (semester == 1 ? yearUnit.getFirstSemester().getCurricularUnits() : yearUnit.getSecondSemester().getCurricularUnits()).stream()
                        .flatMap(uc -> uc.getAssessments().stream())
                        .sorted((a1, a2) -> a1.getStartTime().compareTo(a2.getStartTime()))
                        .collect(Collectors.toList());
                if (assessments.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
                }
                byte[] pdfContent = pdfService.generatePdfForYearAndSemester(director, yearUnit, semester);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_PDF);
                String semesterText = semester == 1 ? "1st" : "2nd";
                String startDate = yearUnit.getFirstSemester().getStartDate();
                headers.setContentDispositionFormData("attachment", "UPT_" + director.getDepartment() + "_" + startDate + "_" + yearId + "_" + semesterText + ".pdf");
                return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    /**
     * Generates a PDF for the curricular units of a specific semester.
     * 
     * @param semesterId the ID of the semester
     * @param semester the semester
     * @param session the HTTP session
     * @return the PDF response entity
     */
    @GetMapping("/director/ucs/pdf")
    public ResponseEntity<byte[]> generateUcsPdf(@RequestParam("semesterId") Long semesterId, @RequestParam("semester") String semester, HttpSession session) {
        if (!isDirector(session)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        Optional<DirectorUnit> directorOpt = verifyDirector(session);
        if (directorOpt.isPresent()) {
            DirectorUnit director = directorOpt.get();
            Optional<SemesterUnit> semesterUnitOpt = yearUnitService.getSemesterUnitById(semesterId);
            if (semesterUnitOpt.isPresent()) {
                SemesterUnit semesterUnit = semesterUnitOpt.get();
                if (semesterUnit.getCurricularUnits().isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
                }
                byte[] pdfContent = pdfService.generatePdfForUcs(semesterUnit, director.getDepartment(), semester);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_PDF);
                headers.setContentDispositionFormData("attachment", "UPT_" + director.getDepartment() + "_UCs_" + semester + "_Semester_" + semesterUnit.getStartDate() + ".pdf");
                return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
}
