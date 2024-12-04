package com.upt.upt.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DirectorUnit class represents a director of a department.
 * A DirectorUnit is responsible for managing coordinators and academic years of
 * the department.
 */
@Entity
public class DirectorUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "director_id")
    private Long id;

    @Column(name = "director_name", nullable = false)
    private String name;

    @Column(name = "director_username", nullable = false, unique = true)
    private String username;

    @Column(name = "director_password", nullable = false)
    private String password;

    @Column(name = "director_department", nullable = true)
    private String department;

    @OneToMany(mappedBy = "directorUnit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CoordinatorUnit> coordinators = new ArrayList<>();

    @OneToMany(mappedBy = "directorUnit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<YearUnit> academicYears = new ArrayList<>();

    // Construtores
    public DirectorUnit() {}

    public DirectorUnit(Long id, String name, String username, String password, String department,
            List<CoordinatorUnit> coordinators, List<YearUnit> academicYears) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.department = department;
        this.coordinators = coordinators;
        this.academicYears = academicYears;
    }

    // Getters e setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public List<CoordinatorUnit> getCoordinators() {
        return coordinators;
    }

    public void setCoordinators(List<CoordinatorUnit> coordinators) {
        this.coordinators = coordinators;
    }

    public List<YearUnit> getAcademicYears() {
        return academicYears;
    }

    public void setAcademicYears(List<YearUnit> academicYears) {
        this.academicYears = academicYears;
    }
}
