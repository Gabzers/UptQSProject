package com.upt.upt.entity;

import jakarta.persistence.*;
import java.util.ArrayList;

/**
 * CurricularUnit class represents a curricular unit entity to be mapped to the database.
 * Customized version with ArrayList and user-friendly column names.
 */

@Entity
@Table(name = "curricular_unit")
public class CurricularUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cu_id")
    private Long id; // Curricular Unit ID

    @Column(name = "cu_id_name", nullable = false)
    private String nameUC; // Name of the Curricular Unit

    @Column(name = "cu_id_studentsNumber", nullable = false)
    private Integer studentsNumber; // Number of students enrolled

    @Column(name = "cu_id_type", nullable = false)
    private String typeUC; // Type of Curricular Unit (e.g., "Theory", "Lab")

    // @Transient
    // private ArrayList<Assessment> assessments = new ArrayList<>(); // ArrayList of assessments (not mapped to DB)

    public CurricularUnit() {
    }

    public CurricularUnit(Long id, String nameUC, Integer studentsNumber, String typeUC) {
        this.id = id;
        this.nameUC = nameUC;
        this.studentsNumber = studentsNumber;
        this.typeUC = typeUC;
        // assessments = new ArrayList<>();
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameUC() {
        return nameUC;
    }

    public void setNameUC(String nameUC) {
        this.nameUC = nameUC;
    }

    public Integer getStudentsNumber() {
        return studentsNumber;
    }

    public void setStudentsNumber(Integer studentsNumber) {
        this.studentsNumber = studentsNumber;
    }

    public String getTypeUC() {
        return typeUC;
    }

    public void setTypeUC(String typeUC) {
        this.typeUC = typeUC;
    }

    // public ArrayList<Assessment> getAssessments() {
    //     return assessments;
    // }

    // public void setAssessments(ArrayList<Assessment> assessments) {
    //     this.assessments = assessments;
    // }
}
