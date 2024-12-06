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
                MasterUnit master = masterUnitRepository.findByUsername(username);
                Long masterId = master != null ? master.getId() : null;
                return masterId;
            case DIRECTOR:
                DirectorUnit director = directorUnitRepository.findByUsername(username);
                Long directorId = director != null ? director.getId() : null;
                return directorId;
            case COORDINATOR:
                CoordinatorUnit coordinator = coordinatorUnitRepository.findByUsername(username);
                Long coordinatorId = coordinator != null ? coordinator.getId() : null;
                return coordinatorId;
            default:
                return null;
        }
    }

}