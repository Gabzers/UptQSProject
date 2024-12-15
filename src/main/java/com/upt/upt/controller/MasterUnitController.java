package com.upt.upt.controller;

import com.upt.upt.entity.MasterUnit;
import com.upt.upt.entity.RoomUnit;
import com.upt.upt.entity.UserType;
import com.upt.upt.service.DirectorUnitService;
import com.upt.upt.service.MasterUnitService;
import com.upt.upt.service.RoomUnitService;
import com.upt.upt.service.UserService;

import java.util.Optional;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

/**
 * Controller class for managing MasterUnit entities.
 * 
 * @autor grupo 5 - 47719, 47713, 46697, 47752, 47004
 */
@Controller
@RequestMapping("/master")
public class MasterUnitController {

    @Autowired
    private DirectorUnitService directorUnitService;

    @Autowired
    private MasterUnitService masterUnitService;
    
    @Autowired
    private RoomUnitService roomUnitService;

    @Autowired
    private UserService userService;

    private boolean isMaster(HttpSession session) {
        UserType userType = (UserType) session.getAttribute("userType");
        return userType == UserType.MASTER;
    }

    /**
     * Lists all directors, masters, and rooms.
     * 
     * @param model the model to add attributes to
     * @param session the HTTP session
     * @return the view name
     */
    @GetMapping
    public String listDirectorsMastersAndRooms(Model model, HttpSession session) {
        if (!isMaster(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        model.addAttribute("directorUnits", directorUnitService.getAllDirectors());
        model.addAttribute("masterUnits", masterUnitService.getAllMasters());
        model.addAttribute("roomUnits", roomUnitService.getAllRooms());
        return "master_index";
    }

    /**
     * Shows the form to create a new master.
     * 
     * @param session the HTTP session
     * @return the view name
     */
    @GetMapping("/create-master")
    public String showCreateMasterForm(HttpSession session) {
        if (!isMaster(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        return "master_addMaster";
    }

    /**
     * Creates a new master.
     * 
     * @param name the name of the master
     * @param username the username of the master
     * @param password the password of the master
     * @param session the HTTP session
     * @return the view name
     */
    @PostMapping("/create-master")
    public String createMaster(@RequestParam("master-name") String name, @RequestParam("master-username") String username, @RequestParam("master-password") String password, HttpSession session) {
        if (!isMaster(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        try {
            if (userService.usernameExists(username)) {
                throw new IllegalArgumentException("Username already exists");
            }
            MasterUnit newMaster = masterUnitService.createMaster(name, username, password);
            masterUnitService.saveMaster(newMaster);
            return "redirect:/master";
        } catch (IllegalArgumentException e) {
            return "redirect:/master/create-master?error=" + e.getMessage();
        } catch (RuntimeException e) {
            return "redirect:/master/create-master?error=Duplicate entry or integrity constraint violated";
        }
    }

    /**
     * Shows the confirmation page to remove a master.
     * 
     * @param id the ID of the master to remove
     * @param model the model to add attributes to
     * @param session the HTTP session
     * @return the view name
     */
    @PostMapping("/remove-master/{id}")
    public String removeMaster(@PathVariable("id") Long id, Model model, HttpSession session) {
        if (!isMaster(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        model.addAttribute("warning", "Are you sure you want to remove this master?");
        model.addAttribute("masterId", id);
        return "master_confirmRemoveMaster";
    }

    /**
     * Confirms the removal of a master.
     * 
     * @param id the ID of the master to remove
     * @param session the HTTP session
     * @return the view name
     */
    @PostMapping("/confirm-remove-master/{id}")
    public String confirmRemoveMaster(@PathVariable("id") Long id, HttpSession session) {
        if (!isMaster(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        masterUnitService.deleteMaster(id);
        return "redirect:/master";
    }

    /**
     * Shows the form to edit an existing master.
     * 
     * @param id the ID of the master to edit
     * @param model the model to add attributes to
     * @param session the HTTP session
     * @return the view name
     */
    @GetMapping("/edit-master")
    public String showEditMasterForm(@RequestParam("id") Long id, Model model, HttpSession session) {
        if (!isMaster(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        Optional<MasterUnit> masterOptional = masterUnitService.getMasterById(id);
        if (masterOptional.isPresent()) {
            model.addAttribute("master", masterOptional.get());
            return "master_editMaster";
        }
        return "redirect:/master?error=Master not found";
    }

    /**
     * Updates an existing master.
     * 
     * @param id the ID of the master to update
     * @param name the name of the master
     * @param username the username of the master
     * @param password the password of the master
     * @param session the HTTP session
     * @return the view name
     */
    @PostMapping("/edit-master/{id}")
    public String updateMaster(@PathVariable("id") Long id, @RequestParam("master-name") String name, @RequestParam("master-username") String username, @RequestParam(value = "master-password", required = false) String password, HttpSession session) {
        if (!isMaster(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        try {
            if (userService.usernameExists(username)) {
                throw new IllegalArgumentException("Username already exists");
            }
            MasterUnit master = masterUnitService.updateMaster(id, name, username, password);
            masterUnitService.saveMaster(master);
            return "redirect:/master";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/edit-master/" + id + "?error=true";
        }
    }

    /**
     * Creates a new room.
     * 
     * @param params the request parameters
     * @param model the model to add attributes to
     * @param session the HTTP session
     * @return the view name
     */
    @PostMapping("/create-room")
    public String createRoom(@RequestParam Map<String, String> params, Model model, HttpSession session) {
        if (!isMaster(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        try {
            String roomNumber = params.get("room-number");
            if (roomUnitService.roomNumberExists(roomNumber)) {
                model.addAttribute("error", "Room number already exists.");
                return "master_addRoom";
            }
            RoomUnit newRoom = roomUnitService.createRoom(params);
            roomUnitService.saveRoom(newRoom);
            return "redirect:/master";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Incomplete information");
            return "master_addRoom";
        } catch (RuntimeException e) {
            model.addAttribute("error", "Duplicate entry or integrity constraint violated");
            return "master_addRoom";
        }
    }

    /**
     * Edits an existing room.
     * 
     * @param params the request parameters
     * @param model the model to add attributes to
     * @param session the HTTP session
     * @return the view name
     */
    @PostMapping("/edit-room")
    public String editRoom(@RequestParam Map<String, String> params, Model model, HttpSession session) {
        if (!isMaster(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        try {
            String roomNumber = params.get("room-number");
            Long roomId = Long.parseLong(params.get("room-id"));
            if (roomUnitService.roomNumberExists(roomNumber, roomId)) {
                model.addAttribute("error", "Room number already exists.");
                return "master_editRoom";
            }
            RoomUnit updatedRoom = roomUnitService.updateRoom(params);
            roomUnitService.saveRoom(updatedRoom);
            return "redirect:/master";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Incomplete information");
            return "master_editRoom";
        } catch (RuntimeException e) {
            model.addAttribute("error", "Duplicate entry or integrity constraint violated");
            return "master_editRoom";
        }
    }

    /**
     * Shows the confirmation page to remove a director.
     * 
     * @param id the ID of the director to remove
     * @param model the model to add attributes to
     * @param session the HTTP session
     * @return the view name
     */
    @PostMapping("/remove-director/{id}")
    public String removeDirector(@PathVariable("id") Long id, Model model, HttpSession session) {
        if (!isMaster(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        model.addAttribute("warning", "Are you sure you want to remove this director?");
        model.addAttribute("directorId", id);
        return "master_confirmRemoveDirector";
    }

    /**
     * Confirms the removal of a director.
     * 
     * @param id the ID of the director to remove
     * @param session the HTTP session
     * @return the view name
     */
    @PostMapping("/confirm-remove-director/{id}")
    public String confirmRemoveDirector(@PathVariable("id") Long id, HttpSession session) {
        if (!isMaster(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        directorUnitService.deleteDirector(id);
        return "redirect:/master";
    }
}
