package com.upt.upt.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToOne;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * SemesterUnit class represents a semester entity with details about its periods.
 * 
 * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
 */
@Entity
@Table(name = "semester_unit")
public class SemesterUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "semester_id")
    private Long id;

    @Column(name = "semester_start", nullable = false)
    @NotNull
    private String startDate;

    @Column(name = "semester_end", nullable = false)
    @NotNull
    private String endDate;

    @Column(name = "semester_exam_period_start", nullable = false)
    @NotNull
    private String examPeriodStart;

    @Column(name = "semester_exam_period_end", nullable = false)
    @NotNull
    private String examPeriodEnd;

    @Column(name = "semester_resit_period_start", nullable = false)
    @NotNull
    private String resitPeriodStart;

    @Column(name = "semester_resit_period_end", nullable = false)
    @NotNull
    private String resitPeriodEnd;

    @OneToOne(mappedBy = "semesterUnit", cascade = CascadeType.ALL, orphanRemoval = true)
    private MapUnit mapUnit;

    @OneToMany(mappedBy = "semesterUnit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CurricularUnit> curricularUnits;

    /**
     * Default constructor.
     * 
     * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public SemesterUnit() {}

    /**
     * Constructor with parameters.
     * 
     * @param id the ID of the semester unit
     * @param startDate the start date of the semester
     * @param endDate the end date of the semester
     * @param examPeriodStart the start date of the exam period
     * @param examPeriodEnd the end date of the exam period
     * @param resitPeriodStart the start date of the resit period
     * @param resitPeriodEnd the end date of the resit period
     * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public SemesterUnit(Long id, String startDate, String endDate, String examPeriodStart, String examPeriodEnd,
                        String resitPeriodStart, String resitPeriodEnd) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.examPeriodStart = examPeriodStart;
        this.examPeriodEnd = examPeriodEnd;
        this.resitPeriodStart = resitPeriodStart;
        this.resitPeriodEnd = resitPeriodEnd;
    }

    /**
     * Constructor with parameters including curricular units.
     * 
     * @param id the ID of the semester unit
     * @param startDate the start date of the semester
     * @param endDate the end date of the semester
     * @param examPeriodStart the start date of the exam period
     * @param examPeriodEnd the end date of the exam period
     * @param resitPeriodStart the start date of the resit period
     * @param resitPeriodEnd the end date of the resit period
     * @param curricularUnits the list of curricular units
     * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public SemesterUnit(Long id, String startDate, String endDate, String examPeriodStart, String examPeriodEnd,
                        String resitPeriodStart, String resitPeriodEnd, List<CurricularUnit> curricularUnits) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.examPeriodStart = examPeriodStart;
        this.examPeriodEnd = examPeriodEnd;
        this.resitPeriodStart = resitPeriodStart;
        this.resitPeriodEnd = resitPeriodEnd;
        this.curricularUnits = curricularUnits;
    }

    /**
     * Gets the ID of the semester unit.
     * 
     * @return the ID of the semester unit
     * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the ID of the semester unit.
     * 
     * @param id the ID to set
     * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the start date of the semester.
     * 
     * @return the start date of the semester
     * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * Sets the start date of the semester.
     * 
     * @param startDate the start date to set
     * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * Gets the end date of the semester.
     * 
     * @return the end date of the semester
     * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * Sets the end date of the semester.
     * 
     * @param endDate the end date to set
     * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * Gets the start date of the exam period.
     * 
     * @return the start date of the exam period
     * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public String getExamPeriodStart() {
        return examPeriodStart;
    }

    /**
     * Sets the start date of the exam period.
     * 
     * @param examPeriodStart the start date to set
     * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public void setExamPeriodStart(String examPeriodStart) {
        this.examPeriodStart = examPeriodStart;
    }

    /**
     * Gets the end date of the exam period.
     * 
     * @return the end date of the exam period
     * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public String getExamPeriodEnd() {
        return examPeriodEnd;
    }

    /**
     * Sets the end date of the exam period.
     * 
     * @param examPeriodEnd the end date to set
     * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public void setExamPeriodEnd(String examPeriodEnd) {
        this.examPeriodEnd = examPeriodEnd;
    }

    /**
     * Gets the start date of the resit period.
     * 
     * @return the start date of the resit period
     * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public String getResitPeriodStart() {
        return resitPeriodStart;
    }

    /**
     * Sets the start date of the resit period.
     * 
     * @param resitPeriodStart the start date to set
     * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public void setResitPeriodStart(String resitPeriodStart) {
        this.resitPeriodStart = resitPeriodStart;
    }

    /**
     * Gets the end date of the resit period.
     * 
     * @return the end date of the resit period
     * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public String getResitPeriodEnd() {
        return resitPeriodEnd;
    }

    /**
     * Sets the end date of the resit period.
     * 
     * @param resitPeriodEnd the end date to set
     * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public void setResitPeriodEnd(String resitPeriodEnd) {
        this.resitPeriodEnd = resitPeriodEnd;
    }

    /**
     * Gets the map unit.
     * 
     * @return the map unit
     * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public MapUnit getMapUnit() {
        return mapUnit;
    }

    /**
     * Sets the map unit.
     * 
     * @param mapUnit the map unit to set
     * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public void setMapUnit(MapUnit mapUnit) {
        this.mapUnit = mapUnit;
    }

    /**
     * Gets the list of curricular units.
     * 
     * @return the list of curricular units
     * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public List<CurricularUnit> getCurricularUnits() {
        return curricularUnits;
    }

    /**
     * Sets the list of curricular units.
     * 
     * @param curricularUnits the list of curricular units to set
     * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public void setCurricularUnits(List<CurricularUnit> curricularUnits) {
        this.curricularUnits = curricularUnits;
    }

    /**
     * Adds a curricular unit to the list.
     * 
     * @param curricularUnit the curricular unit to add
     * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public void addCurricularUnit(CurricularUnit curricularUnit) {
        this.curricularUnits.add(curricularUnit);
        curricularUnit.setSemesterUnit(this);
    }

    /**
     * Removes a curricular unit from the list.
     * 
     * @param curricularUnit the curricular unit to remove
     * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public void removeCurricularUnit(CurricularUnit curricularUnit) {
        this.curricularUnits.remove(curricularUnit);
        curricularUnit.setSemesterUnit(null);
    }

    /**
     * Gets the year of the semester.
     * 
     * @return the year of the semester
     * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
     */
    public Integer getYear() {
        return LocalDate.parse(startDate).getYear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SemesterUnit that = (SemesterUnit) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "SemesterUnit{" +
                "id=" + id +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", examPeriodStart='" + examPeriodStart + '\'' +
                ", examPeriodEnd='" + examPeriodEnd + '\'' +
                ", resitPeriodStart='" + resitPeriodStart + '\'' +
                ", resitPeriodEnd='" + resitPeriodEnd + '\'' +
                '}';
    }
}
