package com.musicloud.models.dtos;

import com.musicloud.validation.MatchingFields;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@MatchingFields(first = "password", second = "confirmPassword", message = "Password are not matching!")
public class ChangePasswordDto {
    private String oldPassword;

    @NotBlank(message = "Password must be between 8 and 32 characters long.")
    @Length(min = 8, max = 32, message = "Password must be between 8 and 32 characters long.")
    private String password;

    @NotBlank(message = "Password must be between 8 and 32 characters long.")
    @Length(min = 8, max = 32, message = "Password must be between 8 and 32 characters long.")
    private String confirmPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
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
