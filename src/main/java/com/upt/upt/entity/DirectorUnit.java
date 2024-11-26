package com.upt.upt.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

/**
 * DirectorUnity class represents a director of a department, extending the User class.
 * A DirectorUnity is responsible for managing coordinators and academic years of the department.
 */
@Entity
public class DirectorUnit extends UserUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "director_id")
    private Long id;

    @Column(name = "director_department", nullable = false)
    private String department;

    @OneToMany(mappedBy = "directorUnity", cascade = CascadeType.ALL, orphanRemoval = true)
    @Column(name = "director_coordinators")
    private ArrayList<CoordinatorUnit> coordinators;

    @OneToMany(mappedBy = "directorUnity", cascade = CascadeType.ALL, orphanRemoval = true)
    @Column(name = "director_academic_years")
    private List<YearUnit> academicYears = new ArrayList<>();

    // Construtores, getters e setters
    public DirectorUnit() {}

    public DirectorUnit(Long id, String department, ArrayList<CoordinatorUnit> coordinators, List<YearUnit> academicYears) {
        this.id = id;
        this.department = department;
        this.coordinators = coordinators;
        this.academicYears = academicYears;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public ArrayList<CoordinatorUnit> getCoordinators() {
        return coordinators;
    }

    public void setCoordinators(ArrayList<CoordinatorUnit> coordinators) {
        this.coordinators = coordinators;
    }

    public List<YearUnit> getAcademicYears() {
        return academicYears;
    }

    public void setAcademicYears(List<YearUnit> academicYears) {
        this.academicYears = academicYears;
    }
}
