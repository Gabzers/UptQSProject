package com.upt.upt.controller;

import com.upt.upt.entity.RoomUnit;
import com.upt.upt.service.RoomUnitService;
import java.util.List;  // Correção da importação

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling requests related to RoomUnit entities.
 */
@Controller
@RequestMapping("/master")
public class RoomUnitController {

    @Autowired
    private RoomUnitService roomUnitService;

    /**
     * Displays a list of all RoomUnit entities.
     *
     * @param model The model to pass data to the view
     * @return The name of the view to display the room units
     */
    @GetMapping("/rooms")
    public String listRooms(Model model) {
        // Get all rooms
        List<RoomUnit> roomUnits = roomUnitService.getAllRooms();
        model.addAttribute("roomUnits", roomUnits);

        return "master_index"; // Redirect to master_index page with room data
    }

    /**
     * Displays the form for creating a new RoomUnit.
     *
     * @return The name of the view to create a new room
     */
    @GetMapping("/create-room")
    public String showCreateRoomForm() {
        return "master_addRoom"; // The page for adding a room
    }

    /**
     * Creates a new RoomUnit from the provided form data.
     *
     * @param roomNumber   The room number
     * @param designation  The room designation
     * @param materialType The type of material in the room
     * @param seatsCount   The number of seats in the room
     * @param building     The building where the room is located
     * @param model        The model to pass error messages to the view
     * @return A redirect to the list of rooms or an error page
     */
    @PostMapping("/create-room")
    public String createRoom(
            @RequestParam("room-number") String roomNumber,
            @RequestParam("room-designation") String designation,
            @RequestParam("room-material-type") String materialType,
            @RequestParam("room-seats-count") Integer seatsCount,
            @RequestParam("room-building") String building,
            Model model) {

        try {
            RoomUnit newRoom = new RoomUnit();
            newRoom.setRoomNumber(roomNumber);
            newRoom.setDesignation(designation);
            newRoom.setMaterialType(materialType);
            newRoom.setSeatsCount(seatsCount);
            newRoom.setBuilding(building);

            roomUnitService.createRoom(newRoom);  // Método correto de salvar
            return "redirect:master_index"; // Redirect to the list of rooms
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Incomplete information");
            return "master_addRoom";  // Return to the create room page with error
        } catch (RuntimeException e) {
            model.addAttribute("error", "Duplicate entry or integrity constraint violated");
            return "master_addRoom";  // Return to the create room page with error
        }
    }

    /**
     * Displays the form for editing an existing RoomUnit.
     *
     * @param id The ID of the room to edit
     * @param model The model to pass data to the view
     * @return The name of the view to edit the room
     */
    @GetMapping("/edit-room")
    public String showEditRoomForm(@RequestParam("id") Long id, Model model) {
        RoomUnit room = roomUnitService.getRoomById(id);
        model.addAttribute("room", room);
        return "master_editRoom"; // The page for editing a room
    }

    /**
     * Updates an existing RoomUnit with the provided form data.
     *
     * @param id The ID of the room to update
     * @param roomNumber The room number
     * @param designation The room designation
     * @param materialType The type of material in the room
     * @param seatsCount The number of seats in the room
     * @param building The building where the room is located
     * @param model The model to pass error messages to the view
     * @return A redirect to the list of rooms or an error page
     */
    @PostMapping("/edit-room")
    public String editRoom(
            @RequestParam("room-id") Long id,
            @RequestParam("room-number") String roomNumber,
            @RequestParam("room-designation") String designation,
            @RequestParam("room-material-type") String materialType,
            @RequestParam("room-seats-count") Integer seatsCount,
            @RequestParam("room-building") String building,
            Model model) {

        try {
            RoomUnit updatedRoom = new RoomUnit();
            updatedRoom.setRoomNumber(roomNumber);
            updatedRoom.setDesignation(designation);
            updatedRoom.setMaterialType(materialType);
            updatedRoom.setSeatsCount(seatsCount);
            updatedRoom.setBuilding(building);

            roomUnitService.updateRoom(id, updatedRoom);
            return "redirect:/master_index"; // Redirect to the list of rooms
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Incomplete information");
            return "master_editRoom";  // Return to the edit room page with error
        } catch (RuntimeException e) {
            model.addAttribute("error", "Duplicate entry or integrity constraint violated");
            return "master_editRoom";  // Return to the edit room page with error
        }
    }

    /**
     * Deletes a RoomUnit.
     *
     * @param id The ID of the room unit to be deleted
     * @return A redirect to the list of rooms
     */
    @PostMapping("/remove-room/{id}")
    public String removeRoom(@PathVariable("id") Long id) {
        roomUnitService.deleteRoom(id);
        return "redirect:master_index"; // Redirect to the list of rooms
    }

}
