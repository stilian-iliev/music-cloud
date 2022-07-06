package com.musicloud.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AccountController {
    @GetMapping("/settings")
    public String getSettings() {
        return "settings";
    }

    @PostMapping("/settings/email")
    public String changeEmail(String email) {
        //todo:change email and password
        //todo:add delete account button
        return "redirect:/";
    }
}
