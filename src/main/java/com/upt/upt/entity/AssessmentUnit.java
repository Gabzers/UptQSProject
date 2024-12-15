package com.upt.upt.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * AssessmentUnit class represents an assessment entity to be mapped to the database.
 * 
 * grupo 5 - 47719, 47713, 46697, 47752, 47004
 */
@Entity
@Table(name = "assessment_unit")
public class AssessmentUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assessment_id")
    private Long id;

    @Column(name = "assessment_type", nullable = false)
    @NotNull
    private String type;

    @Column(name = "assessment_weight", nullable = false)
    @Min(0)
    @Max(100)
    private Integer weight;

    @Column(name = "assessment_exam_period", nullable = false)
    @NotNull
    private String examPeriod;

    @Column(name = "assessment_computer_required", nullable = false)
    @NotNull
    private Boolean computerRequired;

    @Column(name = "assessment_class_time", nullable = false)
    @NotNull
    private Boolean classTime;

    @Column(name = "assessment_start_time", nullable = false)
    @NotNull
    private LocalDateTime startTime;

    @Column(name = "assessment_end_time", nullable = false)
    @NotNull
    private LocalDateTime endTime;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "assessment_room",
        joinColumns = @JoinColumn(name = "assessment_id"),
        inverseJoinColumns = @JoinColumn(name = "room_id")
    )
    private List<RoomUnit> rooms = new ArrayList<>();

    @Column(name = "assessment_minimum_grade", nullable = false)
    @Min(0)
    @Max(20)
    private Double minimumGrade;

    @ManyToOne
    @JoinColumn(name = "assessment_curricular_unit_id", nullable = false)
    private CurricularUnit curricularUnit;

    @ManyToOne
    @JoinColumn(name = "assessment_map_unit_id", nullable = true)
    private MapUnit map;

    /**
     * Default constructor.
     * 
     * grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public AssessmentUnit() {
    }

    /**
     * Constructs a new AssessmentUnit with the specified details.
     * 
     * @param id the ID of the assessment
     * @param type the type of assessment
     * @param weight the weight of the assessment
     * @param examPeriod the exam period for the assessment
     * @param computerRequired whether a computer is required for the assessment
     * @param classTime whether the exam will be held during class time
     * @param startTime the start date and time of the assessment
     * @param endTime the end date and time of the assessment
     * @param rooms the list of rooms for the assessment
     * @param minimumGrade the minimum grade required for the assessment
     * @param curricularUnit the curricular unit to which the assessment belongs
     * @param map the map to which this assessment belongs
     * 
     * grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public AssessmentUnit(Long id, String type, Integer weight, String examPeriod, Boolean computerRequired,
                          Boolean classTime, LocalDateTime startTime, LocalDateTime endTime, List<RoomUnit> rooms, 
                          Double minimumGrade, CurricularUnit curricularUnit, MapUnit map) {
        this.id = id;
        this.type = type;
        this.weight = weight;
        this.examPeriod = examPeriod;
        this.computerRequired = computerRequired;
        this.classTime = classTime;
        this.startTime = startTime;
        this.endTime = endTime;
        this.rooms = rooms;
        this.minimumGrade = minimumGrade;
        this.curricularUnit = curricularUnit;
        this.map = map;
    }

    /**
     * Gets the assessment ID.
     * 
     * @return the assessment ID
     * 
     * grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the assessment ID.
     * 
     * @param id the assessment ID
     * 
     * grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the type of assessment.
     * 
     * @return the type of assessment
     * 
     * grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type of assessment.
     * 
     * @param type the type of assessment
     * 
     * grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the weight of the assessment.
     * 
     * @return the weight of the assessment
     * 
     * grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public Integer getWeight() {
        return weight;
    }

    /**
     * Sets the weight of the assessment.
     * 
     * @param weight the weight of the assessment
     * 
     * grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    /**
     * Gets the exam period for the assessment.
     * 
     * @return the exam period for the assessment
     * 
     * grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public String getExamPeriod() {
        return examPeriod;
    }

    /**
     * Sets the exam period for the assessment.
     * 
     * @param examPeriod the exam period for the assessment
     * 
     * grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public void setExamPeriod(String examPeriod) {
        this.examPeriod = examPeriod;
    }

    /**
     * Gets whether a computer is required for the assessment.
     * 
     * @return true if a computer is required, false otherwise
     * 
     * grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public Boolean getComputerRequired() {
        return computerRequired;
    }

    /**
     * Sets whether a computer is required for the assessment.
     * 
     * @param computerRequired true if a computer is required, false otherwise
     * 
     * grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public void setComputerRequired(Boolean computerRequired) {
        this.computerRequired = computerRequired;
    }

    /**
     * Gets whether the exam will be held during class time.
     * 
     * @return true if the exam will be held during class time, false otherwise
     * 
     * grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public Boolean getClassTime() {
        return classTime;
    }

    /**
     * Sets whether the exam will be held during class time.
     * 
     * @param classTime true if the exam will be held during class time, false otherwise
     * 
     * grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public void setClassTime(Boolean classTime) {
        this.classTime = classTime;
    }

    /**
     * Gets the start date and time of the assessment.
     * 
     * @return the start date and time of the assessment
     * 
     * grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * Sets the start date and time of the assessment.
     * 
     * @param startTime the start date and time of the assessment
     * 
     * grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets the end date and time of the assessment.
     * 
     * @return the end date and time of the assessment
     * 
     * grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * Sets the end date and time of the assessment.
     * 
     * @param endTime the end date and time of the assessment
     * 
     * grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    /**
     * Gets the list of rooms for the assessment.
     * 
     * @return the list of rooms for the assessment
     * 
     * grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public List<RoomUnit> getRooms() {
        return rooms;
    }

    /**
     * Sets the list of rooms for the assessment.
     * 
     * @param rooms the list of rooms for the assessment
     * 
     * grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public void setRooms(List<RoomUnit> rooms) {
        this.rooms = rooms;
    }

    /**
     * Gets the minimum grade required for the assessment.
     * 
     * @return the minimum grade required for the assessment
     * 
     * grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public Double getMinimumGrade() {
        return minimumGrade;
    }

    /**
     * Sets the minimum grade required for the assessment.
     * 
     * @param minimumGrade the minimum grade required for the assessment
     * 
     * grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public void setMinimumGrade(Double minimumGrade) {
        this.minimumGrade = minimumGrade;
    }

    /**
     * Gets the curricular unit to which the assessment belongs.
     * 
     * @return the curricular unit to which the assessment belongs
     * 
     * grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public CurricularUnit getCurricularUnit() {
        return curricularUnit;
    }

    /**
     * Sets the curricular unit to which the assessment belongs.
     * 
     * @param curricularUnit the curricular unit to which the assessment belongs
     * 
     * grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public void setCurricularUnit(CurricularUnit curricularUnit) {
        this.curricularUnit = curricularUnit;
    }

    /**
     * Gets the map to which this assessment belongs.
     * 
     * @return the map to which this assessment belongs
     * 
     * grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public MapUnit getMap() {
        return map;
    }

    /**
     * Sets the map to which this assessment belongs.
     * 
     * @param map the map to which this assessment belongs
     * 
     * grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public void setMap(MapUnit map) {
        this.map = map;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssessmentUnit that = (AssessmentUnit) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "AssessmentUnit{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", weight=" + weight +
                ", examPeriod='" + examPeriod + '\'' +
                ", computerRequired=" + computerRequired +
                ", classTime=" + classTime +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", rooms=" + rooms +
                ", minimumGrade=" + minimumGrade +
                '}';
    }
}
