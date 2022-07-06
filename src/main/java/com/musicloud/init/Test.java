package com.musicloud.init;

import com.musicloud.models.dtos.EditProfileDto;
import com.musicloud.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Component;

@Component
public class Test implements CommandLineRunner {
    private final UserService userService;

    public Test(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        //todo
//        EditProfileDto dto = userService.getEditProfileDto();
//        System.out.println();
    }
}
