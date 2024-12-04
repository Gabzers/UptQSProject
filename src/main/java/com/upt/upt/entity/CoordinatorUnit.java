package com.upt.upt.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * CoordinatorUnit class represents a coordinator entity with details about their course, duration, and curricular units.
 */
@Entity
public class CoordinatorUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coordinator_id")
    private Long id;

    @Column(name = "coordinator_name", nullable = false)
    private String name;

    @Column(name = "coordinator_username", nullable = false, unique = true)
    private String username;

    @Column(name = "coordinator_password", nullable = false)
    private String password;

    @Column(name = "coordinator_course", nullable = true)
    private String course;

    @Column(name = "coordinator_duration", nullable = true)
    private Integer duration;

    @ManyToOne
    @JoinColumn(name = "director_unit_id", nullable = true)  // Nome da coluna que armazena a relação com DirectorUnit
    private DirectorUnit directorUnit;

    @OneToMany(mappedBy = "coordinator", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CurricularUnit> curricularUnits = new ArrayList<>();

    @ManyToMany
    private List<SemesterUnit> semesters = new ArrayList<>();

    // Construtores
    public CoordinatorUnit() {}

    public CoordinatorUnit(Long id, String name, String username, String password, String course, Integer duration, DirectorUnit directorUnit, List<CurricularUnit> curricularUnits) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.course = course;
        this.duration = duration;
        this.directorUnit = directorUnit;
        this.curricularUnits = curricularUnits;
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

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public DirectorUnit getDirectorUnit() {
        return directorUnit;
    }

    public void setDirectorUnit(DirectorUnit directorUnit) {
        this.directorUnit = directorUnit;
    }

    public List<CurricularUnit> getCurricularUnits() {
        return curricularUnits;
    }

    public void setCurricularUnits(List<CurricularUnit> curricularUnits) {
        this.curricularUnits = curricularUnits;
    }

    public List<SemesterUnit> getSemesters() {
        return semesters;
    }

    public void setSemesters(List<SemesterUnit> semesters) {
        this.semesters = semesters;
    }
}
