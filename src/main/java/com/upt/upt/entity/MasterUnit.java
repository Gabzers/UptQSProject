package com.upt.upt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.Objects;

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
    @NotNull
    private String name;

    @Column(name = "master_username", nullable = false, unique = true)
    @NotNull
    private String username;

    @Column(name = "master_password", nullable = false)
    @NotNull
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MasterUnit that = (MasterUnit) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "MasterUnit{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
