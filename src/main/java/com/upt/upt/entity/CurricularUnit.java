package com.upt.upt.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

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

    @Column(name = "cu_name", nullable = false, length = 100)
    @Size(max = 100)
    private String nameUC; // Name of the Curricular Unit

    @Column(name = "cu_students_number", nullable = false)
    @Min(0)
    private Integer studentsNumber; // Number of students enrolled

    @Column(name = "cu_type", nullable = false, length = 50)
    @Size(max = 50)
    private String evaluationType; // Type of Evaluation (e.g., "Continua", "Mista")

    @Column(name = "cu_attendance", nullable = false)
    private Boolean attendance; // Indicates if attendance is required

    @Column(name = "cu_evaluations_count", nullable = false)
    @Min(0)
    private Integer evaluationsCount; // Number of evaluations planned

    @Column(name = "cu_year", nullable = false)
    @Min(1)
    private Integer year; // Year of the course this UC is taught

    @Column(name = "cu_semester", nullable = false)
    private Integer semester; // Semester of the course this UC is taught

    @ManyToOne
    @JoinColumn(name = "coordinator_id")  // A foreign key to the CoordinatorUnit
    private CoordinatorUnit coordinator; // The coordinator for the curricular unit

    @ManyToOne
    @JoinColumn(name = "semester_id")  // A foreign key to the SemesterUnit
    private SemesterUnit semesterUnit; // The semester associated with the curricular unit

    @OneToMany(mappedBy = "curricularUnit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AssessmentUnit> assessments = new ArrayList<>(); // List of assessments

    // Constructor
    public CurricularUnit() {
    }

    public CurricularUnit(Long id, String nameUC, Integer studentsNumber, String evaluationType,
                          Boolean attendance, Integer evaluationsCount, Integer year, Integer semester, 
                          CoordinatorUnit coordinator, SemesterUnit semesterUnit) {
        this.id = id;
        this.nameUC = nameUC;
        this.studentsNumber = studentsNumber;
        this.evaluationType = evaluationType;
        this.attendance = attendance;
        this.evaluationsCount = evaluationsCount;
        this.year = year;
        this.semester = semester;
        this.coordinator = coordinator;
        this.semesterUnit = semesterUnit;
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

    public CoordinatorUnit getCoordinator() {
        return coordinator;
    }

    public void setCoordinator(CoordinatorUnit coordinator) {
        this.coordinator = coordinator;
    }

    public SemesterUnit getSemesterUnit() {
        return semesterUnit;
    }

    public void setSemesterUnit(SemesterUnit semesterUnit) {
        this.semesterUnit = semesterUnit;
    }

    public List<AssessmentUnit> getAssessments() {
        return assessments;
    }

    public void setAssessments(List<AssessmentUnit> assessments) {
        this.assessments = assessments;
    }

    public boolean hasExamPeriodEvaluation() {
        return assessments.stream().anyMatch(assessment -> "Exam Period".equals(assessment.getExamPeriod()));
    }

    public boolean isMixedAndMissingExamPeriod() {
        return "Mixed".equals(this.evaluationType) && !this.hasExamPeriodEvaluation();
    }

    public boolean isEvaluationsCountMismatch() {
        return this.evaluationsCount != this.assessments.size();
    }

    public boolean isTotalWeightInvalid() {
        int normalPeriodTotalWeight = assessments.stream()
                .filter(e -> "Teaching Period".equals(e.getExamPeriod()) || "Exam Period".equals(e.getExamPeriod()))
                .mapToInt(AssessmentUnit::getWeight)
                .sum();

        int resourcePeriodTotalWeight = assessments.stream()
                .filter(e -> "Resource Period".equals(e.getExamPeriod()))
                .mapToInt(AssessmentUnit::getWeight)
                .sum();

        int specialPeriodTotalWeight = assessments.stream()
                .filter(e -> "Special Period".equals(e.getExamPeriod()))
                .mapToInt(AssessmentUnit::getWeight)
                .sum();

        return normalPeriodTotalWeight != 100 || resourcePeriodTotalWeight != 100 || specialPeriodTotalWeight != 100;
    }

    public boolean isTotalWeightLessThan100() {
        return this.assessments.stream().mapToInt(AssessmentUnit::getWeight).sum() < 100;
    }

    public List<AssessmentUnit> getNormalPeriodAssessments() {
        return assessments.stream()
                .filter(e -> "Teaching Period".equals(e.getExamPeriod()) || "Exam Period".equals(e.getExamPeriod()))
                .collect(Collectors.toList());
    }

    public boolean hasAssessments() {
        return !this.assessments.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurricularUnit that = (CurricularUnit) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "CurricularUnit{" +
                "id=" + id +
                ", nameUC='" + nameUC + '\'' +
                ", studentsNumber=" + studentsNumber +
                ", evaluationType='" + evaluationType + '\'' +
                ", attendance=" + attendance +
                ", evaluationsCount=" + evaluationsCount +
                ", year=" + year +
                ", semester=" + semester +
                '}';
    }
}
