package com.upt.upt;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UptWebController {

    @GetMapping("/")
    public String home() {
        return "redirect:/user"; // Redireciona para a URL correta
    }

    @GetMapping("/login")
    public String login() {
        return "redirect:/user"; // Redireciona para a URL correta
    }

    @GetMapping("/logout")
    public String logout() {
        return "login"; // Redireciona para a página login.html
    }

    @GetMapping("/createUC")
    public String createUC() {
        return "user_createUC"; // Redireciona para a página user_createUC.html
    }


    @GetMapping("/viewSemester")
    public String viewSemester() {
        return "director_viewSemester"; // Redireciona para a página director_viewSemester.html
    }
    
    @GetMapping("/createRoom")
    public String createRoom() {
        return "master_addRoom"; // Redireciona para a página master_addRoom.html
    }
}
