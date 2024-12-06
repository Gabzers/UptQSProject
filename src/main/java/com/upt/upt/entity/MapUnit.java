package com.upt.upt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * MapUnit class represents a map of assessments for a given semester.
 */
@Entity
@Table(name = "map_unit")
public class MapUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "map_id")
    private Long id; // ID of the map

    @OneToMany(mappedBy = "map", cascade = CascadeType.ALL, orphanRemoval = true)
    @Column(name = "map_assessments")
    private List<AssessmentUnit> assessments = new ArrayList<>(); // List of assessments in the map

    @OneToOne(mappedBy = "map")
    private SemesterUnit semesterUnit; // The semester this map is associated with

    // Constructors
    public MapUnit() {}

    public MapUnit(Long id) {
        this.id = id;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<AssessmentUnit> getAssessments() {
        return assessments;
    }

    public void setAssessments(List<AssessmentUnit> assessments) {
        this.assessments = assessments;
    }

    public SemesterUnit getSemesterUnit() {
        return semesterUnit;
    }

    public void setSemesterUnit(SemesterUnit semesterUnit) {
        this.semesterUnit = semesterUnit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MapUnit mapUnit = (MapUnit) o;
        return Objects.equals(id, mapUnit.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "MapUnit{" +
                "id=" + id +
                '}';
    }
}
