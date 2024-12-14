package com.upt.upt.controller;

import com.upt.upt.entity.RoomUnit;
import com.upt.upt.entity.AssessmentUnit;
import com.upt.upt.entity.UserType;
import com.upt.upt.service.RoomUnitService;
import com.upt.upt.service.AssessmentUnitService;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

/**
 * Controller for handling requests related to RoomUnit entities.
 */
@Controller
@RequestMapping("/master")
public class RoomUnitController {

    @Autowired
    private RoomUnitService roomUnitService;

    @Autowired
    private AssessmentUnitService assessmentUnitService;

    private boolean isMaster(HttpSession session) {
        UserType userType = (UserType) session.getAttribute("userType");
        return userType == UserType.MASTER;
    }

    @GetMapping("/rooms")
    public String listRooms(Model model, HttpSession session) {
        if (!isMaster(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        model.addAttribute("roomUnits", roomUnitService.getAllRooms());
        return "master_index";
    }

    @GetMapping("/create-room")
    public String showCreateRoomForm(HttpSession session) {
        if (!isMaster(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        return "master_addRoom";
    }

    @PostMapping("/remove-room/{id}")
    public String removeRoom(@PathVariable("id") Long id, Model model, HttpSession session) {
        if (!isMaster(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        RoomUnit room = roomUnitService.getRoomById(id);
        if (room != null) {
            model.addAttribute("warning", "Are you sure you want to remove this room?");
            model.addAttribute("roomId", id);
            return "master_confirmRemoveRoom";
        }
        return "redirect:/master";
    }

    @PostMapping("/confirm-remove-room/{id}")
    public String confirmRemoveRoom(@PathVariable("id") Long id, HttpSession session) {
        if (!isMaster(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        RoomUnit room = roomUnitService.getRoomById(id);
        if (room != null) {
            List<AssessmentUnit> assessments = assessmentUnitService.getAssessmentsByRoomId(id);
            for (AssessmentUnit assessment : assessments) {
                assessmentUnitService.deleteAssessment(assessment.getCurricularUnit().getId(), assessment.getId());
            }
            roomUnitService.deleteRoom(id);
        }
        return "redirect:/master";
    }

    @PostMapping("/save-assessment")
    public String saveAssessment(@RequestParam Map<String, String> params, @RequestParam List<Long> roomIds, Model model, HttpSession session) {
        if (!isMaster(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        try {
            AssessmentUnit assessment = new AssessmentUnit();
            // Set other assessment properties from params
            assessmentUnitService.saveAssessment(assessment, roomIds);
            return "redirect:/master";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Incomplete information");
            return "master_addAssessment";
        } catch (RuntimeException e) {
            model.addAttribute("error", "Duplicate entry or integrity constraint violated");
            return "master_addAssessment";
        }
    }

    @PostMapping("/update-assessment")
    public String updateAssessment(@RequestParam Long id, @RequestParam Map<String, String> params, @RequestParam List<Long> roomIds, Model model, HttpSession session) {
        if (!isMaster(session)) {
            return "redirect:/login?error=Unauthorized access";
        }
        try {
            AssessmentUnit updatedAssessment = new AssessmentUnit();
            // Set other updatedAssessment properties from params
            assessmentUnitService.updateAssessment(id, updatedAssessment, roomIds);
            return "redirect:/master";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Incomplete information");
            return "master_editAssessment";
        } catch (RuntimeException e) {
            model.addAttribute("error", "Duplicate entry or integrity constraint violated");
            return "master_editAssessment";
        }
    }
}
