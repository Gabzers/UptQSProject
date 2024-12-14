package com.upt.upt.repository;

import com.upt.upt.entity.CoordinatorUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class CoordinatorUnitRepositoryTest {

    @Autowired
    private CoordinatorUnitRepository repository;

    @Test
    void testFindByUsernameAndPassword() {
        // Preparação: salvar uma entidade no banco de dados
        CoordinatorUnit coordinator = new CoordinatorUnit();
        coordinator.setName("John Doe");
        coordinator.setUsername("jdoe");
        coordinator.setPassword("password123");
        repository.save(coordinator);

        // Execução: buscar a entidade pelo username e password
        CoordinatorUnit result = repository.findByUsernameAndPassword("jdoe", "password123");

        // Verificação
        assertEquals("John Doe", result.getName());
        assertEquals("jdoe", result.getUsername());
    }

    @Test
    void testFindByUsername() {
        // Preparação: salvar uma entidade no banco de dados
        CoordinatorUnit coordinator = new CoordinatorUnit();
        coordinator.setName("Jane Doe");
        coordinator.setUsername("janedoe");
        repository.save(coordinator);

        // Execução: buscar a entidade pelo username
        Optional<CoordinatorUnit> result = repository.findByUsername("janedoe");

        // Verificação
        assertTrue(result.isPresent());
        assertEquals("Jane Doe", result.get().getName());
        assertEquals("janedoe", result.get().getUsername());
    }
}
