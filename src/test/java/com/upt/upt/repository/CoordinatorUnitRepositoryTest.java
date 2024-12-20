package com.upt.upt.repository;

import com.upt.upt.entity.CoordinatorUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for CoordinatorUnitRepository.
 * 
 * @autor Grupo 5 - 47719, 47713, 46697, 47752, 47004
 */
@DataJpaTest
class CoordinatorUnitRepositoryTest {

    @Autowired
    private CoordinatorUnitRepository repository;

    /**
     * Test findByUsernameAndPassword method.
     */
    @Test
    void testFindByUsernameAndPassword() {
        // Arrange
        CoordinatorUnit coordinator = new CoordinatorUnit();
        coordinator.setName("John Doe");
        coordinator.setUsername("jdoe");
        coordinator.setPassword("password123");
        repository.save(coordinator);

        // Act
        CoordinatorUnit result = repository.findByUsernameAndPassword("jdoe", "password123");

        // Assert
        assertEquals("John Doe", result.getName());
        assertEquals("jdoe", result.getUsername());
    }

    /**
     * Test findByUsername method.
     */
    @Test
    void testFindByUsername() {
        // Arrange
        CoordinatorUnit coordinator = new CoordinatorUnit();
        coordinator.setName("Jane Doe");
        coordinator.setUsername("janedoe");
        repository.save(coordinator);

        // Act
        Optional<CoordinatorUnit> result = repository.findByUsername("janedoe");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Jane Doe", result.get().getName());
        assertEquals("janedoe", result.get().getUsername());
    }
}
