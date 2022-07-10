package com.musicloud.models.dtos.user;

import com.musicloud.validation.MatchingFields;
import com.musicloud.validation.UniqueEmail;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@MatchingFields(first = "password", second = "confirmPassword", message = "Passwords must be matching.")
public class RegisterDto {
    @NotBlank(message = "Enter a valid email.")
    @Email(message = "Enter a valid email.")
    @UniqueEmail(message = "Email is already registered.")
    private String email;
    @NotBlank(message = "Username is required.")
    private String username;
    @NotBlank(message = "Password must be between 8 and 32 characters long.")
    @Length(min = 8, max = 32, message = "Password must be between 8 and 32 characters long.")
    private String password;
    @NotBlank(message = "Password must be between 8 and 32 characters long.")
    @Length(min = 8, max = 32, message = "Password must be between 8 and 32 characters long.")
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
