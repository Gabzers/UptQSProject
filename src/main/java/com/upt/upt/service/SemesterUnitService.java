package com.upt.upt.service;

import com.upt.upt.entity.SemesterUnit;
import com.upt.upt.repository.SemesterUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SemesterUnitService {

    @Autowired
    private SemesterUnitRepository semesterUnitRepository;

    public SemesterUnit saveSemesterUnit(SemesterUnit semesterUnit) {
        return semesterUnitRepository.save(semesterUnit);
    }

    public Optional<SemesterUnit> getSemesterUnitById(Long id) {
        return semesterUnitRepository.findById(id);
    }

    public void deleteSemesterUnit(Long id) {
        semesterUnitRepository.deleteById(id);
    }
}
