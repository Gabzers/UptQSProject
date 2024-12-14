package com.upt.upt.repository;

import com.upt.upt.entity.DirectorUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class DirectorUnitRepositoryTest {

    @Autowired
    private DirectorUnitRepository directorUnitRepository;

    @BeforeEach
    public void setUp() {
        directorUnitRepository.deleteAll();
    }

    @Test
    public void testSaveDirector() {
        DirectorUnit director = new DirectorUnit();
        director.setName("John Doe");
        director.setDepartment("Computer Science");
        director.setUsername("johndoe");
        director.setPassword("password");

        DirectorUnit savedDirector = directorUnitRepository.save(director);
        assertNotNull(savedDirector.getId());
        assertEquals("John Doe", savedDirector.getName());
    }

    @Test
    public void testFindById() {
        DirectorUnit director = new DirectorUnit();
        director.setName("John Doe");
        director.setDepartment("Computer Science");
        director.setUsername("johndoe");
        director.setPassword("password");

        DirectorUnit savedDirector = directorUnitRepository.save(director);
        Optional<DirectorUnit> foundDirector = directorUnitRepository.findById(savedDirector.getId());
        assertTrue(foundDirector.isPresent());
        assertEquals("John Doe", foundDirector.get().getName());
    }

    @Test
    public void testFindByUsername() {
        DirectorUnit director = new DirectorUnit();
        director.setName("John Doe");
        director.setDepartment("Computer Science");
        director.setUsername("johndoe");
        director.setPassword("password");

        directorUnitRepository.save(director);
        Optional<DirectorUnit> foundDirector = directorUnitRepository.findByUsername("johndoe");
        assertTrue(foundDirector.isPresent());
        assertEquals("John Doe", foundDirector.get().getName());
    }

    @Test
    public void testDeleteById() {
        DirectorUnit director = new DirectorUnit();
        director.setName("John Doe");
        director.setDepartment("Computer Science");
        director.setUsername("johndoe");
        director.setPassword("password");

        DirectorUnit savedDirector = directorUnitRepository.save(director);
        directorUnitRepository.deleteById(savedDirector.getId());

        Optional<DirectorUnit> foundDirector = directorUnitRepository.findById(savedDirector.getId());
        assertFalse(foundDirector.isPresent());
    }
}