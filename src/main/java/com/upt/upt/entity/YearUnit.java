package com.upt.upt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.Objects;

/**
 * YearUnit class represents an academic year, including information about the
 * semesters and special exam periods.
 */
@Entity
@Table(name = "year_unit")
public class YearUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "year_id")
    private Long id; // Year ID

    @Column(name = "year_first_semester", nullable = false)
    @NotNull
    private String firstSemester; // 1º semestre (Start and End Dates)

    @Column(name = "year_second_semester", nullable = false)
    @NotNull
    private String secondSemester; // 2º semestre (Start and End Dates)

    @Column(name = "year_special_exam_start", nullable = false)
    @NotNull
    private String specialExamStart; // Época especial início

    @Column(name = "year_special_exam_end", nullable = false)
    @NotNull
    private String specialExamEnd; // Época especial fim

    // Relacionamento ManyToOne com a entidade DirectorUnit
    @ManyToOne
    @JoinColumn(name = "director_unit_id", nullable = false)
    private DirectorUnit directorUnit;

    // Constructor
    public YearUnit() {
    }

    public YearUnit(Long id, String firstSemester, String secondSemester, String specialExamStart,
            String specialExamEnd, DirectorUnit directorUnit) {
        this.id = id;
        this.firstSemester = firstSemester;
        this.secondSemester = secondSemester;
        this.specialExamStart = specialExamStart;
        this.specialExamEnd = specialExamEnd;
        this.directorUnit = directorUnit;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstSemester() {
        return firstSemester;
    }

    public void setFirstSemester(String firstSemester) {
        this.firstSemester = firstSemester;
    }

    public String getSecondSemester() {
        return secondSemester;
    }

    public void setSecondSemester(String secondSemester) {
        this.secondSemester = secondSemester;
    }

    public String getSpecialExamStart() {
        return specialExamStart;
    }

    public void setSpecialExamStart(String specialExamStart) {
        this.specialExamStart = specialExamStart;
    }

    public String getSpecialExamEnd() {
        return specialExamEnd;
    }

    public void setSpecialExamEnd(String specialExamEnd) {
        this.specialExamEnd = specialExamEnd;
    }

    public DirectorUnit getDirectorUnit() {
        return directorUnit;
    }

    public void setDirectorUnit(DirectorUnit directorUnit) {
        this.directorUnit = directorUnit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        YearUnit yearUnit = (YearUnit) o;
        return Objects.equals(id, yearUnit.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "YearUnit{" +
                "id=" + id +
                ", firstSemester='" + firstSemester + '\'' +
                ", secondSemester='" + secondSemester + '\'' +
                ", specialExamStart='" + specialExamStart + '\'' +
                ", specialExamEnd='" + specialExamEnd + '\'' +
                '}';
    }
}
