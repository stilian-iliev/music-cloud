package com.musicloud.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String getHome() {
        return "index";
    }

    @GetMapping("/player")
    public String getPlayer() {
        return "fragments/player";
    }

    @GetMapping("/list")
    public String getList() {
        return "fragments/songlist";
    }
}
