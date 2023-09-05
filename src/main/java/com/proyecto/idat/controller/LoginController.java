package com.proyecto.idat.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class LoginController {

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }
}
