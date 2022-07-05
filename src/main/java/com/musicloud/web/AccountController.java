package com.musicloud.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccountController {
    @GetMapping("/settings")
    public String getSettings() {
        return "settings";
    }
}
