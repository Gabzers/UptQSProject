package com.upt.upt.service;

import com.upt.upt.entity.DirectorUnit;
import com.upt.upt.entity.MapUnit;
import com.upt.upt.entity.SemesterUnit;
import com.upt.upt.entity.YearUnit;
import com.upt.upt.repository.SemesterUnitRepository;
import com.upt.upt.repository.YearUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import jakarta.servlet.http.HttpSession;
import java.util.Map;

/**
 * Service class for managing YearUnit entities.
 * 
 * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
 */
@Service
public class YearUnitService {

    @Autowired
    private YearUnitRepository yearUnitRepository;

    @Autowired
    private SemesterUnitRepository semesterUnitRepository;

    @Autowired
    private DirectorUnitService directorUnitService;

    @Autowired
    private MapUnitService mapUnitService;

    /**
     * Retrieve all YearUnits.
     * 
     * @return a list of all YearUnits
     */
    public List<YearUnit> getAllYearUnits() {
        return yearUnitRepository.findAll();
    }

    /**
     * Retrieve a YearUnit by its ID.
     * 
     * @param id the ID of the YearUnit
     * @return an Optional containing the YearUnit if found, or empty if not found
     */
    public Optional<YearUnit> getYearUnitById(Long id) {
        return yearUnitRepository.findById(id);
    }

    /**
     * Save a new YearUnit.
     * 
     * @param yearUnit the YearUnit to be saved
     * @return the saved YearUnit
     */
    public YearUnit saveYearUnit(YearUnit yearUnit) {
        return yearUnitRepository.save(yearUnit);
    }

    /**
     * Delete a YearUnit by its ID.
     * 
     * @param id the ID of the YearUnit to be deleted
     */
    public void deleteYearUnit(Long id) {
        yearUnitRepository.deleteById(id);
    }

    /**
     * Save a new SemesterUnit.
     * 
     * @param semesterUnit the SemesterUnit to be saved
     * @return the saved SemesterUnit
     */
    public SemesterUnit saveSemesterUnit(SemesterUnit semesterUnit) {
        return semesterUnitRepository.save(semesterUnit);
    }

    /**
     * Retrieve the most recent YearUnit.
     * 
     * @return an Optional containing the most recent YearUnit if found, or empty if not found
     */
    public Optional<YearUnit> getMostRecentYearUnit() {
        return yearUnitRepository.findTopByOrderByIdDesc();
    }

    /**
     * Retrieve the most recent YearUnit by director ID.
     * 
     * @param directorId the ID of the director
     * @return an Optional containing the most recent YearUnit if found, or empty if not found
     */
    public Optional<YearUnit> getMostRecentYearUnitByDirector(Long directorId) {
        return yearUnitRepository.findTopByDirectorUnitIdOrderByIdDesc(directorId);
    }

    /**
     * Retrieve the current YearUnit by director ID.
     * 
     * @param directorId the ID of the director
     * @return an Optional containing the current YearUnit if found, or empty if not found
     */
    public Optional<YearUnit> getCurrentYearUnitByDirector(Long directorId) {
        return yearUnitRepository.findByDirectorUnitId(directorId).stream()
                .filter(YearUnit::isCurrentYear)
                .max((y1, y2) -> y1.getFirstSemester().getStartDate().compareTo(y2.getFirstSemester().getStartDate()));
    }

    /**
     * Retrieve a SemesterUnit by its ID.
     * 
     * @param id the ID of the SemesterUnit
     * @return an Optional containing the SemesterUnit if found, or empty if not found
     */
    public Optional<SemesterUnit> getSemesterUnitById(Long id) {
        return semesterUnitRepository.findById(id);
    }

    /**
     * Update an existing SemesterUnit.
     * 
     * @param semester the SemesterUnit to be updated
     * @param startDate the new start date
     * @param endDate the new end date
     * @param examPeriodStart the new exam period start date
     * @param examPeriodEnd the new exam period end date
     * @param resitPeriodStart the new resit period start date
     * @param resitPeriodEnd the new resit period end date
     * @return the updated SemesterUnit
     */
    public SemesterUnit updateSemester(SemesterUnit semester, String startDate, String endDate, String examPeriodStart, String examPeriodEnd, String resitPeriodStart, String resitPeriodEnd) {
        semester.setStartDate(startDate);
        semester.setEndDate(endDate);
        semester.setExamPeriodStart(examPeriodStart);
        semester.setExamPeriodEnd(examPeriodEnd);
        semester.setResitPeriodStart(resitPeriodStart);
        semester.setResitPeriodEnd(resitPeriodEnd);
        SemesterUnit savedSemester = saveSemesterUnit(semester);

        if (savedSemester.getMapUnit() == null) {
            MapUnit semesterMap = new MapUnit();
            semesterMap.setSemesterUnit(savedSemester);
            mapUnitService.saveMapUnit(semesterMap);
            savedSemester.setMapUnit(semesterMap);
            saveSemesterUnit(savedSemester);
        }

        return savedSemester;
    }

    /**
     * Save a new YearUnit with its associated semesters.
     * 
     * @param yearUnit the YearUnit to be saved
     * @param params the parameters for the semesters
     * @param session the current HTTP session
     */
    public void saveNewYear(YearUnit yearUnit, Map<String, String> params, HttpSession session) {
        SemesterUnit firstSemester = updateSemester(new SemesterUnit(), params.get("firstSemester.startDate"), params.get("firstSemester.endDate"), params.get("firstSemester.examPeriodStart"), params.get("firstSemester.examPeriodEnd"), params.get("firstSemester.resitPeriodStart"), params.get("firstSemester.resitPeriodEnd"));
        SemesterUnit secondSemester = updateSemester(new SemesterUnit(), params.get("secondSemester.startDate"), params.get("secondSemester.endDate"), params.get("secondSemester.examPeriodStart"), params.get("secondSemester.examPeriodEnd"), params.get("secondSemester.resitPeriodStart"), params.get("secondSemester.resitPeriodEnd"));

        yearUnit.setFirstSemester(firstSemester);
        yearUnit.setSecondSemester(secondSemester);
        yearUnit.setSpecialExamStart(params.get("specialExamStart"));
        yearUnit.setSpecialExamEnd(params.get("specialExamEnd"));

        Long directorId = (Long) session.getAttribute("userId");
        Optional<DirectorUnit> directorUnit = directorUnitService.getDirectorById(directorId);
        if (directorUnit.isPresent()) {
            DirectorUnit director = directorUnit.get();
            yearUnit.setDirectorUnit(director);
            director.addAcademicYear(yearUnit);
        } else {
            throw new IllegalArgumentException("Director not found");
        }

        saveYearUnit(yearUnit);
    }

    /**
     * Update an existing YearUnit.
     * 
     * @param id the ID of the YearUnit to be updated
     * @param yearUnit the YearUnit with updated information
     * @param params the parameters for the semesters
     * @param session the current HTTP session
     */
    public void updateYear(Long id, YearUnit yearUnit, Map<String, String> params, HttpSession session) {
        Optional<YearUnit> existingYearUnit = getYearUnitById(id);
        if (existingYearUnit.isPresent()) {
            YearUnit updatedYearUnit = existingYearUnit.get();

            SemesterUnit firstSemester = updateSemester(updatedYearUnit.getFirstSemester(), params.get("firstSemester.startDate"), params.get("firstSemester.endDate"), params.get("firstSemester.examPeriodStart"), params.get("firstSemester.examPeriodEnd"), params.get("firstSemester.resitPeriodStart"), params.get("firstSemester.resitPeriodEnd"));
            SemesterUnit secondSemester = updateSemester(updatedYearUnit.getSecondSemester(), params.get("secondSemester.startDate"), params.get("secondSemester.endDate"), params.get("secondSemester.examPeriodStart"), params.get("secondSemester.examPeriodEnd"), params.get("secondSemester.resitPeriodStart"), params.get("secondSemester.resitPeriodEnd"));

            updatedYearUnit.setFirstSemester(firstSemester);
            updatedYearUnit.setSecondSemester(secondSemester);
            updatedYearUnit.setSpecialExamStart(params.get("specialExamStart"));
            updatedYearUnit.setSpecialExamEnd(params.get("specialExamEnd"));

            Long directorId = (Long) session.getAttribute("userId");
            Optional<DirectorUnit> directorUnit = directorUnitService.getDirectorById(directorId);
            if (directorUnit.isPresent()) {
                updatedYearUnit.setDirectorUnit(directorUnit.get());
            } else {
                throw new IllegalArgumentException("Director not found");
            }

            saveYearUnit(updatedYearUnit);
        } else {
            throw new IllegalArgumentException("Year not found");
        }
    }

    /**
     * Validate the dates for a new YearUnit.
     * 
     * @param params the parameters for the semesters
     * @param model the model to add error messages to
     * @param directorId the ID of the director
     * @return true if the dates are valid, false otherwise
     */
    public boolean validateYearDates(Map<String, String> params, Model model, Long directorId) {
        LocalDate firstSemesterStartDate = LocalDate.parse(params.get("firstSemester.startDate"));
        LocalDate firstSemesterEndDate = LocalDate.parse(params.get("firstSemester.endDate"));
        LocalDate firstSemesterExamStartDate = LocalDate.parse(params.get("firstSemester.examPeriodStart"));
        LocalDate firstSemesterExamEndDate = LocalDate.parse(params.get("firstSemester.examPeriodEnd"));
        LocalDate firstSemesterResitStartDate = LocalDate.parse(params.get("firstSemester.resitPeriodStart"));
        LocalDate firstSemesterResitEndDate = LocalDate.parse(params.get("firstSemester.resitPeriodEnd"));

        LocalDate secondSemesterStartDate = LocalDate.parse(params.get("secondSemester.startDate"));
        LocalDate secondSemesterEndDate = LocalDate.parse(params.get("secondSemester.endDate"));
        LocalDate secondSemesterExamStartDate = LocalDate.parse(params.get("secondSemester.examPeriodStart"));
        LocalDate secondSemesterExamEndDate = LocalDate.parse(params.get("secondSemester.examPeriodEnd"));
        LocalDate secondSemesterResitStartDate = LocalDate.parse(params.get("secondSemester.resitPeriodStart"));
        LocalDate secondSemesterResitEndDate = LocalDate.parse(params.get("secondSemester.resitPeriodEnd"));

        LocalDate specialExamStartDate = LocalDate.parse(params.get("specialExamStart"));
        LocalDate specialExamEndDate = LocalDate.parse(params.get("specialExamEnd"));

        if (firstSemesterStartDate.isAfter(firstSemesterEndDate)) {
            model.addAttribute("error", "First semester start date cannot be after end date.");
            return false;
        }
        if (firstSemesterExamStartDate.isAfter(firstSemesterExamEndDate)) {
            model.addAttribute("error", "First semester exam period start date cannot be after end date.");
            return false;
        }
        if (firstSemesterResitStartDate.isAfter(firstSemesterResitEndDate)) {
            model.addAttribute("error", "First semester resit period start date cannot be after end date.");
            return false;
        }
        if (secondSemesterStartDate.isAfter(secondSemesterEndDate)) {
            model.addAttribute("error", "Second semester start date cannot be after end date.");
            return false;
        }
        if (secondSemesterExamStartDate.isAfter(secondSemesterExamEndDate)) {
            model.addAttribute("error", "Second semester exam period start date cannot be after end date.");
            return false;
        }
        if (secondSemesterResitStartDate.isAfter(secondSemesterResitEndDate)) {
            model.addAttribute("error", "Second semester resit period start date cannot be after end date.");
            return false;
        }
        if (specialExamStartDate.isAfter(specialExamEndDate)) {
            model.addAttribute("error", "Special exam start date cannot be after end date.");
            return false;
        }

        Optional<YearUnit> mostRecentYearOpt = getMostRecentYearUnitByDirector(directorId);
        if (mostRecentYearOpt.isPresent()) {
            YearUnit mostRecentYear = mostRecentYearOpt.get();
            LocalDate mostRecentYearEndDate = LocalDate.parse(mostRecentYear.getSecondSemester().getEndDate());
            if (firstSemesterStartDate.isBefore(mostRecentYearEndDate)) {
                model.addAttribute("error", "New year cannot start before the end of the most recent year.");
                return false;
            }
        }

        // New validation logic to check for overlapping dates
        List<YearUnit> directorYears = yearUnitRepository.findByDirectorUnitId(directorId);
        for (YearUnit year : directorYears) {
            if (datesOverlap(year, params)) {
                model.addAttribute("error", "The provided dates overlap with an existing academic year.");
                return false;
            }
        }

        return true;
    }

    private boolean datesOverlap(YearUnit year, Map<String, String> params) {
        LocalDate firstSemesterStartDate = LocalDate.parse(params.get("firstSemester.startDate"));
        LocalDate firstSemesterEndDate = LocalDate.parse(params.get("firstSemester.endDate"));
        LocalDate secondSemesterStartDate = LocalDate.parse(params.get("secondSemester.startDate"));
        LocalDate secondSemesterEndDate = LocalDate.parse(params.get("secondSemester.endDate"));

        LocalDate yearFirstSemesterStartDate = LocalDate.parse(year.getFirstSemester().getStartDate());
        LocalDate yearFirstSemesterEndDate = LocalDate.parse(year.getFirstSemester().getEndDate());
        LocalDate yearSecondSemesterStartDate = LocalDate.parse(year.getSecondSemester().getStartDate());
        LocalDate yearSecondSemesterEndDate = LocalDate.parse(year.getSecondSemester().getEndDate());

        return (firstSemesterStartDate.isBefore(yearSecondSemesterEndDate) && firstSemesterEndDate.isAfter(yearFirstSemesterStartDate)) ||
               (secondSemesterStartDate.isBefore(yearSecondSemesterEndDate) && secondSemesterEndDate.isAfter(yearFirstSemesterStartDate));
    }

    /**
     * Validate the dates for a new YearUnit.
     * 
     * @param params the parameters for the semesters
     * @param model the model to add error messages to
     * @param directorId the ID of the director
     * @return true if the dates are valid, false otherwise
     */
    public boolean validateNewYearDates(Map<String, String> params, Model model, Long directorId) {
        LocalDate firstSemesterStartDate = LocalDate.parse(params.get("firstSemester.startDate"));
        LocalDate firstSemesterEndDate = LocalDate.parse(params.get("firstSemester.endDate"));
        LocalDate secondSemesterStartDate = LocalDate.parse(params.get("secondSemester.startDate"));
        LocalDate secondSemesterEndDate = LocalDate.parse(params.get("secondSemester.endDate"));
        LocalDate specialExamStartDate = LocalDate.parse(params.get("specialExamStart"));
        LocalDate specialExamEndDate = LocalDate.parse(params.get("specialExamEnd"));

        if (firstSemesterStartDate.isAfter(firstSemesterEndDate)) {
            model.addAttribute("error", "First semester start date cannot be after end date.");
            return false;
        }
        if (secondSemesterStartDate.isAfter(secondSemesterEndDate)) {
            model.addAttribute("error", "Second semester start date cannot be after end date.");
            return false;
        }
        if (specialExamStartDate.isAfter(specialExamEndDate)) {
            model.addAttribute("error", "Special exam start date cannot be after end date.");
            return false;
        }

        Optional<YearUnit> mostRecentYearOpt = getMostRecentYearUnitByDirector(directorId);
        if (mostRecentYearOpt.isPresent()) {
            YearUnit mostRecentYear = mostRecentYearOpt.get();
            LocalDate mostRecentYearEndDate = LocalDate.parse(mostRecentYear.getSecondSemester().getEndDate());
            if (firstSemesterStartDate.isBefore(mostRecentYearEndDate)) {
                model.addAttribute("error", "New year cannot start before the end of the most recent year.");
                return false;
            }
        }

        return true;
    }
}