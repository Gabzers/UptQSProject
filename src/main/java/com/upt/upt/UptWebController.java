package com.upt.upt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.upt.upt.service.UserService;
import com.upt.upt.entity.UserType;
import org.springframework.web.bind.annotation.GetMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpSession;

@Controller
public class UptWebController {

    private static final Logger logger = LoggerFactory.getLogger(UptWebController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home() {
        return "redirect:/user"; // Redireciona para a URL correta
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid credentials");
        }
        return "login"; // Redireciona para a página login.html
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Invalida a sessão
        return "redirect:/login"; // Redireciona para a página login.html
    }

    @GetMapping("/viewSemester")
    public String viewSemester() {
        return "director_viewSemester"; // Redireciona para a página director_viewSemester.html
    }
    
    @GetMapping("/createRoom")
    public String createRoom() {
        return "master_addRoom"; // Redireciona para a página master_addRoom.html
    }

    @PostMapping("/validate-login")
    public String validateLogin(@RequestParam("username") String username, @RequestParam("password") String password, HttpSession session) {
        logger.info("Entering validateLogin method");
        logger.info("Attempting login with username: {}", username);
        logger.info("Password: {}", password);
        UserType userType = userService.validateUser(username, password);
        if (userType != null) {
            logger.info("Login successful for username: {} with userType: {}", username, userType);
            Long userId = userService.getUserIdByUsername(username, userType); // Obtém o ID do usuário
            session.setAttribute("userId", userId); // Armazena o ID do usuário na sessão
            session.setAttribute("userType", userType); // Armazena o tipo de usuário na sessão
            switch (userType) {
                case MASTER:
                    return "redirect:/master";
                case DIRECTOR:
                    return "redirect:/director";
                case COORDINATOR:
                    return "redirect:/user";
                default:
                    return "redirect:/login?error=Invalid user type";
            }
        } else {
            logger.warn("Login failed for username: {}", username);
            return "redirect:/login?error=Invalid credentials";
        }
    }

}
