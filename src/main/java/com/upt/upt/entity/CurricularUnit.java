package com.upt.upt.entity;

import jakarta.persistence.*;

/**
 * CurricularUnit class represents a curricular unit entity to be mapped to the database.
 */

@Entity
@Table(name = "curricular_unit")
public class CurricularUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cu_id")
    private Long id; // Curricular Unit ID

    @Column(name = "cu_name", nullable = false)
    private String nameUC; // Name of the Curricular Unit

    @Column(name = "cu_students_number", nullable = false)
    private Integer studentsNumber; // Number of students enrolled

    @Column(name = "cu_type", nullable = false)
    private String evaluationType; // Type of Evaluation (e.g., "Continua", "Mista")

    @Column(name = "cu_attendance", nullable = false)
    private Boolean attendance; // Indicates if attendance is required

    @Column(name = "cu_evaluations_count", nullable = false)
    private Integer evaluationsCount; // Number of evaluations planned

    @Column(name = "cu_year", nullable = false)
    private Integer year; // Year of the course this UC is taught

    @Column(name = "cu_semester", nullable = false)
    private Integer semester; // Semester of the course this UC is taught

    // @OneToMany(mappedBy = "curricularUnit", cascade = CascadeType.ALL, orphanRemoval = true)
    // private List<Assessment> assessments = new ArrayList<>(); // List of assessments

    public CurricularUnit() {
    }

    public CurricularUnit(Long id, String nameUC, Integer studentsNumber, String evaluationType,
                          Boolean attendance, Integer evaluationsCount, Integer year, Integer semester) {
        this.id = id;
        this.nameUC = nameUC;
        this.studentsNumber = studentsNumber;
        this.evaluationType = evaluationType;
        this.attendance = attendance;
        this.evaluationsCount = evaluationsCount;
        this.year = year;
        this.semester = semester;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameUC() {
        return nameUC;
    }

    public void setNameUC(String nameUC) {
        this.nameUC = nameUC;
    }

    public Integer getStudentsNumber() {
        return studentsNumber;
    }

    public void setStudentsNumber(Integer studentsNumber) {
        this.studentsNumber = studentsNumber;
    }

    public String getEvaluationType() {
        return evaluationType;
    }

    public void setEvaluationType(String evaluationType) {
        this.evaluationType = evaluationType;
    }

    public Boolean getAttendance() {
        return attendance;
    }

    public void setAttendance(Boolean attendance) {
        this.attendance = attendance;
    }

    public Integer getEvaluationsCount() {
        return evaluationsCount;
    }

    public void setEvaluationsCount(Integer evaluationsCount) {
        this.evaluationsCount = evaluationsCount;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    // public List<Assessment> getAssessments() {
    //     return assessments;
    // }

    // public void setAssessments(List<Assessment> assessments) {
    //     this.assessments = assessments;
    // }
}
