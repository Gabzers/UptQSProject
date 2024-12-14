package com.upt.upt.repository;

import com.upt.upt.entity.MasterUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Optional;


@SpringBootTest
@Transactional
class MasterUnitRepositoryTest {

    @Autowired
    private MasterUnitRepository masterUnitRepository;

    @Test
    void testFindByUsernameAndPassword() {
        MasterUnit master = new MasterUnit(null, "RepoTest", "repoUser", "repoPass");
        masterUnitRepository.save(master);

        MasterUnit retrieved = masterUnitRepository.findByUsernameAndPassword("repoUser", "repoPass");
        assertNotNull(retrieved);
        assertEquals("RepoTest", retrieved.getName());
    }

    @Test
    void testFindByUsername() {
        MasterUnit master = new MasterUnit(null, "RepoTest", "uniqueUser", "password");
        masterUnitRepository.save(master);

        Optional<MasterUnit> retrieved = masterUnitRepository.findByUsername("uniqueUser");
        assertTrue(retrieved.isPresent());
        assertEquals("RepoTest", retrieved.get().getName());
    }
}
