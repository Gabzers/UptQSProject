package com.upt.upt.controller;

import com.upt.upt.entity.CoordinatorUnit;
import com.upt.upt.service.CoordinatorUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Controller class for managing CoordinatorUnit entities.
 */
@Controller
@RequestMapping("/director")
public class CoordinatorUnitController {

    @Autowired
    private CoordinatorUnitService coordinatorService;

    /**
     * Displays a form for creating a new CoordinatorUnit.
     *
     * @param model The model to pass data to the view
     * @return The name of the view for the create form
     */
    @GetMapping("/create-coordinator")
    public String createCoordinatorForm(Model model) {
        model.addAttribute("coordinator", new CoordinatorUnit());
        return "director_addCoordinator"; // Name of the view template for the form
    }

    /**
     * Handles the submission of the form to create a new CoordinatorUnit.
     *
     * @param coordinator The CoordinatorUnit to save
     * @return Redirect to the list of coordinators
     */
    @PostMapping("/create-coordinator")
    public String saveCoordinator(@ModelAttribute CoordinatorUnit coordinator) {
        coordinatorService.saveCoordinator(coordinator);
        return "redirect:/director";
    }

    /**
     * Displays a form for editing an existing CoordinatorUnit.
     *
     * @param id    The ID of the CoordinatorUnit to edit
     * @param model The model to pass data to the view
     * @return The name of the view for the edit form
     */
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

    /**
     * Handles the submission of the form to update an existing CoordinatorUnit.
     *
     * @param id          The ID of the CoordinatorUnit to update
     * @param coordinator The updated CoordinatorUnit
     * @return Redirect to the list of coordinators
     */
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

    /**
     * Handles the deletion of a CoordinatorUnit by its ID.
     *
     * @param id The ID of the CoordinatorUnit to delete
     * @return Redirect to the list of coordinators
     */
    @PostMapping("/delete-coordinator/{id}")
    public String deleteCoordinator(@PathVariable("id") Long id) {
        coordinatorService.deleteCoordinator(id);
        return "redirect:/director";
    }
}
