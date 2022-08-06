package com.musicloud.web;

import com.musicloud.models.ResetPasswordRequest;
import com.musicloud.models.dtos.user.PasswordResetRequestDto;
import com.musicloud.models.dtos.user.ResetPasswordDto;
import com.musicloud.repositories.ResetPasswordRequestRepository;
import com.musicloud.services.AuthService;
import com.musicloud.services.EmailService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@Controller
public class PasswordResetController {
    private final ResetPasswordRequestRepository resetPasswordRequestRepository;
    private final EmailService emailService;
    private final AuthService authService;

    public PasswordResetController(ResetPasswordRequestRepository resetPasswordRequestRepository, EmailService emailService, AuthService authService) {
        this.resetPasswordRequestRepository = resetPasswordRequestRepository;
        this.emailService = emailService;
        this.authService = authService;
    }

    @GetMapping("/request-reset-password")
    public String requestResetPage(Model model) {
        if (!model.containsAttribute("passwordResetRequestDto")) model.addAttribute("passwordResetRequestDto", new PasswordResetRequestDto());
        return "request-reset-password";
    }

    @PostMapping("/request-reset-password")
    public String requestReset(@Valid PasswordResetRequestDto passwordResetRequestDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("passwordResetRequestDto" ,passwordResetRequestDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.passwordResetRequestDto", bindingResult);
            return "redirect:/request-reset-password";
        }
        ResetPasswordRequest resetPasswordRequest = authService.generateResetPasswordRequest(passwordResetRequestDto.getEmail());
        emailService.sendResetPasswordEmail(passwordResetRequestDto.getEmail(), resetPasswordRequest.getId());
        return "request-reset-password-post";
    }

    @GetMapping("/reset-password/{id}")
    public String forgotPasswordPage(Model model, @PathVariable("id")UUID requestId) {
        Optional<ResetPasswordRequest> request = resetPasswordRequestRepository.findById(requestId);
        if (request.isEmpty() || request.get().isExpired() || request.get().isUsed()) return "redirect:/";
        if (!model.containsAttribute("resetPasswordDto")) model.addAttribute("resetPasswordDto", new ResetPasswordDto());
        model.addAttribute("id", request.get().getId());
        return "reset-password";
    }

    @PostMapping("/reset-password/{id}")
    public String resetPassword(@PathVariable("id")UUID requestId, @Valid ResetPasswordDto resetPasswordDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        Optional<ResetPasswordRequest> request = resetPasswordRequestRepository.findById(requestId);
        if (request.isEmpty() || request.get().isExpired()) return "redirect:/";
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("resetPasswordDto", resetPasswordDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.resetPasswordDto", bindingResult);
            return "redirect:/reset-password/"+ request.get().getId();
        }
        authService.changePassword(request.get().getUser().getId(), resetPasswordDto.getPassword());
        authService.changePassword(request.get(), resetPasswordDto.getPassword());
//        authService.login
        return "redirect:/";
    }
}
