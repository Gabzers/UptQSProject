package com.upt.upt;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UptWebController {

    @GetMapping("/")
    public String home() {
        return "user";  // Página inicial que você deseja exibir
    }

    @GetMapping("/back")
    public String back() {
        return "user";  // Redireciona para a página user.html quando o botão "Back" é clicado
    }

    @GetMapping("/logout")
    public String logout() {
        return "login";  // Redireciona para a página login.html quando o botão "Back" é clicado
    }

    @GetMapping("/user")
    public String userPage() {
        return "user";  // Outra página de usuário, caso necessário
    }

    @GetMapping("/edit")
    public String edit() {
        return "userEvaluation";  // Redireciona para a página user.html quando o botão "Back" é clicado
    }

    @GetMapping("/createUC")
    public String createUC() {
        return "userUC";  // Redireciona para a página createUC.html quando o botão "Back" é clicado
    }
}
