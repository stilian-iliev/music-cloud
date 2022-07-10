package com.musicloud.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping(value = "/")
    public String getHome() {
        return "index";
    }

    @GetMapping(value = "/search")
    public String getSerach() {
        return "index";
    }

}
