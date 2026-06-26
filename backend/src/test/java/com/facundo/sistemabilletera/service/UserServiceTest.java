package com.facundo.sistemabilletera.service;

import com.facundo.sistemabilletera.model.AppUser;
import com.facundo.sistemabilletera.repository.AppUserRepository;
import com.facundo.sistemabilletera.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void loginShouldReturnUserWhenCredentialsAreValid() {
        AppUser user = new AppUser();
        user.setId(1L);
        user.setFullName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("encoded-password");

        when(appUserRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("123456", "encoded-password")).thenReturn(true);

        AppUser result = userService.login("test@example.com", "123456");

        assertEquals(1L, result.getId());
        assertEquals("test@example.com", result.getEmail());

        verify(appUserRepository).findByEmail("test@example.com");
        verify(passwordEncoder).matches("123456", "encoded-password");
    }

    @Test
    void loginShouldFailWhenPasswordIsInvalid() {
        AppUser user = new AppUser();
        user.setId(1L);
        user.setFullName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("encoded-password");

        when(appUserRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong-password", "encoded-password")).thenReturn(false);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.login("test@example.com", "wrong-password")
        );

        assertEquals("Invalid email or password", exception.getMessage());

        verify(appUserRepository).findByEmail("test@example.com");
        verify(passwordEncoder).matches("wrong-password", "encoded-password");
    }


}
