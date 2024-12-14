package com.upt.upt.repository;

import com.upt.upt.entity.DirectorUnit;
import com.upt.upt.entity.SemesterUnit;
import com.upt.upt.entity.YearUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class YearUnitRepositoryTest {

    @Autowired
    private YearUnitRepository yearUnitRepository;

    @Test
    @DisplayName("Test findTopByOrderByFirstSemesterStartDateDesc")
    void testFindTopByOrderByFirstSemesterStartDateDesc() {
        // Arrange
        SemesterUnit semester1 = new SemesterUnit();
        semester1.setStartDate("2023-09-01");

        SemesterUnit semester2 = new SemesterUnit();
        semester2.setStartDate("2024-09-01");

        YearUnit yearUnit1 = new YearUnit();
        yearUnit1.setFirstSemester(semester1);
        yearUnit1.setSpecialExamStart("2023-12-01");
        yearUnit1.setSpecialExamEnd("2023-12-15");
        yearUnitRepository.save(yearUnit1);

        YearUnit yearUnit2 = new YearUnit();
        yearUnit2.setFirstSemester(semester2);
        yearUnit2.setSpecialExamStart("2024-12-01");
        yearUnit2.setSpecialExamEnd("2024-12-15");
        yearUnitRepository.save(yearUnit2);

        // Act
        Optional<YearUnit> result = yearUnitRepository.findTopByOrderByFirstSemesterStartDateDesc();

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getFirstSemester().getStartDate()).isEqualTo("2024-09-01");
    }

    @Test
    @DisplayName("Test findTopByOrderByIdDesc")
    void testFindTopByOrderByIdDesc() {
        // Arrange
        YearUnit yearUnit1 = new YearUnit();
        yearUnit1.setSpecialExamStart("2023-12-01");
        yearUnit1.setSpecialExamEnd("2023-12-15");
        yearUnitRepository.save(yearUnit1);

        YearUnit yearUnit2 = new YearUnit();
        yearUnit2.setSpecialExamStart("2024-12-01");
        yearUnit2.setSpecialExamEnd("2024-12-15");
        yearUnitRepository.save(yearUnit2);

        // Act
        Optional<YearUnit> result = yearUnitRepository.findTopByOrderByIdDesc();

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(yearUnit2.getId());
    }

    @Test
    @DisplayName("Test findTopByDirectorUnitIdOrderByIdDesc")
    void testFindTopByDirectorUnitIdOrderByIdDesc() {
        // Arrange
        DirectorUnit director1 = new DirectorUnit();
        DirectorUnit director2 = new DirectorUnit();

        YearUnit yearUnit1 = new YearUnit();
        yearUnit1.setDirectorUnit(director1);
        yearUnit1.setSpecialExamStart("2023-12-01");
        yearUnit1.setSpecialExamEnd("2023-12-15");
        yearUnitRepository.save(yearUnit1);

        YearUnit yearUnit2 = new YearUnit();
        yearUnit2.setDirectorUnit(director1);
        yearUnit2.setSpecialExamStart("2024-12-01");
        yearUnit2.setSpecialExamEnd("2024-12-15");
        yearUnitRepository.save(yearUnit2);

        YearUnit yearUnit3 = new YearUnit();
        yearUnit3.setDirectorUnit(director2);
        yearUnit3.setSpecialExamStart("2024-12-01");
        yearUnit3.setSpecialExamEnd("2024-12-15");
        yearUnitRepository.save(yearUnit3);

        // Act
        Optional<YearUnit> result = yearUnitRepository.findTopByDirectorUnitIdOrderByIdDesc(director1.getId());

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(yearUnit2.getId());
    }

    @Test
    @DisplayName("Test findByDirectorUnitId")
    void testFindByDirectorUnitId() {
        // Arrange
        DirectorUnit director1 = new DirectorUnit();
        DirectorUnit director2 = new DirectorUnit();

        YearUnit yearUnit1 = new YearUnit();
        yearUnit1.setDirectorUnit(director1);
        yearUnit1.setSpecialExamStart("2023-12-01");
        yearUnit1.setSpecialExamEnd("2023-12-15");
        yearUnitRepository.save(yearUnit1);

        YearUnit yearUnit2 = new YearUnit();
        yearUnit2.setDirectorUnit(director1);
        yearUnit2.setSpecialExamStart("2024-12-01");
        yearUnit2.setSpecialExamEnd("2024-12-15");
        yearUnitRepository.save(yearUnit2);

        YearUnit yearUnit3 = new YearUnit();
        yearUnit3.setDirectorUnit(director2);
        yearUnit3.setSpecialExamStart("2024-12-01");
        yearUnit3.setSpecialExamEnd("2024-12-15");
        yearUnitRepository.save(yearUnit3);

        // Act
        var result = yearUnitRepository.findByDirectorUnitId(director1.getId());

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).extracting("directorUnit.id").containsOnly(director1.getId());
    }
}
