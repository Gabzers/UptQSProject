package com.upt.upt.controller;

import com.upt.upt.entity.DirectorUnit;
import com.upt.upt.repository.DirectorUnitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class DirectorUnitControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private DirectorUnitRepository directorUnitRepository;

    private String baseUrl;

    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + port + "/api/directors";
        directorUnitRepository.deleteAll();
    }

    @Test
    public void testCreateDirector() {
        DirectorUnit director = new DirectorUnit();
        director.setName("John Doe");
        director.setDepartment("Computer Science");
        director.setUsername("johndoe");
        director.setPassword("password");

        ResponseEntity<DirectorUnit> response = restTemplate.postForEntity(baseUrl, director, DirectorUnit.class);
        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody().getId());
        assertEquals("John Doe", response.getBody().getName());
    }

    @Test
    public void testGetDirectorById() {
        DirectorUnit director = new DirectorUnit();
        director.setName("John Doe");
        director.setDepartment("Computer Science");
        director.setUsername("johndoe");
        director.setPassword("password");

        DirectorUnit savedDirector = directorUnitRepository.save(director);
        ResponseEntity<DirectorUnit> response = restTemplate.getForEntity(baseUrl + "/" + savedDirector.getId(), DirectorUnit.class);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("John Doe", response.getBody().getName());
    }

    @Test
    public void testUpdateDirector() {
        DirectorUnit director = new DirectorUnit();
        director.setName("John Doe");
        director.setDepartment("Computer Science");
        director.setUsername("johndoe");
        director.setPassword("password");

        DirectorUnit savedDirector = directorUnitRepository.save(director);
        savedDirector.setName("Jane Doe");

        HttpEntity<DirectorUnit> requestUpdate = new HttpEntity<>(savedDirector);
        ResponseEntity<DirectorUnit> response = restTemplate.exchange(baseUrl + "/" + savedDirector.getId(), HttpMethod.PUT, requestUpdate, DirectorUnit.class);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Jane Doe", response.getBody().getName());
    }

    @Test
    public void testDeleteDirector() {
        DirectorUnit director = new DirectorUnit();
        director.setName("John Doe");
        director.setDepartment("Computer Science");
        director.setUsername("johndoe");
        director.setPassword("password");

        DirectorUnit savedDirector = directorUnitRepository.save(director);
        restTemplate.delete(baseUrl + "/" + savedDirector.getId());

        Optional<DirectorUnit> foundDirector = directorUnitRepository.findById(savedDirector.getId());
        assertFalse(foundDirector.isPresent());
    }
}