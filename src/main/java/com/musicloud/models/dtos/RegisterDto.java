package com.musicloud.models.dtos;

import com.musicloud.validation.MatchingFields;
import com.musicloud.validation.UniqueEmail;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@MatchingFields(first = "password", second = "confirmPassword", message = "Passwords must be matching.")
public class RegisterDto {
    @NotBlank
    @Email
    @UniqueEmail(message = "Email is already registered.")
    private String email;
    @NotBlank
    private String username;
    @NotBlank
    @Length(min = 8, max = 32)
    private String password;
    @NotBlank
    @Length(min = 8, max = 32)
    private String confirmPassword;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
