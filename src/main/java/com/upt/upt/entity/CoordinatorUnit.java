package com.upt.upt.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * CoordinatorUnit class represents a coordinator entity with details about their course, duration, and curricular units.
 * 
 * @author 
 * grupo 5 - 47719, 47713, 46697, 47752, 47004
 */
@Entity
public class CoordinatorUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coordinator_id")
    private Long id;

    @Column(name = "coordinator_name", nullable = false)
    @NotNull
    private String name;

    @Column(name = "coordinator_username", nullable = false, unique = true)
    @NotNull
    private String username;

    @Column(name = "coordinator_password", nullable = false)
    @NotNull
    private String password;

    @Column(name = "coordinator_course")
    private String course;

    @Column(name = "coordinator_duration")
    private Integer duration;

    @ManyToOne
    @JoinColumn(name = "director_unit_id")
    private DirectorUnit directorUnit;

    @OneToMany(mappedBy = "coordinator", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CurricularUnit> curricularUnits = new ArrayList<>();

    @ManyToMany
    private List<SemesterUnit> semesters = new ArrayList<>();

    // Constructors
    public CoordinatorUnit() {}

    /**
     * Constructs a new CoordinatorUnit with the specified details.
     * 
     * @param id the ID of the coordinator
     * @param name the name of the coordinator
     * @param username the username of the coordinator
     * @param password the password of the coordinator
     * @param course the course of the coordinator
     * @param duration the duration of the coordinator's course
     * @param directorUnit the director unit associated with the coordinator
     * @param curricularUnits the list of curricular units associated with the coordinator
     */
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

    /**
     * Gets the coordinator ID.
     * 
     * @return the coordinator ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the coordinator ID.
     * 
     * @param id the coordinator ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the name of the coordinator.
     * 
     * @return the name of the coordinator
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the coordinator.
     * 
     * @param name the name of the coordinator
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the username of the coordinator.
     * 
     * @return the username of the coordinator
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the coordinator.
     * 
     * @param username the username of the coordinator
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password of the coordinator.
     * 
     * @return the password of the coordinator
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the coordinator.
     * 
     * @param password the password of the coordinator
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the course of the coordinator.
     * 
     * @return the course of the coordinator
     */
    public String getCourse() {
        return course;
    }

    /**
     * Sets the course of the coordinator.
     * 
     * @param course the course of the coordinator
     */
    public void setCourse(String course) {
        this.course = course;
    }

    /**
     * Gets the duration of the coordinator's course.
     * 
     * @return the duration of the coordinator's course
     */
    public Integer getDuration() {
        return duration;
    }

    /**
     * Sets the duration of the coordinator's course.
     * 
     * @param duration the duration of the coordinator's course
     */
    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    /**
     * Gets the director unit associated with the coordinator.
     * 
     * @return the director unit associated with the coordinator
     */
    public DirectorUnit getDirectorUnit() {
        return directorUnit;
    }

    /**
     * Sets the director unit associated with the coordinator.
     * 
     * @param directorUnit the director unit associated with the coordinator
     */
    public void setDirectorUnit(DirectorUnit directorUnit) {
        this.directorUnit = directorUnit;
    }

    /**
     * Gets the list of curricular units associated with the coordinator.
     * 
     * @return the list of curricular units associated with the coordinator
     */
    public List<CurricularUnit> getCurricularUnits() {
        return curricularUnits;
    }

    /**
     * Sets the list of curricular units associated with the coordinator.
     * 
     * @param curricularUnits the list of curricular units associated with the coordinator
     */
    public void setCurricularUnits(List<CurricularUnit> curricularUnits) {
        this.curricularUnits = curricularUnits;
    }

    /**
     * Gets the list of semesters associated with the coordinator.
     * 
     * @return the list of semesters associated with the coordinator
     */
    public List<SemesterUnit> getSemesters() {
        return semesters;
    }

    /**
     * Sets the list of semesters associated with the coordinator.
     * 
     * @param semesters the list of semesters associated with the coordinator
     */
    public void setSemesters(List<SemesterUnit> semesters) {
        this.semesters = semesters;
    }

    /**
     * Adds a curricular unit to the coordinator.
     * 
     * @param curricularUnit the curricular unit to be added
     */
    public void addCurricularUnit(CurricularUnit curricularUnit) {
        curricularUnits.add(curricularUnit);
        curricularUnit.setCoordinator(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoordinatorUnit that = (CoordinatorUnit) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "CoordinatorUnit{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", course='" + course + '\'' +
                ", duration=" + duration +
                '}';
    }
}
