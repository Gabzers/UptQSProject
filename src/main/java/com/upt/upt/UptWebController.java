package com.upt.upt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import com.upt.upt.service.UserService;
import com.upt.upt.entity.UserType;

/**
 * Controller class for handling web requests related to user authentication and session management.
 * 
 * @author grupo 5 - 47719, 47713, 46697, 47752, 47004
 */
@Controller
public class UptWebController {

    @Autowired
    private UserService userService;

    /**
     * Redirects to the login page.
     * 
     * @return the redirect URL to the login page
     */
    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    /**
     * Displays the login page.
     * 
     * @param error optional error message to display
     * @param model the model to add attributes to
     * @return the login page view name
     */
    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid credentials");
        }
        return "login";
    }

    /**
     * Logs out the user by invalidating the session.
     * 
     * @param session the current HTTP session
     * @return the redirect URL to the login page
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    /**
     * Validates the user login credentials and sets session attributes based on the user type.
     * 
     * @param username the username entered by the user
     * @param password the password entered by the user
     * @param session the current HTTP session
     * @return the redirect URL based on the user type or an error message if validation fails
     */
    @PostMapping("/validate-login")
    public String validateLogin(@RequestParam("username") String username, @RequestParam("password") String password, HttpSession session) {
        UserType userType = userService.validateUser(username, password);
        if (userType != null) {
            Long userId = userService.getUserIdByUsername(username, userType);
            session.setAttribute("userId", userId);
            session.setAttribute("userType", userType);
            session.setAttribute("username", username);
            switch (userType) {
                case MASTER:
                    return "redirect:/master";
                case DIRECTOR:
                    return "redirect:/director";
                case COORDINATOR:
                    return "redirect:/coordinator";
                default:
                    return "redirect:/login?error=Invalid user type";
            }
        } else {
            return "redirect:/login?error=Invalid credentials";
        }
    }
}
