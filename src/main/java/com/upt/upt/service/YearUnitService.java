package com.upt.upt.service;

import com.upt.upt.entity.SemesterUnit;
import com.upt.upt.entity.YearUnit;
import com.upt.upt.repository.SemesterUnitRepository;
import com.upt.upt.repository.YearUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class YearUnitService {

    @Autowired
    private YearUnitRepository yearUnitRepository;

    @Autowired
    private SemesterUnitRepository semesterUnitRepository;

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
}