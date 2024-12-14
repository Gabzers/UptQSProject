package com.upt.upt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.upt.upt.service.UserService;
import com.upt.upt.entity.UserType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpSession;

@Controller
public class UptWebController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home() {
        return "redirect:/login"; // Redireciona para a URL correta
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

    @PostMapping("/validate-login")
    public String validateLogin(@RequestParam("username") String username, @RequestParam("password") String password, HttpSession session) {
        UserType userType = userService.validateUser(username, password);
        if (userType != null) {
            Long userId = userService.getUserIdByUsername(username, userType); // Obtém o ID do usuário
            session.setAttribute("userId", userId); // Armazena o ID do usuário na sessão
            session.setAttribute("userType", userType); // Armazena o tipo de usuário na sessão
            session.setAttribute("username", username); // Armazena o username na sessão
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
