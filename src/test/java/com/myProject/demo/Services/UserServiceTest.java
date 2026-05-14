package com.myProject.demo.Services;

import com.myProject.demo.DTO.LoginDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;


import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private AuthenticationManager authmanager;

    @Mock
    private JWTService jwtService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserService authService;

    @Test
    void loginShouldReturnTokenWhenCredentialsValid() {

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("baraa");
        loginDTO.setPassword("Baraa@123");

        when(authmanager.authenticate(any()))
                .thenReturn(authentication);

        when(authentication.isAuthenticated()).thenReturn(true);

        Collection<GrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority("ROLE_USER"));

        when(jwtService.generateToken("baraa", "USER"))
                .thenReturn("fake-token");

        String result = authService.verify(loginDTO);

        assertEquals("fake-token", result);

        verify(authmanager).authenticate(any());
        verify(jwtService).generateToken("baraa", "USER");
    }
    @Test
    void loginShouldReturnFailed_whenNotAuthenticated() {

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("baraa");
        loginDTO.setPassword("1234");

        when(authmanager.authenticate(any()))
                .thenReturn(authentication);

        when(authentication.isAuthenticated()).thenReturn(false);

        String result = authService.verify(loginDTO);

        assertEquals("failed", result);

        verify(jwtService, never()).generateToken(any(), any());
    }



}
