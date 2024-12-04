package com.upt.upt.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * AssessmentUnit class represents an assessment entity to be mapped to the database.
 */
@Entity
@Table(name = "assessment_unit")
public class AssessmentUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assessment_id")
    private Long id; // Assessment ID

    @Column(name = "assessment_type", nullable = false)
    private String type; // Type of assessment (e.g., "Test", "Presentation")

    @Column(name = "assessment_weight", nullable = false)
    private Integer weight; // Weight of the assessment (in percentage)

    @Column(name = "assessment_exam_period", nullable = false)
    private String examPeriod; // Exam period for the assessment (e.g., "Teaching Period", "Exam Period")

    @Column(name = "assessment_computer_required", nullable = false)
    private Boolean computerRequired; // Indicates if a computer is required for the assessment

    @Column(name = "assessment_class_time", nullable = false)
    private Boolean classTime; // Indicates if the exam will be held during class time

    @Column(name = "assessment_start_time", nullable = false)
    private LocalDateTime startTime; // Start date and time of the assessment

    @Column(name = "assessment_end_time", nullable = false)
    private LocalDateTime endTime; // End date and time of the assessment

    @Column(name = "assessment_room", nullable = false)
    private String room; // Room where the assessment will take place

    @Column(name = "assessment_minimum_grade", nullable = false)
    private Double minimumGrade; // Minimum grade required for the assessment

    @ManyToOne
    @JoinColumn(name = "assessment_curricular_unit_id", nullable = false)
    private CurricularUnit curricularUnit; // The curricular unit to which the assessment belongs

    @ManyToOne
    @JoinColumn(name = "assessment_map_unit_id", nullable = true)  // Permite que o valor de 'map' seja nulo
    private MapUnit map; // The map to which this assessment belongs

    public AssessmentUnit() {
    }

    public AssessmentUnit(Long id, String type, Integer weight, String examPeriod, Boolean computerRequired,
                          Boolean classTime, LocalDateTime startTime, LocalDateTime endTime, String room, 
                          Double minimumGrade, CurricularUnit curricularUnit, MapUnit map) {
        this.id = id;
        this.type = type;
        this.weight = weight;
        this.examPeriod = examPeriod;
        this.computerRequired = computerRequired;
        this.classTime = classTime;
        this.startTime = startTime;
        this.endTime = endTime;
        this.room = room;
        this.minimumGrade = minimumGrade;
        this.curricularUnit = curricularUnit;
        this.map = map;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getExamPeriod() {
        return examPeriod;
    }

    public void setExamPeriod(String examPeriod) {
        this.examPeriod = examPeriod;
    }

    public Boolean getComputerRequired() {
        return computerRequired;
    }

    public void setComputerRequired(Boolean computerRequired) {
        this.computerRequired = computerRequired;
    }

    public Boolean getClassTime() {
        return classTime;
    }

    public void setClassTime(Boolean classTime) {
        this.classTime = classTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public Double getMinimumGrade() {
        return minimumGrade;
    }

    public void setMinimumGrade(Double minimumGrade) {
        this.minimumGrade = minimumGrade;
    }

    public CurricularUnit getCurricularUnit() {
        return curricularUnit;
    }

    public void setCurricularUnit(CurricularUnit curricularUnit) {
        this.curricularUnit = curricularUnit;
    }

    public MapUnit getMap() {
        return map;
    }

    public void setMap(MapUnit map) {
        this.map = map;
    }
}
