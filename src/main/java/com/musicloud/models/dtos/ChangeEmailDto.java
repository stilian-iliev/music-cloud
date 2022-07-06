package com.musicloud.models.dtos;

import com.musicloud.validation.UniqueEmail;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class ChangeEmailDto {
    @NotBlank(message = "Enter a valid email.")
    @Email(message = "Enter a valid email.")
    @UniqueEmail(message = "Email is already registered.")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
