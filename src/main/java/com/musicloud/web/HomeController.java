package com.musicloud.web;

import com.musicloud.models.principal.AppUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping(value = "/")
    public String getHome(@AuthenticationPrincipal AppUserDetails userDetails) {
        if (userDetails == null) return "welcome";
        return "index";
    }


}
