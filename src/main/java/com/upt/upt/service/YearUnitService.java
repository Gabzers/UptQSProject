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

    public List<YearUnit> getAllYearUnits() {
        return yearUnitRepository.findAll();
    }

    public Optional<YearUnit> getYearUnitById(Long id) {
        return yearUnitRepository.findById(id);
    }

    public YearUnit saveYearUnit(YearUnit yearUnit) {
        return yearUnitRepository.save(yearUnit);
    }

    public void deleteYearUnit(Long id) {
        yearUnitRepository.deleteById(id);
    }

    public SemesterUnit saveSemesterUnit(SemesterUnit semesterUnit) {
        return semesterUnitRepository.save(semesterUnit);
    }

    public Optional<YearUnit> getMostRecentYearUnit() {
        return yearUnitRepository.findTopByOrderByIdDesc();
    }

    public Optional<YearUnit> getMostRecentYearUnitByDirector(Long directorId) {
        return yearUnitRepository.findTopByDirectorUnitIdOrderByIdDesc(directorId);
    }

    public Optional<YearUnit> getCurrentYearUnitByDirector(Long directorId) {
        return yearUnitRepository.findByDirectorUnitId(directorId).stream()
                .filter(YearUnit::isCurrentYear)
                .max((y1, y2) -> y1.getFirstSemester().getStartDate().compareTo(y2.getFirstSemester().getStartDate()));
    }

    public Optional<SemesterUnit> getSemesterUnitById(Long id) {
        return semesterUnitRepository.findById(id);
    }

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

    public boolean validateYearDates(Map<String, String> params, Model model) {
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

        Optional<YearUnit> mostRecentYearOpt = getMostRecentYearUnit();
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