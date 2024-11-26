package com.upt.upt.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

/**
 * CoordinatorUnity class represents a coordinator entity with details about their course, duration, and curricular units.
 */
@Entity
public class CoordinatorUnit extends UserUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coordinator_id")
    private Long id;

    @Column(name = "coordinator_course", nullable = false)
    private String course;

    @Column(name = "coordinator_duration", nullable = false)
    private Integer duration;

    @ManyToOne
    @JoinColumn(name = "director_unit_id", nullable = false)  // Nome da coluna que armazena a relação com DirectorUnit
    private DirectorUnit directorUnity;  // Adicionar referência ao DirectorUnit

    @OneToMany(mappedBy = "coordinator", cascade = CascadeType.ALL, orphanRemoval = true)
    @Column(name = "coordinator_uc_list")
    private List<CurricularUnit> curricularUnits = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "coordinator_semester",  // Nome da tabela intermediária
        joinColumns = @JoinColumn(name = "coordinator_id"),  // Coluna de chave estrangeira para CoordinatorUnit
        inverseJoinColumns = @JoinColumn(name = "semester_id")  // Coluna de chave estrangeira para SemesterUnit
    )
    private List<SemesterUnit> semesters = new ArrayList<>();  // Semestres associados ao coordenador

    // Construtores, getters e setters
    public CoordinatorUnit() {}

    public CoordinatorUnit(Long id, String name, String username, String password, String course, Integer duration, DirectorUnit directorUnity, List<CurricularUnit> curricularUnits) {
        super(id, name, username, password);
        this.id = id;
        this.course = course;
        this.duration = duration;
        this.directorUnity = directorUnity;
        this.curricularUnits = curricularUnits;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public DirectorUnit getDirectorUnity() {
        return directorUnity;
    }

    public void setDirectorUnity(DirectorUnit directorUnity) {
        this.directorUnity = directorUnity;
    }

    public List<CurricularUnit> getCurricularUnits() {
        return curricularUnits;
    }

}
