package com.upt.upt.controller;

import com.upt.upt.entity.DirectorUnit;
import com.upt.upt.entity.MasterUnit;
import com.upt.upt.entity.RoomUnit;
import com.upt.upt.service.DirectorUnitService;
import com.upt.upt.service.MasterUnitService;
import com.upt.upt.service.RoomUnitService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/master")
public class MasterUnitController {

    @Autowired
    private DirectorUnitService directorUnitService;

    @Autowired
    private MasterUnitService masterUnitService;
    
    @Autowired
    private RoomUnitService roomUnitService;

    /**
     * Displays a list of all DirectorUnit entities, MasterUnit entities, and RoomUnit entities.
     *
     * @param model The model to pass data to the view
     * @return The name of the view to display the master
     */
    @GetMapping
    public String listDirectorsMastersAndRooms(Model model) {
        // Obter todos os diretores
        List<DirectorUnit> directorUnits = directorUnitService.getAllDirectors();
        model.addAttribute("directorUnits", directorUnits);

        // Obter todos os masters
        List<MasterUnit> masterUnits = masterUnitService.getAllMasters();
        model.addAttribute("masterUnits", masterUnits);

        // Obter todas as salas
        List<RoomUnit> roomUnits = roomUnitService.getAllRooms();
        model.addAttribute("roomUnits", roomUnits);

        return "master_index"; // Nome do template
    }

    /**
     * Displays a list of all MasterUnit entities.
     *
     * @param model The model to pass data to the view
     * @return The name of the view to display the master units
     */
    // @GetMapping
    // public String listMasters(Model model) {
    // List<MasterUnit> masterUnits = masterUnitService.getAllMasters();
    // model.addAttribute("masterUnits", masterUnits);
    // return "master_index"; // View template for listing masters
    // }

    /**
     * Displays the form for creating a new MasterUnit.
     *
     * @return The name of the view to create a new master
     */
    @GetMapping("/create-master")
    public String showCreateMasterForm() {
        return "master_addMaster"; // The page for adding a master
    }

    /**
     * Creates a new MasterUnit from the provided form data.
     *
     * @param name     The name of the master unit
     * @param username The username for the master unit
     * @param password The password for the master unit
     * @return A redirect to the list of master units or an error page
     */
    @PostMapping("/create-master")
    public String createMaster(
            @RequestParam("master-name") String name,
            @RequestParam("master-username") String username,
            @RequestParam("master-password") String password) {
        try {
            MasterUnit newMaster = new MasterUnit();
            newMaster.setName(name);
            newMaster.setUsername(username);
            newMaster.setPassword(password);

            masterUnitService.saveMaster(newMaster);
            return "redirect:/master"; // Redirect to the list of masters
        } catch (IllegalArgumentException e) {
            return "redirect:/master/create-master?error=Incomplete information";
        } catch (RuntimeException e) {
            return "redirect:/master/create-master?error=Duplicate entry or integrity constraint violated";
        }
    }

    /**
     * Deletes a MasterUnit.
     *
     * @param id The ID of the master unit to be deleted
     * @return A redirect to the list of master units
     */
    @PostMapping("/remove-master/{id}")
    public String removeMaster(@PathVariable("id") Long id) {
        masterUnitService.deleteMaster(id);
        return "redirect:/master"; // Redirect to the list of masters
    }

    /**
     * Displays the form for editing an existing MasterUnit.
     *
     * @param id    The ID of the master to be edited
     * @param model The model to pass data to the view
     * @return The name of the view to edit the master
     */
    @GetMapping("/edit-master")
    public String showEditMasterForm(@RequestParam("id") Long id, Model model) {
        Optional<MasterUnit> masterOptional = masterUnitService.getMasterById(id);
        if (masterOptional.isPresent()) {
            model.addAttribute("master", masterOptional.get());
            return "master_editMaster"; // The page for editing a master
        }
        return "redirect:/master?error=Master not found"; // Redirect if the master is not found
    }

    /**
     * Updates an existing MasterUnit.
     *
     * @param id       The ID of the master unit to be updated
     * @param name     The updated name of the master unit
     * @param username The updated username of the master unit
     * @param password The updated password of the master unit
     * @return A redirect to the list of master units
     */
    @PostMapping("/edit-master/{id}")
    public String updateMaster(
            @PathVariable("id") Long id,
            @RequestParam("master-name") String name,
            @RequestParam("master-username") String username,
            @RequestParam(value = "master-password", required = false) String password) {
        try {
            MasterUnit master = masterUnitService.getMasterById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Master ID: " + id));

            master.setName(name);
            master.setUsername(username);
            master.setPassword(password != null && !password.isEmpty() ? password : master.getPassword());

            masterUnitService.saveMaster(master);
            return "redirect:/master";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/edit-master/" + id + "?error=true";
        }
    }

}
