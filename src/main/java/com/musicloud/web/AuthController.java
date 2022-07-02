package com.musicloud.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }

    @PostMapping("/login-error")
    public String getError() {
        return "navigation";
    }

    @GetMapping("/register")
    public String getRegister() {
        return "register";
    }
}
