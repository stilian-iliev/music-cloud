package com.musicloud.web;

import com.musicloud.models.dtos.user.ChangeEmailDto;
import com.musicloud.models.dtos.user.ChangePasswordDto;
import com.musicloud.models.dtos.user.RegisterDto;
import com.musicloud.models.principal.AppUserDetails;
import com.musicloud.services.AuthService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }

    @PostMapping("/login-error")
    public String onFailedLogin(
            @ModelAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY) String userName,
            RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY, userName);
        redirectAttributes.addFlashAttribute("bad_credentials", true);

        return "redirect:/login";
    }
    @ModelAttribute("registerDto")
    public RegisterDto registerDto() {
        return new RegisterDto();
    }
    @GetMapping("/register")
    public String getRegister() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid RegisterDto registerDto,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("registerDto", registerDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registerDto", bindingResult);
            return "redirect:/register";
        }
        authService.register(registerDto);

        return "redirect:/login";
    }

    @GetMapping("/settings")
    public String getSettings(Model model) {
        if (!model.containsAttribute("changePasswordDto")) {
            model.addAttribute("changePasswordDto", new ChangePasswordDto());
        }
        if (!model.containsAttribute("changeEmailDto")) {
            model.addAttribute("changeEmailDto", new ChangeEmailDto());
        }
        return "settings";
    }

    @PostMapping("/settings/email")
    public String changeEmail(@Valid ChangeEmailDto changeEmailDto, BindingResult bindingResult, RedirectAttributes redirectAttributes, @AuthenticationPrincipal AppUserDetails userDetails) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("emailError", true);
            redirectAttributes.addFlashAttribute("changeEmailDto", changeEmailDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.changeEmailDto", bindingResult);
            return "redirect:/settings";
        }
        authService.changeEmail(userDetails, changeEmailDto.getEmail());

        return "redirect:/settings";
    }

    @PostMapping("/settings/password")
    public String changePassword(@Valid ChangePasswordDto changePasswordDto, BindingResult bindingResult, RedirectAttributes redirectAttributes, @AuthenticationPrincipal AppUserDetails userDetails) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("passError", true);
            redirectAttributes.addFlashAttribute("changePasswordDto", changePasswordDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.changePasswordDto", bindingResult);
            return "redirect:/settings";
        }
        authService.changePassword(userDetails, changePasswordDto.getPassword());
        return "redirect:/settings";
    }

    @PostMapping("/settings/delete")
    public String deleteAcc(@AuthenticationPrincipal AppUserDetails userDetails) {
        authService.deleteAccount(userDetails.getId());
        authService.unauthorize();
        return "redirect:/";
    }
}
