package com.upt.upt.controller;

import com.upt.upt.entity.DirectorUnit;
import com.upt.upt.service.DirectorUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller for managing DirectorUnit entities.
 */
@Controller
@RequestMapping("/director")
public class DirectorUnitController {

    @Autowired
    private DirectorUnitService directorUnitService;

    /**
     * Displays a list of all DirectorUnit entities.
     *
     * @param model The model to pass data to the view
     * @return The name of the view to display the master
     */
    @GetMapping
    public String listDirectors(Model model) {
        List<DirectorUnit> directorUnits = directorUnitService.getAllDirectors();
        model.addAttribute("directorUnits", directorUnits);
        return "director_index"; // Name of the view template
    }

    /**
     * Displays the form for creating a new DirectorUnit.
     *
     * @return The name of the view to create a new director
     */
    @GetMapping("/create-director")
    public String showCreateDirectorForm() {
        return "master_addDirector"; // The page for adding a director
    }

    /**
     * Creates a new DirectorUnit from the provided form data.
     *
     * @param directorName     The name of the director
     * @param directorDepartment The department of the director
     * @param directorUsername The username for the director
     * @param directorPassword The password for the director
     * @return A redirect to the list of master or an error page
     */
    @PostMapping("/create-director")
    public String createDirector(@RequestParam("director-name") String directorName,
            @RequestParam("director-department") String directorDepartment,
            @RequestParam("director-username") String directorUsername,
            @RequestParam("director-password") String directorPassword) {
        try {
            DirectorUnit newDirector = new DirectorUnit();
            newDirector.setName(directorName);
            newDirector.setDepartment(directorDepartment);
            newDirector.setUsername(directorUsername);
            newDirector.setPassword(directorPassword);

            directorUnitService.saveDirector(newDirector);
            return "redirect:/master"; // Redirect to the list of master
        } catch (IllegalArgumentException e) {
            return "redirect:/master/create?error=Incomplete information";
        } catch (RuntimeException e) {
            return "redirect:/master/create?error=Duplicate entry or integrity constraint violated";
        }
    }

    /**
     * Displays the form for editing an existing DirectorUnit.
     *
     * @param id    The ID of the director to be edited
     * @param model The model to pass data to the view
     * @return The name of the view to edit the director
     */
    @GetMapping("/edit-director")
public String showEditDirectorForm(@RequestParam("id") Long id, Model model) {
    Optional<DirectorUnit> directorOptional = directorUnitService.getDirectorById(id);
    if (directorOptional.isPresent()) {
        model.addAttribute("director", directorOptional.get());
        return "master_editDirector"; // The page for editing a director
    }
    return "redirect:/master?error=Director not found"; // Redirect if the director is not found
}


    /**
     * Deletes a DirectorUnit.
     *
     * @param id The ID of the director to be deleted
     * @return A redirect to the list of master
     */
    @PostMapping("/remove-director/{id}")
    public String removeDirector(@PathVariable("id") Long id) {
        directorUnitService.deleteDirector(id); // Remove the director from the database
        return "redirect:/master"; // Redirect to the list of directors
    }

    /**
     * Displays the user edit page.
     *
     * @return The name of the view for user edit
     */
    @GetMapping("/director-editUser")
    public String editUser() {
        return "director_editUser"; // Redirect to the user edit page
    }

    /**
     * Displays the new year page.
     *
     * @return The name of the view for the new year page
     */
    @GetMapping("/newYear")
    public String newYear() {
        return "director_newYear"; // Redirect to the new year page
    }

    /**
     * Displays the edit year page.
     *
     * @return The name of the view for the edit year page
     */
    @GetMapping("/editYear")
    public String editYear() {
        return "director_editYear"; // Redirect to the edit year page
    }

    
    
}
