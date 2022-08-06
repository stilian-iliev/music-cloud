package com.musicloud.models.dtos.user;

import com.musicloud.validation.ExistingEmail;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class PasswordResetRequestDto {
    @Email
    @NotBlank
    @ExistingEmail
    private String email;

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
