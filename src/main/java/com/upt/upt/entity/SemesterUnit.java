package com.upt.upt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

/**
 * SemesterUnit class represents a semester entity with details about its periods.
 */
@Entity
@Table(name = "semester_unit")
public class SemesterUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "semester_id")
    private Long id; // ID of the semester

    @Column(name = "semester_start", nullable = false)
    @NotNull
    private String startDate; // Start date of the semester

    @Column(name = "semester_end", nullable = false)
    @NotNull
    private String endDate; // End date of the semester

    @Column(name = "semester_exam_period_start", nullable = false)
    @NotNull
    private String examPeriodStart; // Start date of the exam period

    @Column(name = "semester_exam_period_end", nullable = false)
    @NotNull
    private String examPeriodEnd; // End date of the exam period

    @Column(name = "semester_resit_period_start", nullable = false)
    @NotNull
    private String resitPeriodStart; // Start date of the resit period

    @Column(name = "semester_resit_period_end", nullable = false)
    @NotNull
    private String resitPeriodEnd; // End date of the resit period

    @OneToOne(mappedBy = "semesterUnit")
    private MapUnit map; // Map associated with the semester

    // Constructors
    public SemesterUnit() {}

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

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getExamPeriodStart() {
        return examPeriodStart;
    }

    public void setExamPeriodStart(String examPeriodStart) {
        this.examPeriodStart = examPeriodStart;
    }

    public String getExamPeriodEnd() {
        return examPeriodEnd;
    }

    public void setExamPeriodEnd(String examPeriodEnd) {
        this.examPeriodEnd = examPeriodEnd;
    }

    public String getResitPeriodStart() {
        return resitPeriodStart;
    }

    public void setResitPeriodStart(String resitPeriodStart) {
        this.resitPeriodStart = resitPeriodStart;
    }

    public String getResitPeriodEnd() {
        return resitPeriodEnd;
    }

    public void setResitPeriodEnd(String resitPeriodEnd) {
        this.resitPeriodEnd = resitPeriodEnd;
    }

    public MapUnit getMap() {
        return map;
    }

    public void setMap(MapUnit map) {
        this.map = map;
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
                ", map=" + map +
                '}';
    }
}
