package com.upt.upt;

import com.upt.upt.entity.CurricularUnit;
import com.upt.upt.service.CurricularUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UptWebController {

    private final CurricularUnitService curricularUnitService;

    @Autowired
    public UptWebController(CurricularUnitService curricularUnitService) {
        this.curricularUnitService = curricularUnitService;
    }

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

    @GetMapping("/createUC")
    public String createUC() {
        return "userUC";  // Redireciona para a página createUC.html
    }

    // Método POST para receber os dados do formulário
    @PostMapping("/create-uc")
    public String createUC(@RequestParam("uc-name") String nameUC,
                           @RequestParam("num-students") Integer studentsNumber,
                           @RequestParam("evaluation-type") String typeUC) {
        // Criar a nova CurricularUnit com os dados do formulário
        CurricularUnit curricularUnit = new CurricularUnit();
        curricularUnit.setNameUC(nameUC);
        curricularUnit.setStudentsNumber(studentsNumber);
        curricularUnit.setTypeUC(typeUC);

        // Salvar a CurricularUnit no banco de dados
        curricularUnitService.saveCurricularUnit(curricularUnit);

        return "redirect:/user";  // Redirecionar para a página de usuário após criar a UC
    }
}
