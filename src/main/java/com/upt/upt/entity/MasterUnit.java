package com.upt.upt.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

/**
 * MasterUnit class represents a master user with additional attributes.
 * 
 * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
 */
@Entity
public class MasterUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "master_id")
    private Long id;

    @Column(name = "master_name", nullable = false)
    @NotNull
    private String name;

    @Column(name = "master_username", nullable = false, unique = true)
    @NotNull
    private String username;

    @Column(name = "master_password", nullable = false)
    @NotNull
    private String password;

    // Constructors
    public MasterUnit() {}

    /**
     * Constructs a new MasterUnit with the specified details.
     * 
     * @param id the ID of the master
     * @param name the name of the master
     * @param username the username of the master
     * @param password the password of the master
     */
    public MasterUnit(Long id, String name, String username, String password) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
    }

    /**
     * Gets the master ID.
     * 
     * @return the master ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the master ID.
     * 
     * @param id the master ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the name of the master.
     * 
     * @return the name of the master
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the master.
     * 
     * @param name the name of the master
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the username of the master.
     * 
     * @return the username of the master
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the master.
     * 
     * @param username the username of the master
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password of the master.
     * 
     * @return the password of the master
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the master.
     * 
     * @param password the password of the master
     */
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
