package com.musicloud.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {
    @GetMapping("/profile")
    public String getProfile() {
        return "profile-details";
    }

    @GetMapping("/profile/edit")
    public String getEditProfile() {
        return "profile-edit";
    }

}
