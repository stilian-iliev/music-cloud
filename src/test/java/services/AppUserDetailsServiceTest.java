package services;

import com.musicloud.models.User;
import com.musicloud.models.UserRole;
import com.musicloud.models.enums.UserRoleEnum;
import com.musicloud.models.principal.AppUserDetails;
import com.musicloud.repositories.UserRepository;
import com.musicloud.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Set;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AppUserDetailsServiceTest {
    @Mock
    private UserRepository mockUserRepo;

    private UserDetailsServiceImpl toTest;

    @BeforeEach
    void setUp() {
        toTest = new UserDetailsServiceImpl(
                mockUserRepo
        );
    }

    @Test
    void testLoadUserByUsername_UserExists() {
        User testUser = new User();

        testUser.setEmail("test@example.com");
        testUser.setUsername("test");
        testUser.setFirstName("test");
        testUser.setLastName("testov");
        testUser.setPassword("topsecret");
        testUser.setImageUrl("http://image.com/image");
        testUser.setRoles(Set.of(
                new UserRole(UserRoleEnum.USER),
                new UserRole(UserRoleEnum.ADMIN)
        ));
        when(mockUserRepo.existsByEmail(testUser.getEmail())).thenReturn(true);
        when(mockUserRepo.findByEmail(testUser.getEmail())).thenReturn(testUser);

        AppUserDetails userDetails = (AppUserDetails)
                toTest.loadUserByUsername(testUser.getEmail());

        Assertions.assertEquals(testUser.getEmail(), userDetails.getUsername());
        Assertions.assertEquals(testUser.getUsername(), userDetails.getDisplayName());
        Assertions.assertEquals(testUser.getPassword(), userDetails.getPassword());
        Assertions.assertEquals(testUser.getImageUrl() ,userDetails.getImageUrl());

        var authorities = userDetails.getAuthorities();

        Assertions.assertEquals(2, authorities.size());

        var authoritiesIter = authorities.iterator();

        Assertions.assertEquals("ROLE_" + UserRoleEnum.USER.name(),
                authoritiesIter.next().getAuthority());
        Assertions.assertEquals("ROLE_" + UserRoleEnum.ADMIN.name(),
                authoritiesIter.next().getAuthority());
    }

    @Test
    void testLoadUserByUsername_UserDoesNotExist() {
        Assertions.assertThrows(
                UsernameNotFoundException.class,
                () -> toTest.loadUserByUsername("non-existant@example.com")
        );
    }
}
