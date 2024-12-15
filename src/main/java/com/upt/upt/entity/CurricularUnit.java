package com.upt.upt.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * CurricularUnit class represents a curricular unit entity to be mapped to the database.
 * 
 * @autor Grupo 5 - 47719, 47713, 46697, 47752, 47004
 */
@Entity
@Table(name = "curricular_unit")
public class CurricularUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cu_id")
    private Long id;

    @Column(name = "cu_name", nullable = false, length = 100)
    @Size(max = 100)
    private String nameUC;

    @Column(name = "cu_students_number", nullable = false)
    @Min(0)
    private Integer studentsNumber;

    @Column(name = "cu_type", nullable = false, length = 50)
    @Size(max = 50)
    private String evaluationType;

    @Column(name = "cu_attendance", nullable = false)
    private Boolean attendance;

    @Column(name = "cu_evaluations_count", nullable = false)
    @Min(0)
    private Integer evaluationsCount;

    @Column(name = "cu_year", nullable = false)
    @Min(1)
    private Integer year;

    @Column(name = "cu_semester", nullable = false)
    private Integer semester;

    @ManyToOne
    @JoinColumn(name = "coordinator_id")
    private CoordinatorUnit coordinator;

    @ManyToOne
    @JoinColumn(name = "semester_id")
    private SemesterUnit semesterUnit;

    @OneToMany(mappedBy = "curricularUnit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AssessmentUnit> assessments = new ArrayList<>();

    // Constructor
    public CurricularUnit() {
    }

    /**
     * Constructs a new CurricularUnit with the specified details.
     * 
     * @param id the ID of the curricular unit
     * @param nameUC the name of the curricular unit
     * @param studentsNumber the number of students in the curricular unit
     * @param evaluationType the evaluation type of the curricular unit
     * @param attendance the attendance requirement of the curricular unit
     * @param evaluationsCount the number of evaluations in the curricular unit
     * @param year the year of the curricular unit
     * @param semester the semester of the curricular unit
     * @param coordinator the coordinator of the curricular unit
     * @param semesterUnit the semester unit of the curricular unit
     */
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

    /**
     * Gets the curricular unit ID.
     * 
     * @return the curricular unit ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the curricular unit ID.
     * 
     * @param id the curricular unit ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the name of the curricular unit.
     * 
     * @return the name of the curricular unit
     */
    public String getNameUC() {
        return nameUC;
    }

    /**
     * Sets the name of the curricular unit.
     * 
     * @param nameUC the name of the curricular unit
     */
    public void setNameUC(String nameUC) {
        this.nameUC = nameUC;
    }

    /**
     * Gets the number of students in the curricular unit.
     * 
     * @return the number of students in the curricular unit
     */
    public Integer getStudentsNumber() {
        return studentsNumber;
    }

    /**
     * Sets the number of students in the curricular unit.
     * 
     * @param studentsNumber the number of students in the curricular unit
     */
    public void setStudentsNumber(Integer studentsNumber) {
        this.studentsNumber = studentsNumber;
    }

    /**
     * Gets the evaluation type of the curricular unit.
     * 
     * @return the evaluation type of the curricular unit
     */
    public String getEvaluationType() {
        return evaluationType;
    }

    /**
     * Sets the evaluation type of the curricular unit.
     * 
     * @param evaluationType the evaluation type of the curricular unit
     */
    public void setEvaluationType(String evaluationType) {
        this.evaluationType = evaluationType;
    }

    /**
     * Gets the attendance requirement of the curricular unit.
     * 
     * @return the attendance requirement of the curricular unit
     */
    public Boolean getAttendance() {
        return attendance;
    }

    /**
     * Sets the attendance requirement of the curricular unit.
     * 
     * @param attendance the attendance requirement of the curricular unit
     */
    public void setAttendance(Boolean attendance) {
        this.attendance = attendance;
    }

    /**
     * Gets the number of evaluations in the curricular unit.
     * 
     * @return the number of evaluations in the curricular unit
     */
    public Integer getEvaluationsCount() {
        return evaluationsCount;
    }

    /**
     * Sets the number of evaluations in the curricular unit.
     * 
     * @param evaluationsCount the number of evaluations in the curricular unit
     */
    public void setEvaluationsCount(Integer evaluationsCount) {
        this.evaluationsCount = evaluationsCount;
    }

    /**
     * Gets the year of the curricular unit.
     * 
     * @return the year of the curricular unit
     */
    public Integer getYear() {
        return year;
    }

    /**
     * Sets the year of the curricular unit.
     * 
     * @param year the year of the curricular unit
     */
    public void setYear(Integer year) {
        this.year = year;
    }

    /**
     * Gets the semester of the curricular unit.
     * 
     * @return the semester of the curricular unit
     */
    public Integer getSemester() {
        return semester;
    }

    /**
     * Sets the semester of the curricular unit.
     * 
     * @param semester the semester of the curricular unit
     */
    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    /**
     * Gets the coordinator of the curricular unit.
     * 
     * @return the coordinator of the curricular unit
     */
    public CoordinatorUnit getCoordinator() {
        return coordinator;
    }

    /**
     * Sets the coordinator of the curricular unit.
     * 
     * @param coordinator the coordinator of the curricular unit
     */
    public void setCoordinator(CoordinatorUnit coordinator) {
        this.coordinator = coordinator;
    }

    /**
     * Gets the semester unit of the curricular unit.
     * 
     * @return the semester unit of the curricular unit
     */
    public SemesterUnit getSemesterUnit() {
        return semesterUnit;
    }

    /**
     * Sets the semester unit of the curricular unit.
     * 
     * @param semesterUnit the semester unit of the curricular unit
     */
    public void setSemesterUnit(SemesterUnit semesterUnit) {
        this.semesterUnit = semesterUnit;
    }

    /**
     * Gets the list of assessments in the curricular unit.
     * 
     * @return the list of assessments in the curricular unit
     */
    public List<AssessmentUnit> getAssessments() {
        return assessments;
    }

    /**
     * Sets the list of assessments in the curricular unit.
     * 
     * @param assessments the list of assessments in the curricular unit
     */
    public void setAssessments(List<AssessmentUnit> assessments) {
        this.assessments = assessments;
    }

    /**
     * Checks if the curricular unit has an exam period evaluation.
     * 
     * @return true if the curricular unit has an exam period evaluation, false otherwise
     */
    public boolean hasExamPeriodEvaluation() {
        return assessments.stream().anyMatch(assessment -> "Exam Period".equals(assessment.getExamPeriod()));
    }

    /**
     * Checks if the curricular unit is mixed and missing an exam period evaluation.
     * 
     * @return true if the curricular unit is mixed and missing an exam period evaluation, false otherwise
     */
    public boolean isMixedAndMissingExamPeriod() {
        return "Mixed".equals(this.evaluationType) && !this.hasExamPeriodEvaluation();
    }

    /**
     * Checks if the number of evaluations in the curricular unit does not match the expected count.
     * 
     * @return true if the number of evaluations does not match the expected count, false otherwise
     */
    public boolean isEvaluationsCountMismatch() {
        return this.evaluationsCount != this.assessments.size();
    }

    /**
     * Checks if the total weight of the assessments is invalid.
     * 
     * @return true if the total weight of the assessments is invalid, false otherwise
     */
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

        boolean normalPeriodInvalid = normalPeriodTotalWeight > 0 && normalPeriodTotalWeight != 100;
        boolean resourcePeriodInvalid = resourcePeriodTotalWeight > 0 && resourcePeriodTotalWeight != 100;
        boolean specialPeriodInvalid = specialPeriodTotalWeight > 0 && specialPeriodTotalWeight != 100;

        return normalPeriodInvalid || resourcePeriodInvalid || specialPeriodInvalid;
    }

    /**
     * Checks if the total weight of the assessments is less than 100.
     * 
     * @return true if the total weight of the assessments is less than 100, false otherwise
     */
    public boolean isTotalWeightLessThan100() {
        return this.assessments.stream().mapToInt(AssessmentUnit::getWeight).sum() < 100;
    }

    /**
     * Gets the list of normal period assessments.
     * 
     * @return the list of normal period assessments
     */
    public List<AssessmentUnit> getNormalPeriodAssessments() {
        return assessments.stream()
                .filter(e -> "Teaching Period".equals(e.getExamPeriod()) || "Exam Period".equals(e.getExamPeriod()))
                .collect(Collectors.toList());
    }

    /**
     * Checks if the curricular unit has assessments.
     * 
     * @return true if the curricular unit has assessments, false otherwise
     */
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
