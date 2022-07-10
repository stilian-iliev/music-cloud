package com.musicloud.init;

import com.musicloud.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Test implements CommandLineRunner {
    private final UserService userService;

    public Test(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
    }
}
