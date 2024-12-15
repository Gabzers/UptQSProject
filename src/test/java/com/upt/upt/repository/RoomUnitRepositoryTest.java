package com.upt.upt.repository;

import com.upt.upt.entity.RoomUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for RoomUnitRepository.
 * 
 * @autor Grupo 5 - 47719, 47713, 46697, 47752, 47004
 */
@DataJpaTest
public class RoomUnitRepositoryTest {

    @Autowired
    private RoomUnitRepository roomUnitRepository;

    private RoomUnit room1;
    private RoomUnit room2;

    /**
     * Set up test data.
     */
    @BeforeEach
    public void setUp() {
        room1 = new RoomUnit();
        room1.setDesignation("A101");
        room1.setBuilding("Main Building");
        room1.setMaterialType("Wood");
        room1.setRoomNumber("101");

        room2 = new RoomUnit();
        room2.setDesignation("B202");
        room2.setBuilding("Secondary Building");
        room2.setMaterialType("Concrete");
        room2.setRoomNumber("202");

        roomUnitRepository.save(room1);
        roomUnitRepository.save(room2);
    }

    /**
     * Test findByDesignation method.
     */
    @Test
    public void testFindByDesignation() {
        List<RoomUnit> found = roomUnitRepository.findByDesignation("A101");
        assertThat(found).hasSize(1);
        assertThat(found.get(0)).isEqualTo(room1);
    }

    /**
     * Test findByBuilding method.
     */
    @Test
    public void testFindByBuilding() {
        List<RoomUnit> found = roomUnitRepository.findByBuilding("Main Building");
        assertThat(found).hasSize(1);
        assertThat(found.get(0)).isEqualTo(room1);
    }

    /**
     * Test findByMaterialType method.
     */
    @Test
    public void testFindByMaterialType() {
        List<RoomUnit> found = roomUnitRepository.findByMaterialType("Concrete");
        assertThat(found).hasSize(1);
        assertThat(found.get(0)).isEqualTo(room2);
    }

    /**
     * Test findAllById method.
     */
    @Test
    public void testFindAllById() {
        List<RoomUnit> found = roomUnitRepository.findAllById(List.of(room1.getId(), room2.getId()));
        assertThat(found).hasSize(2);
        assertThat(found).containsExactlyInAnyOrder(room1, room2);
    }

    /**
     * Test findByRoomNumber method.
     */
    @Test
    public void testFindByRoomNumber() {
        Optional<RoomUnit> found = roomUnitRepository.findByRoomNumber("101");
        assertThat(found).isPresent();
        assertThat(found.get()).isEqualTo(room1);
    }
}
