package com.upt.upt.entity;

import jakarta.persistence.*;

/**
 * Master class extends User and represents a master user with additional attributes.
 */
@Entity
public class MasterUnit extends UserUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "master_id")
    private Long id; // Master ID

    // Constructors
    public MasterUnit() {
        super();
    }

    public MasterUnit(Long id, String name, String username, String password) {
        super(id, name, username, password); // Calls the constructor of the User class
        this.id = id;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
