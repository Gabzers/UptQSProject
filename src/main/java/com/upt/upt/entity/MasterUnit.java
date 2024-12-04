package com.upt.upt.entity;

import jakarta.persistence.*;

/**
 * MasterUnit class represents a master user with additional attributes.
 */
@Entity
public class MasterUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "master_id")
    private Long id; // Master ID

    @Column(name = "master_name", nullable = false)
    private String name;

    @Column(name = "master_username", nullable = false, unique = true)
    private String username;

    @Column(name = "master_password", nullable = false)
    private String password;

    // Construtores
    public MasterUnit() {}

    public MasterUnit(Long id, String name, String username, String password) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
    }

    // Getters e Setters
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
}
