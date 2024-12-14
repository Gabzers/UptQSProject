package com.upt.upt.service;

import com.upt.upt.entity.DirectorUnit;
import com.upt.upt.repository.DirectorUnitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class DirectorUnitServiceTest {

    @Autowired
    private DirectorUnitService directorUnitService;

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

        DirectorUnit savedDirector = directorUnitService.saveDirector(director);
        assertNotNull(savedDirector.getId());
        assertEquals("John Doe", savedDirector.getName());
    }

    @Test
    public void testGetAllDirectors() {
        DirectorUnit director1 = new DirectorUnit();
        director1.setName("John Doe");
        director1.setDepartment("Computer Science");
        director1.setUsername("johndoe");
        director1.setPassword("password");

        DirectorUnit director2 = new DirectorUnit();
        director2.setName("Jane Doe");
        director2.setDepartment("Mathematics");
        director2.setUsername("janedoe");
        director2.setPassword("password");

        directorUnitService.saveDirector(director1);
        directorUnitService.saveDirector(director2);

        List<DirectorUnit> directors = directorUnitService.getAllDirectors();
        assertEquals(2, directors.size());
    }

    @Test
    public void testGetDirectorById() {
        DirectorUnit director = new DirectorUnit();
        director.setName("John Doe");
        director.setDepartment("Computer Science");
        director.setUsername("johndoe");
        director.setPassword("password");

        DirectorUnit savedDirector = directorUnitService.saveDirector(director);
        Optional<DirectorUnit> foundDirector = directorUnitService.getDirectorById(savedDirector.getId());
        assertTrue(foundDirector.isPresent());
        assertEquals("John Doe", foundDirector.get().getName());
    }

    @Test
    public void testUpdateDirector() {
        DirectorUnit director = new DirectorUnit();
        director.setName("John Doe");
        director.setDepartment("Computer Science");
        director.setUsername("johndoe");
        director.setPassword("password");

        DirectorUnit savedDirector = directorUnitService.saveDirector(director);

        Map<String, String> params = new HashMap<>();
        params.put("director-name", "John Smith");
        params.put("director-department", "Physics");
        params.put("director-username", "johnsmith");

        DirectorUnit updatedDirector = directorUnitService.updateDirector(savedDirector.getId(), params);
        assertEquals("John Smith", updatedDirector.getName());
        assertEquals("Physics", updatedDirector.getDepartment());
        assertEquals("johnsmith", updatedDirector.getUsername());
    }

    @Test
    public void testDeleteDirector() {
        DirectorUnit director = new DirectorUnit();
        director.setName("John Doe");
        director.setDepartment("Computer Science");
        director.setUsername("johndoe");
        director.setPassword("password");

        DirectorUnit savedDirector = directorUnitService.saveDirector(director);
        directorUnitService.deleteDirector(savedDirector.getId());

        Optional<DirectorUnit> foundDirector = directorUnitService.getDirectorById(savedDirector.getId());
        assertFalse(foundDirector.isPresent());
    }
}