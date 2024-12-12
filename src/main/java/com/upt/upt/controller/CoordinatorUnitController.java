package com.upt.upt.controller;

import com.upt.upt.entity.CoordinatorUnit;
import com.upt.upt.entity.DirectorUnit;
import com.upt.upt.service.CoordinatorUnitService;
import com.upt.upt.service.DirectorUnitService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller class for managing CoordinatorUnit entities.
 */
@Controller
@RequestMapping("/director")
public class CoordinatorUnitController {

    private static final Logger logger = LoggerFactory.getLogger(CoordinatorUnitController.class);

    @Autowired
    private CoordinatorUnitService coordinatorService;

    @Autowired
    private DirectorUnitService directorUnitService;

    @GetMapping("/create-coordinator")
    public String createCoordinatorForm(Model model) {
        model.addAttribute("coordinator", new CoordinatorUnit());
        return "director_addCoordinator"; // Name of the view template for the form
    }

    @PostMapping("/create-coordinator")
    public String saveCoordinator(@ModelAttribute CoordinatorUnit coordinator, HttpSession session) {
        Long directorId = (Long) session.getAttribute("userId");
        if (directorId != null) {
            Optional<DirectorUnit> directorOpt = directorUnitService.getDirectorById(directorId);
            if (directorOpt.isPresent()) {
                DirectorUnit director = directorOpt.get();
                director.addCoordinator(coordinator);
                coordinatorService.saveCoordinator(coordinator);
            } else {
                return "redirect:/login?error=Director not found";
            }
        } else {
            return "redirect:/login?error=Session expired";
        }
        return "redirect:/director";
    }

    @GetMapping("/edit-coordinator/{id}")
    public String editCoordinatorForm(@PathVariable("id") Long id, Model model) {
        Optional<CoordinatorUnit> coordinator = coordinatorService.getCoordinatorById(id);
        if (coordinator.isPresent()) {
            model.addAttribute("coordinator", coordinator.get());
            return "director_editCoordinator"; // Name of the view template for the form
        } else {
            return "redirect:/director";
        }
    }

    @PostMapping("/edit-coordinator/{id}")
    public String updateCoordinator(@PathVariable("id") Long id, @ModelAttribute CoordinatorUnit coordinator) {
        Optional<CoordinatorUnit> existingCoordinator = coordinatorService.getCoordinatorById(id);
        if (existingCoordinator.isPresent()) {
            CoordinatorUnit updatedCoordinator = existingCoordinator.get();
            updatedCoordinator.setName(coordinator.getName());
            updatedCoordinator.setCourse(coordinator.getCourse());
            updatedCoordinator.setDuration(coordinator.getDuration());
            updatedCoordinator.setUsername(coordinator.getUsername());
            if (coordinator.getPassword() != null && !coordinator.getPassword().isEmpty()) {
                updatedCoordinator.setPassword(coordinator.getPassword());
            }
            coordinatorService.saveCoordinator(updatedCoordinator);
        }
        return "redirect:/director";
    }

    @PostMapping("/delete-coordinator/{id}")
    public String deleteCoordinator(@PathVariable("id") Long id) {
        coordinatorService.deleteCoordinator(id);
        return "redirect:/director";
    }
}
