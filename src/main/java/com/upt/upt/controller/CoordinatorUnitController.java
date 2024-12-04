package com.upt.upt.controller;

import com.upt.upt.entity.CoordinatorUnit;
import com.upt.upt.service.CoordinatorUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller class for managing CoordinatorUnit entities.
 */
@Controller
@RequestMapping("/coordinators")
public class CoordinatorUnitController {

    @Autowired
    private CoordinatorUnitService coordinatorService;

    /**
     * Displays a list of all CoordinatorUnit entities.
     *
     * @param model The model to pass data to the view
     * @return The name of the view to display the coordinators
     */
    @GetMapping
    public String listCoordinators(Model model) {
        List<CoordinatorUnit> coordinators = coordinatorService.getAllCoordinators();
        model.addAttribute("coordinators", coordinators);
        return "coordinator_list"; // Name of the view template
    }

    /**
     * Displays a form for creating a new CoordinatorUnit.
     *
     * @param model The model to pass data to the view
     * @return The name of the view for the create form
     */
    @GetMapping("/create")
    public String createCoordinatorForm(Model model) {
        model.addAttribute("coordinator", new CoordinatorUnit());
        return "coordinator_form"; // Name of the view template for the form
    }

    /**
     * Handles the submission of the form to create a new CoordinatorUnit.
     *
     * @param coordinator The CoordinatorUnit to save
     * @return Redirect to the list of coordinators
     */
    @PostMapping("/save")
    public String saveCoordinator(@ModelAttribute CoordinatorUnit coordinator) {
        coordinatorService.saveCoordinator(coordinator);
        return "redirect:/coordinators";
    }

    /**
     * Displays a form for editing an existing CoordinatorUnit.
     *
     * @param id    The ID of the CoordinatorUnit to edit
     * @param model The model to pass data to the view
     * @return The name of the view for the edit form
     */
    @GetMapping("/edit/{id}")
    public String editCoordinatorForm(@PathVariable Long id, Model model) {
        Optional<CoordinatorUnit> coordinator = coordinatorService.getCoordinatorById(id);
        if (coordinator.isPresent()) {
            model.addAttribute("coordinator", coordinator.get());
            return "coordinator_form"; // Name of the view template for the form
        } else {
            return "redirect:/coordinators";
        }
    }

    /**
     * Handles the deletion of a CoordinatorUnit by its ID.
     *
     * @param id The ID of the CoordinatorUnit to delete
     * @return Redirect to the list of coordinators
     */
    @GetMapping("/delete/{id}")
    public String deleteCoordinator(@PathVariable Long id) {
        coordinatorService.deleteCoordinator(id);
        return "redirect:/coordinators";
    }
}
