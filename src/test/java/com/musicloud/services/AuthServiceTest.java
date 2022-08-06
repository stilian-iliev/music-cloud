package com.musicloud.services;

import com.musicloud.models.dtos.user.RegisterDto;
import com.musicloud.repositories.UserRepository;
import com.musicloud.repositories.UserRoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository mockUserRepo;

    @Autowired
    private AuthService toTest;
    @Mock
    private EmailService mockEmailService;
    @Mock
    private PasswordEncoder mockPasswordEncoder;
    @Mock
    private ModelMapper mockModelMapper;
    @Mock
    private UserRoleRepository mockRoleRepo;
    private RegisterDto registerDto;


//    @BeforeEach
//    void setUp() {
//        mockModelMapper = new ModelMapper();
//        registerDto = new RegisterDto();
//        toTest = new AuthService(mockUserRepo, mockEmailService, mockPasswordEncoder, mockModelMapper, mockRoleRepo, resetPasswordRequestRepository);
//    }

//    @Test
//    void testUserRegister() {
//        registerDto.setEmail("register@test.com");
//        registerDto.setUsername("test");
//        registerDto.setPassword("topsecret");
//        registerDto.setConfirmPassword("topsecret");
//        toTest.register(registerDto);
//    }
}
