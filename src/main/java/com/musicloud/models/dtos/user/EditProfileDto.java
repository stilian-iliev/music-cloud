package com.musicloud.models.dtos.user;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

public class EditProfileDto {
    private String firstName;
    private String lastName;
    @NotBlank
    private String username;
    private MultipartFile image;

    public EditProfileDto() {
    }

    public EditProfileDto(String firstName, String lastName, String username) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }
}
