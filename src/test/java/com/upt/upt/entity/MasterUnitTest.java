package com.upt.upt.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for MasterUnit.
 * 
 * @autor Grupo 5 - 47719, 47713, 46697, 47752, 47004
 */
public class MasterUnitTest {

    /**
     * Test getters and setters.
     */
    @Test
    void testGettersAndSetters() {
        MasterUnit master = new MasterUnit();

        master.setId(1L);
        master.setName("Test Master");
        master.setUsername("testuser");
        master.setPassword("password123");

        assertEquals(1L, master.getId());
        assertEquals("Test Master", master.getName());
        assertEquals("testuser", master.getUsername());
        assertEquals("password123", master.getPassword());
    }

    /**
     * Test equals method with same object.
     */
    @Test
    void testEquals_SameObject_ShouldReturnTrue() {
        MasterUnit master = new MasterUnit(1L, "Test Master", "testuser", "password123");

        assertTrue(master.equals(master));
    }

    /**
     * Test equals method with different object but same ID.
     */
    @Test
    void testEquals_DifferentObjectSameId_ShouldReturnTrue() {
        MasterUnit master1 = new MasterUnit(1L, "Master One", "user1", "password1");
        MasterUnit master2 = new MasterUnit(1L, "Master Two", "user2", "password2");

        assertTrue(master1.equals(master2));
    }

    /**
     * Test equals method with different IDs.
     */
    @Test
    void testEquals_DifferentId_ShouldReturnFalse() {
        MasterUnit master1 = new MasterUnit(1L, "Master One", "user1", "password1");
        MasterUnit master2 = new MasterUnit(2L, "Master Two", "user2", "password2");

        assertFalse(master1.equals(master2));
    }

    /**
     * Test equals method with null.
     */
    @Test
    void testEquals_Null_ShouldReturnFalse() {
        MasterUnit master = new MasterUnit(1L, "Test Master", "testuser", "password123");

        assertFalse(master.equals(null));
    }

    /**
     * Test hashCode method with same ID.
     */
    @Test
    void testHashCode_SameId_ShouldBeEqual() {
        MasterUnit master1 = new MasterUnit(1L, "Master One", "user1", "password1");
        MasterUnit master2 = new MasterUnit(1L, "Master Two", "user2", "password2");

        assertEquals(master1.hashCode(), master2.hashCode());
    }

    /**
     * Test hashCode method with different IDs.
     */
    @Test
    void testHashCode_DifferentId_ShouldNotBeEqual() {
        MasterUnit master1 = new MasterUnit(1L, "Master One", "user1", "password1");
        MasterUnit master2 = new MasterUnit(2L, "Master Two", "user2", "password2");

        assertNotEquals(master1.hashCode(), master2.hashCode());
    }

    /**
     * Test toString method.
     */
    @Test
    void testToString_ShouldContainAllFields() {
        MasterUnit master = new MasterUnit(1L, "Test Master", "testuser", "password123");

        String toString = master.toString();

        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("name='Test Master'"));
        assertTrue(toString.contains("username='testuser'"));
        assertTrue(toString.contains("password='password123'"));
    }
}
