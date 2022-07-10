package com.musicloud.web;

import com.musicloud.models.dtos.user.ChangeEmailDto;
import com.musicloud.models.dtos.user.ChangePasswordDto;
import com.musicloud.models.principal.AppUserDetails;
import com.musicloud.services.AuthService;
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
    private final AuthService authService;

    public AccountController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
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
        authService.changeEmail(userDetails, changeEmailDto.getEmail());

        return "redirect:/settings";
    }

    @PostMapping("/settings/password")
    public String changePassword(@Valid ChangePasswordDto changePasswordDto, BindingResult bindingResult, RedirectAttributes redirectAttributes, @AuthenticationPrincipal AppUserDetails userDetails) {
        if (bindingResult.hasErrors()) {
            //todo: show notification if successful
            return "redirect:/settings";
        }
        authService.changePassword(userDetails, changePasswordDto.getPassword());
        return "redirect:/settings";
    }

    @PostMapping("/settings/delete")
    public String deleteAcc(@AuthenticationPrincipal AppUserDetails userDetails) {
        authService.deleteAccount(userDetails);
        authService.unauthorize();
        return "redirect:/";
    }

}
