package com.upt.upt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.upt.upt.entity.MasterUnit;
import com.upt.upt.entity.DirectorUnit;
import com.upt.upt.entity.CoordinatorUnit;
import com.upt.upt.repository.MasterUnitRepository;
import com.upt.upt.repository.DirectorUnitRepository;
import com.upt.upt.repository.CoordinatorUnitRepository;
import com.upt.upt.entity.UserType;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private MasterUnitRepository masterUnitRepository;

    @Autowired
    private DirectorUnitRepository directorUnitRepository;

    @Autowired
    private CoordinatorUnitRepository coordinatorUnitRepository;

    public UserType validateUser(String username, String password) {

        MasterUnit master = masterUnitRepository.findByUsernameAndPassword(username, password);
        if (master != null) {
            return UserType.MASTER;
        }

        DirectorUnit director = directorUnitRepository.findByUsernameAndPassword(username, password);
        if (director != null) {
            return UserType.DIRECTOR;
        }

        CoordinatorUnit coordinator = coordinatorUnitRepository.findByUsernameAndPassword(username, password);
        if (coordinator != null) {
            return UserType.COORDINATOR;
        }

        return null;
    }

    public Long getUserIdByUsername(String username, UserType userType) {
        switch (userType) {
            case MASTER:
                return masterUnitRepository.findByUsername(username)
                        .map(MasterUnit::getId)
                        .orElse(null);
            case DIRECTOR:
                return directorUnitRepository.findByUsername(username)
                        .map(DirectorUnit::getId)
                        .orElse(null);
            case COORDINATOR:
                return coordinatorUnitRepository.findByUsername(username)
                        .map(CoordinatorUnit::getId)
                        .orElse(null);
            default:
                return null;
        }
    }

    public Optional<DirectorUnit> getDirectorById(Long id) {
        return directorUnitRepository.findById(id);
    }

    public boolean usernameExists(String username) {
        return masterUnitRepository.findByUsername(username).isPresent() ||
               directorUnitRepository.findByUsername(username).isPresent() ||
               coordinatorUnitRepository.findByUsername(username).isPresent();
    }
}