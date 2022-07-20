package com.musicloud.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SearchController {

    @GetMapping("/search")
    public String getSearch() {
        return "index";
    }
}
