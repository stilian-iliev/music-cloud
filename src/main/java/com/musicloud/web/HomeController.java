package com.musicloud.web;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping(value = "/" , produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public String getHome() {
        return "index";
    }

}
