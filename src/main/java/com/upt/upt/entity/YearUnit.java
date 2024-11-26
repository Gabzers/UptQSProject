package com.upt.upt.entity;

import jakarta.persistence.*;

/**
 * YearUnit class represents an academic year, including information about the semesters and special exam periods.
 */
@Entity
@Table(name = "year_unit")
public class YearUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "year_id")
    private Long id; // Year ID

    @Column(name = "year_first_semester", nullable = false)
    private String firstSemester; // 1º semestre (Start and End Dates)

    @Column(name = "year_second_semester", nullable = false)
    private String secondSemester; // 2º semestre (Start and End Dates)

    @Column(name = "year_special_exam_start", nullable = false)
    private String specialExamStart; // Época especial início

    @Column(name = "year_special_exam_end", nullable = false)
    private String specialExamEnd; // Época especial fim

    // Relacionamento ManyToOne com a entidade DirectorUnit
    @ManyToOne
    @JoinColumn(name = "director_unit_id", nullable = false) // Chave estrangeira para DirectorUnit
    private DirectorUnit directorUnity;

    // Constructor
    public YearUnit() {}

    public YearUnit(Long id, String firstSemester, String secondSemester, String specialExamStart, String specialExamEnd, DirectorUnit directorUnity) {
        this.id = id;
        this.firstSemester = firstSemester;
        this.secondSemester = secondSemester;
        this.specialExamStart = specialExamStart;
        this.specialExamEnd = specialExamEnd;
        this.directorUnity = directorUnity;
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

    public DirectorUnit getDirectorUnity() {
        return directorUnity;
    }

    public void setDirectorUnity(DirectorUnit directorUnity) {
        this.directorUnity = directorUnity;
    }
}
