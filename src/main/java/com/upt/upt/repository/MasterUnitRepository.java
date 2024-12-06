package com.upt.upt.repository;

import com.upt.upt.entity.MasterUnit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MasterUnitRepository extends JpaRepository<MasterUnit, Long> {
    MasterUnit findByUsernameAndPassword(String username, String password);
    MasterUnit findByUsername(String username); // Adicione este método
}
