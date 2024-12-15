package com.upt.upt.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * MapUnit class represents a map of assessments for a given semester.
 * 
 * @autor Grupo 5 - 47719, 47713, 46697, 47752, 47004
 */
@Entity
@Table(name = "map_unit")
public class MapUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "map_id")
    private Long id;

    @OneToMany(mappedBy = "map", cascade = CascadeType.ALL, orphanRemoval = true)
    @Column(name = "map_assessments")
    private List<AssessmentUnit> assessments = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "semester_unit_id")
    private SemesterUnit semesterUnit;

    // Constructors
    public MapUnit() {}

    /**
     * Constructs a new MapUnit with the specified details.
     * 
     * @param id the ID of the map
     * @param semesterUnit the semester unit associated with the map
     */
    public MapUnit(Long id, SemesterUnit semesterUnit) {
        this.id = id;
        this.semesterUnit = semesterUnit;
    }

    /**
     * Gets the map ID.
     * 
     * @return the map ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the map ID.
     * 
     * @param id the map ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the list of assessments in the map.
     * 
     * @return the list of assessments in the map
     */
    public List<AssessmentUnit> getAssessments() {
        return assessments;
    }

    /**
     * Sets the list of assessments in the map.
     * 
     * @param assessments the list of assessments in the map
     */
    public void setAssessments(List<AssessmentUnit> assessments) {
        this.assessments = assessments;
    }

    /**
     * Gets the semester unit associated with the map.
     * 
     * @return the semester unit associated with the map
     */
    public SemesterUnit getSemesterUnit() {
        return semesterUnit;
    }

    /**
     * Sets the semester unit associated with the map.
     * 
     * @param semesterUnit the semester unit associated with the map
     */
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
