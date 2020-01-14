package edu.progmatic.messageapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Controller
public class HomeController {
    private static ArrayList<String> greetings = new ArrayList<>();

    static {
        greetings.add("Koniciwa");
        greetings.add("Szia");
        greetings.add("Hello");
        greetings.add("Saluti");
    }

    @GetMapping(value = "/home")
    public String greetUser(Model model,
                            @RequestParam(value = "name", required = false) String name) {
        for (int i = 1; i < 5; i++){
            String value = "greetingText";
            model.addAttribute(value = value + i, greetings.get(i-1));
        }
        return "/home";
    }



}
