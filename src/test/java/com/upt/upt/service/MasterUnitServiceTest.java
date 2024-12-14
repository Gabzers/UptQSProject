package com.upt.upt.service;

import com.upt.upt.entity.MasterUnit;
import com.upt.upt.repository.MasterUnitRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Optional;

@SpringBootTest
@Transactional
class MasterUnitServiceTest {

    @Autowired
    private MasterUnitService masterUnitService;

    @Autowired
    private MasterUnitRepository masterUnitRepository;

    @Test
    void testSaveMaster() {
        MasterUnit master = new MasterUnit();
        master.setName("Test Master");
        master.setUsername("testmaster");
        master.setPassword("password123");

        masterUnitService.saveMaster(master);

        Optional<MasterUnit> retrieved = masterUnitRepository.findByUsername("testmaster");
        assertTrue(retrieved.isPresent());
        assertEquals("Test Master", retrieved.get().getName());
    }

    @Test
    void testGetMasterById() {
        MasterUnit master = new MasterUnit(null, "Test Master", "testmaster", "password123");
        masterUnitRepository.save(master);

        Optional<MasterUnit> retrieved = masterUnitService.getMasterById(master.getId());
        assertTrue(retrieved.isPresent());
        assertEquals("testmaster", retrieved.get().getUsername());
    }

    @Test
    void testUsernameExists() {
        MasterUnit master = new MasterUnit(null, "Test Master", "uniqueusername", "password123");
        masterUnitRepository.save(master);

        assertTrue(masterUnitService.usernameExists("uniqueusername"));
        assertFalse(masterUnitService.usernameExists("nonexistentuser"));
    }

    @Test
    void testDeleteMaster() {
        MasterUnit master = new MasterUnit(null, "ToDelete", "deleteusername", "password123");
        masterUnitRepository.save(master);

        masterUnitService.deleteMaster(master.getId());

        Optional<MasterUnit> deletedMaster = masterUnitRepository.findById(master.getId());
        assertTrue(deletedMaster.isEmpty());
    }
}
