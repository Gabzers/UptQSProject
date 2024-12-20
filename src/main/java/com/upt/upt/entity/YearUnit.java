package com.upt.upt.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToOne;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;
import java.time.LocalDate;

/**
 * YearUnit class represents an academic year, including information about the
 * semesters and special exam periods.
 * 
 * @author grupo 5 - 47719, 47713, 46697, 47752, 47004
 */
@Entity
@Table(name = "year_unit")
public class YearUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "year_id")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "year_first_semester", referencedColumnName = "semester_id")
    private SemesterUnit firstSemester;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "year_second_semester", referencedColumnName = "semester_id")
    private SemesterUnit secondSemester;

    @Column(name = "year_special_exam_start", nullable = false)
    @NotNull
    private String specialExamStart;

    @Column(name = "year_special_exam_end", nullable = false)
    @NotNull
    private String specialExamEnd;

    @ManyToOne
    @JoinColumn(name = "year_director_unit_id", nullable = false)
    private DirectorUnit directorUnit;

    /**
     * Default constructor.
     * 
     * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public YearUnit() {
    }

    /**
     * Constructor with parameters.
     * 
     * @param id the ID of the year unit
     * @param firstSemester the first semester unit
     * @param secondSemester the second semester unit
     * @param specialExamStart the start date of the special exam period
     * @param specialExamEnd the end date of the special exam period
     * @param directorUnit the director unit
     * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public YearUnit(Long id, SemesterUnit firstSemester, SemesterUnit secondSemester, String specialExamStart,
            String specialExamEnd, DirectorUnit directorUnit) {
        this.id = id;
        this.firstSemester = firstSemester;
        this.secondSemester = secondSemester;
        this.specialExamStart = specialExamStart;
        this.specialExamEnd = specialExamEnd;
        this.directorUnit = directorUnit;
    }

    /**
     * Gets the ID of the year unit.
     * 
     * @return the ID of the year unit
     * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the ID of the year unit.
     * 
     * @param id the ID to set
     * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the first semester unit.
     * 
     * @return the first semester unit
     * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public SemesterUnit getFirstSemester() {
        return firstSemester;
    }

    /**
     * Sets the first semester unit.
     * 
     * @param firstSemester the first semester unit to set
     * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public void setFirstSemester(SemesterUnit firstSemester) {
        this.firstSemester = firstSemester;
    }

    /**
     * Gets the second semester unit.
     * 
     * @return the second semester unit
     * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public SemesterUnit getSecondSemester() {
        return secondSemester;
    }

    /**
     * Sets the second semester unit.
     * 
     * @param secondSemester the second semester unit to set
     * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public void setSecondSemester(SemesterUnit secondSemester) {
        this.secondSemester = secondSemester;
    }

    /**
     * Gets the start date of the special exam period.
     * 
     * @return the start date of the special exam period
     * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public String getSpecialExamStart() {
        return specialExamStart;
    }

    /**
     * Sets the start date of the special exam period.
     * 
     * @param specialExamStart the start date to set
     * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public void setSpecialExamStart(String specialExamStart) {
        this.specialExamStart = specialExamStart;
    }

    /**
     * Gets the end date of the special exam period.
     * 
     * @return the end date of the special exam period
     * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public String getSpecialExamEnd() {
        return specialExamEnd;
    }

    /**
     * Sets the end date of the special exam period.
     * 
     * @param specialExamEnd the end date to set
     * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public void setSpecialExamEnd(String specialExamEnd) {
        this.specialExamEnd = specialExamEnd;
    }

    /**
     * Gets the director unit.
     * 
     * @return the director unit
     * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public DirectorUnit getDirectorUnit() {
        return directorUnit;
    }

    /**
     * Sets the director unit.
     * 
     * @param directorUnit the director unit to set
     * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public void setDirectorUnit(DirectorUnit directorUnit) {
        this.directorUnit = directorUnit;
    }

    /**
     * Checks if the current date is within the academic year.
     * 
     * @return true if the current date is within the academic year, false otherwise
     * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public boolean isCurrentYear() {
        LocalDate now = LocalDate.now();
        LocalDate firstSemesterStartDate = LocalDate.parse(firstSemester.getStartDate());
        LocalDate secondSemesterEndDate = LocalDate.parse(secondSemester.getEndDate());
        return (now.isEqual(firstSemesterStartDate) || now.isAfter(firstSemesterStartDate)) && now.isBefore(secondSemesterEndDate);
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
                ", firstSemester=" + firstSemester +
                ", secondSemester=" + secondSemester +
                ", specialExamStart='" + specialExamStart + '\'' +
                ", specialExamEnd='" + specialExamEnd + '\'' +
                ", directorUnit=" + directorUnit +
                '}';
    }
}
