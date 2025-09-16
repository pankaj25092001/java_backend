package com.example.Pankaj.orderup.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomePageController {

    @GetMapping("/")
    public String welcome() {
        return "Hlw There";
    }
}
