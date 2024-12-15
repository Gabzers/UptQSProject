package com.upt.upt.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * DirectorUnit class represents a director of a department.
 * A DirectorUnit is responsible for managing coordinators and academic years of the department.
 * 
 * @autor 
 * grupo 5 - 47719, 47713, 46697, 47752, 47004
 */
@Entity
public class DirectorUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "director_id")
    private Long id;

    @Column(name = "director_name", nullable = false)
    @NotNull
    private String name;

    @Column(name = "director_username", nullable = false, unique = true)
    @NotNull
    private String username;

    @Column(name = "director_password", nullable = false)
    @NotNull
    private String password;

    @Column(name = "director_department", nullable = true)
    private String department;

    @OneToMany(mappedBy = "directorUnit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CoordinatorUnit> coordinators = new ArrayList<>();

    @OneToMany(mappedBy = "directorUnit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<YearUnit> academicYears = new ArrayList<>();

    // Constructors
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

    /**
     * Gets the director ID.
     * 
     * @return the director ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the director ID.
     * 
     * @param id the director ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the name of the director.
     * 
     * @return the name of the director
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the director.
     * 
     * @param name the name of the director
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the username of the director.
     * 
     * @return the username of the director
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the director.
     * 
     * @param username the username of the director
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password of the director.
     * 
     * @return the password of the director
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the director.
     * 
     * @param password the password of the director
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the department of the director.
     * 
     * @return the department of the director
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Sets the department of the director.
     * 
     * @param department the department of the director
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * Gets the list of coordinators managed by the director.
     * 
     * @return the list of coordinators managed by the director
     */
    public List<CoordinatorUnit> getCoordinators() {
        return coordinators;
    }

    /**
     * Sets the list of coordinators managed by the director.
     * 
     * @param coordinators the list of coordinators managed by the director
     */
    public void setCoordinators(List<CoordinatorUnit> coordinators) {
        this.coordinators = coordinators;
    }

    /**
     * Gets the list of academic years managed by the director.
     * 
     * @return the list of academic years managed by the director
     */
    public List<YearUnit> getAcademicYears() {
        return academicYears;
    }

    /**
     * Sets the list of academic years managed by the director.
     * 
     * @param academicYears the list of academic years managed by the director
     */
    public void setAcademicYears(List<YearUnit> academicYears) {
        this.academicYears = academicYears;
    }

    /**
     * Adds a coordinator to the director.
     * 
     * @param coordinator the coordinator to be added
     */
    public void addCoordinator(CoordinatorUnit coordinator) {
        coordinators.add(coordinator);
        coordinator.setDirectorUnit(this);
    }

    /**
     * Adds an academic year to the director.
     * 
     * @param year the academic year to be added
     */
    public void addAcademicYear(YearUnit year) {
        academicYears.add(year);
        year.setDirectorUnit(this);
    }

    /**
     * Gets the current academic year.
     * 
     * @return the current academic year
     */
    public YearUnit getCurrentYear() {
        return this.academicYears.stream()
                .max(Comparator.comparing(YearUnit::getId))
                .orElse(null);
    }

    /**
     * Gets the list of past academic years.
     * 
     * @return the list of past academic years
     */
    public List<YearUnit> getPastYears() {
        YearUnit currentYear = getCurrentYear();
        return academicYears.stream()
                .filter(year -> !year.equals(currentYear))
                .sorted(Comparator.comparing(YearUnit::getId).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DirectorUnit that = (DirectorUnit) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "DirectorUnit{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", department='" + department + '\'' +
                '}';
    }
}
