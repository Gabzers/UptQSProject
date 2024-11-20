package com.upt.upt;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UptWebController {

    @GetMapping("/")
    public String home() {
        return "user"; 
    }
}
