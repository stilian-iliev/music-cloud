package com.musicloud.models.dtos;

public class EditProfileDto {
    private String firstName;
    private String lastName;
    private String username;

    public EditProfileDto(String firstName, String lastName, String username) {
        this.firstName = firstName;
        this.lastName = lastName;
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
}
