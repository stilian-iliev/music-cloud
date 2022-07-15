package com.musicloud.init;

import com.musicloud.models.UserRole;
import com.musicloud.models.enums.UserRoleEnum;
import com.musicloud.repositories.UserRoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RolesSeeder implements CommandLineRunner {
    private final UserRoleRepository userRoleRepository;

    public RolesSeeder(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        for (UserRoleEnum name : UserRoleEnum.values()) {
            if (!userRoleRepository.existsByName(name)) {
                UserRole role = new UserRole(name);
                userRoleRepository.save(role);
            }
        }
    }
}
