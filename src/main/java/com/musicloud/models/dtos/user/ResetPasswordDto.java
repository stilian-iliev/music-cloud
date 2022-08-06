package com.musicloud.models.dtos.user;

import com.musicloud.validation.MatchingFields;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@MatchingFields(first = "password", second = "confirmPassword")
public class ResetPasswordDto {
    @NotBlank
    @Length(min = 8, max = 32)
    private String password;

    private String confirmPassword;

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

