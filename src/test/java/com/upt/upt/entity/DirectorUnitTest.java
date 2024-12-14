package com.upt.upt.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DirectorUnitTest {

    @Test
    public void testDirectorUnitConstructorAndGetters() {
        DirectorUnit director = new DirectorUnit();
        director.setName("John Doe");
        director.setDepartment("Computer Science");
        director.setUsername("johndoe");
        director.setPassword("password");

        assertEquals("John Doe", director.getName());
        assertEquals("Computer Science", director.getDepartment());
        assertEquals("johndoe", director.getUsername());
        assertEquals("password", director.getPassword());
    }

    @Test
    public void testDirectorUnitSetters() {
        DirectorUnit director = new DirectorUnit();
        director.setName("Jane Doe");
        director.setDepartment("Mathematics");
        director.setUsername("janedoe");
        director.setPassword("newpassword");

        assertEquals("Jane Doe", director.getName());
        assertEquals("Mathematics", director.getDepartment());
        assertEquals("janedoe", director.getUsername());
        assertEquals("newpassword", director.getPassword());
    }

    @Test
    public void testDirectorUnitEqualsAndHashCode() {
        DirectorUnit director1 = new DirectorUnit();
        director1.setName("John Doe");
        director1.setDepartment("Computer Science");
        director1.setUsername("johndoe");
        director1.setPassword("password");

        DirectorUnit director2 = new DirectorUnit();
        director2.setName("John Doe");
        director2.setDepartment("Computer Science");
        director2.setUsername("johndoe");
        director2.setPassword("password");

        assertEquals(director1, director2);
        assertEquals(director1.hashCode(), director2.hashCode());
    }
}