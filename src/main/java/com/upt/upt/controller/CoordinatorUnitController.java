package com.upt.upt.controller;

import com.upt.upt.entity.CoordinatorUnit;
import com.upt.upt.entity.CurricularUnit;
import com.upt.upt.entity.DirectorUnit;
import com.upt.upt.entity.UserType;
import com.upt.upt.service.CoordinatorUnitService;
import com.upt.upt.service.DirectorUnitService;
import com.upt.upt.service.UserService;
import com.upt.upt.service.CurricularUnitService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
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
    @Autowired
    private UserService userService;

    @Autowired
    private CurricularUnitService curricularUnitService;

    private Optional<DirectorUnit> verifyDirector(HttpSession session) {
        Long directorId = (Long) session.getAttribute("userId");
        if (directorId == null) {
            return Optional.empty();
        }
        return directorUnitService.getDirectorById(directorId);
    }

    private boolean isDirector(HttpSession session) {
        UserType userType = (UserType) session.getAttribute("userType");
        return userType == UserType.DIRECTOR;
    }

    @GetMapping("/create-coordinator")
    public String createCoordinatorForm(HttpSession session, Model model) {
        if (!isDirector(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        if (verifyDirector(session).isEmpty()) {
            return "redirect:/login?error=Unauthorized access";
        }
        model.addAttribute("coordinator", new CoordinatorUnit());
        return "director_addCoordinator"; // Name of the view template for the form
    }

    @PostMapping("/create-coordinator")
    public String saveCoordinator(@ModelAttribute CoordinatorUnit coordinator, HttpSession session) {
        if (!isDirector(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        if (verifyDirector(session).isEmpty()) {
            return "redirect:/login?error=Unauthorized access";
        }
        if (coordinator.getDuration() > 10) {
            return "redirect:/director/create-coordinator?error=Course duration cannot exceed 10 years";
        }
        Long directorId = (Long) session.getAttribute("userId");
        if (!coordinatorService.hasYearCreated(directorId)) {
            return "redirect:/director/create-coordinator?yearError=No year created. Please create a year first.";
        }
        try {
            if (userService.usernameExists(coordinator.getUsername())) {
                throw new IllegalArgumentException("Username already exists");
            }
            coordinatorService.saveCoordinatorWithDirector(coordinator, session);
            return "redirect:/director";
        } catch (IllegalArgumentException e) {
            return "redirect:/director/create-coordinator?error=" + e.getMessage();
        }
    }

    @GetMapping("/edit-coordinator/{id}")
    public String editCoordinatorForm(@PathVariable("id") Long id, HttpSession session, Model model) {
        if (!isDirector(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        if (verifyDirector(session).isEmpty()) {
            return "redirect:/login?error=Unauthorized access";
        }
        Optional<CoordinatorUnit> coordinator = coordinatorService.getCoordinatorById(id);
        if (coordinator.isPresent()) {
            model.addAttribute("coordinator", coordinator.get());
            return "director_editCoordinator"; // Name of the view template for the form
        } else {
            return "redirect:/director";
        }
    }

    @PostMapping("/edit-coordinator/{id}")
    public String updateCoordinator(@PathVariable("id") Long id, @ModelAttribute CoordinatorUnit coordinator, HttpSession session) {
        if (!isDirector(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        if (verifyDirector(session).isEmpty()) {
            return "redirect:/login?error=Unauthorized access";
        }
        if (coordinator.getDuration() > 10) {
            return "redirect:/director/edit-coordinator/" + id + "?error=Course duration cannot exceed 10 years";
        }
        try {
            if (userService.usernameExists(coordinator.getUsername())) {
                throw new IllegalArgumentException("Username already exists");
            }
            coordinatorService.updateCoordinator(id, coordinator);
            return "redirect:/director";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/edit-coordinator/" + id + "?error=true";
        }
    }

    @PostMapping("/delete-coordinator/{id}")
    public String deleteCoordinator(@PathVariable("id") Long id, Model model, HttpSession session) {
        if (!isDirector(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        model.addAttribute("warning", "Are you sure you want to remove this coordinator?");
        model.addAttribute("coordinatorId", id);
        return "director_confirmRemoveCoordinator";
    }

    @PostMapping("/remove-coordinator/{id}")
    public String removeCoordinator(@PathVariable("id") Long id, Model model, HttpSession session) {
        if (!isDirector(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        model.addAttribute("warning", "Are you sure you want to remove this coordinator?");
        model.addAttribute("coordinatorId", id);
        return "director_confirmRemoveCoordinator";
    }

    @PostMapping("/confirm-remove-coordinator/{id}")
    public String confirmRemoveCoordinator(@PathVariable("id") Long id, HttpSession session) {
        if (!isDirector(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        coordinatorService.deleteCoordinator(id);
        return "redirect:/director";
    }
}
