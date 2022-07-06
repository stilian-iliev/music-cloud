package com.musicloud.web;

import com.musicloud.models.dtos.ChangeEmailDto;
import com.musicloud.models.dtos.ChangePasswordDto;
import com.musicloud.models.principal.AppUserDetails;
import com.musicloud.services.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class AccountController {
    private final UserService userService;

    public AccountController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/settings")
    public String getSettings() {
        return "settings";
    }

    @PostMapping("/settings/email")
    public String changeEmail(@Valid ChangeEmailDto changeEmailDto, BindingResult bindingResult, RedirectAttributes redirectAttributes, @AuthenticationPrincipal AppUserDetails userDetails) {
        if (bindingResult.hasErrors()) {
            //todo:make validation (maybe just front end)
            return "redirect:/settings";
        }
        userService.changeEmail(userDetails, changeEmailDto.getEmail());

        return "redirect:/settings";
    }

    @PostMapping("/settings/password")
    public String changeEmail(@Valid ChangePasswordDto changePasswordDto, BindingResult bindingResult, RedirectAttributes redirectAttributes, @AuthenticationPrincipal AppUserDetails userDetails) {
        if (bindingResult.hasErrors()) {
            return "redirect:/settings";
        }
        userService.changePassword(userDetails, changePasswordDto.getOldPassword(), changePasswordDto.getPassword());
        return "redirect:/settings";
    }

    @PostMapping("/settings/delete")
    public String deleteAcc(@AuthenticationPrincipal AppUserDetails userDetails) {

        userService.deleteAccount(userDetails);

        return "redirect:/logout";
    }

    //todo:change email and password
    //todo:add delete account button
}
