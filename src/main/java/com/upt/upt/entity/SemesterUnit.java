package com.upt.upt.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * SemesterUnit class represents a semester entity with details about its periods and associated users and courses.
 */
@Entity
@Table(name = "semester_unit")
public class SemesterUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "semester_id")
    private Long id; // ID of the semester

    @Column(name = "semester_start", nullable = false)
    private String startDate; // Start date of the semester

    @Column(name = "semester_end", nullable = false)
    private String endDate; // End date of the semester

    @Column(name = "semester_exam_period_start", nullable = false)
    private String examPeriodStart; // Start date of the exam period

    @Column(name = "semester_exam_period_end", nullable = false)
    private String examPeriodEnd; // End date of the exam period

    @Column(name = "semester_resit_period_start", nullable = false)
    private String resitPeriodStart; // Start date of the resit period

    @Column(name = "semester_resit_period_end", nullable = false)
    private String resitPeriodEnd; // End date of the resit period

    @ManyToMany(mappedBy = "semesters") // Mapeamento inverso da relação muitos para muitos
    private Set<CoordinatorUnit> coordinators = new HashSet<>(); // List of users associated with the semester

    @OneToMany(mappedBy = "semesterUnit", cascade = CascadeType.ALL, orphanRemoval = true)
    @Column(name = "semester_courses")
    private List<CurricularUnit> courses = new ArrayList<>(); // List of courses associated with the semester

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "map_unit_id")
    private MapUnit map;  // Mapeamento do semestre para o mapa

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

    public List<CurricularUnit> getCourses() {
        return courses;
    }

    public void setCourses(List<CurricularUnit> courses) {
        this.courses = courses;
    }

    public MapUnit getMap() {
        return map;
    }

    public void setMap(MapUnit map) {
        this.map = map;
    }
}
