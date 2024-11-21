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
        return "redirect:/user"; // Redireciona para a URL correta
    }

    @GetMapping("/login")
    public String login() {
        return "redirect:/user"; // Redireciona para a URL correta
    }

    @GetMapping("/backUser")
    public String backUser() {
        return "redirect:/user"; // Redireciona para a página user.html quando o botão "Back" é clicado
    }

    @GetMapping("/backMaster")
    public String backMaster() {
        return "redirect:/master"; // Redireciona para a página user.html quando o botão "Back" é clicado
    }

    @GetMapping("/logout")
    public String logout() {
        return "login"; // Redireciona para a página login.html quando o botão "Back" é clicado
    }

    @GetMapping("/createUC")
    public String createUC() {
        return "user_createUC"; // Redireciona para a página user_createUC.html
    }

    @GetMapping("/createAdmin")
    public String createAdmin() {
        return "master-addAdmin"; // Redireciona para a página createUC.html
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin_index"; // Redireciona para a página createUC.html
    }
    
    @GetMapping("/master")
    public String master() {
        return "master_index"; // Redireciona para a página createUC.html
    }
}
