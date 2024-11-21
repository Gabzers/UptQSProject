package com.upt.upt;

import com.upt.upt.service.CurricularUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UptWebController {

    private final CurricularUnitService curricularUnitService;

    @Autowired
    public UptWebController(CurricularUnitService curricularUnitService) {
        this.curricularUnitService = curricularUnitService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/course-list"; // Redireciona para a URL correta
    }

    @GetMapping("/login")
    public String login() {
        return "redirect:/course-list"; // Redireciona para a URL correta
    }

    @GetMapping("/back")
    public String back() {
        return "redirect:/course-list"; // Redireciona para a página user.html quando o botão "Back" é clicado
    }

    @GetMapping("/logout")
    public String logout() {
        return "login"; // Redireciona para a página login.html quando o botão "Back" é clicado
    }

    @GetMapping("/createUC")
    public String createUC() {
        return "create-uc"; // Redireciona para a página createUC.html
    }


    @GetMapping("/admin")
    public String admin() {
        return "admin"; // Redireciona para a página createUC.html
    }
}
