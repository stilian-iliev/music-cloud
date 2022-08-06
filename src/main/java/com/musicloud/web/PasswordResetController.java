package com.musicloud.web;

import com.musicloud.models.ResetPasswordRequest;
import com.musicloud.models.dtos.user.ChangePasswordDto;
import com.musicloud.models.dtos.user.PasswordResetRequestDto;
import com.musicloud.models.dtos.user.ResetPasswordDto;
import com.musicloud.repositories.ResetPasswordRequestRepository;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@Controller
public class PasswordResetController {
    private final ResetPasswordRequestRepository resetPasswordRequestRepository;

    public PasswordResetController(ResetPasswordRequestRepository resetPasswordRequestRepository) {
        this.resetPasswordRequestRepository = resetPasswordRequestRepository;
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
        //todo send email
        return "request-reset-password-post";
    }

    @GetMapping("/reset-password/{id}")
    public String forgotPasswordPage(@PathVariable("id")UUID requestId) {
        Optional<ResetPasswordRequest> request = resetPasswordRequestRepository.findById(requestId);
        if (request.isEmpty() || request.get().isExpired()) return "redirect:/";
        return "";
    }

    @PostMapping("/reset-password/{id}")
    public String resetPassword(@PathVariable("id")UUID requestId, @Valid ResetPasswordDto resetPasswordDto, BindingResult bindingResult) {
        Optional<ResetPasswordRequest> request = resetPasswordRequestRepository.findById(requestId);
        if (request.isEmpty() || request.get().isExpired()) return "redirect:/";
        request.get().getUser().setPassword(resetPasswordDto.getPassword());
        return "redirect:/login";
    }
}
