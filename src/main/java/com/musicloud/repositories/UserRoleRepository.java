package com.musicloud.repositories;

import com.musicloud.models.UserRole;
import com.musicloud.models.enums.UserRoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    boolean existsByName(UserRoleEnum name);

    UserRole findByName(UserRoleEnum user);
}
