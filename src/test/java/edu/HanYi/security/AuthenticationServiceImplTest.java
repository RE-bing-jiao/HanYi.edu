package edu.HanYi.security;

import edu.HanYi.exception.ResourceAlreadyExistsException;
import edu.HanYi.exception.ResourceNotFoundException;
import edu.HanYi.model.User;
import edu.HanYi.repository.UserRepository;
import edu.HanYi.security.request.SignInRequest;
import edu.HanYi.security.request.SignUpRequest;
import edu.HanYi.security.response.JwtAuthenticationResponse;
import edu.HanYi.security.service.JwtService;
import edu.HanYi.security.service.impl.AuthenticationServiceImpl;
import edu.HanYi.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserService userService;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    private SignUpRequest signUpRequest;
    private SignInRequest signInRequest;
    private User user;
    private static final String USER_EMAIL = "test@mail.ru";
    private static final String USER_PASSWORD = "password";

    @BeforeEach
    void setUp() {
        signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("testuser");
        signUpRequest.setPassword(USER_PASSWORD);
        signUpRequest.setEmail(USER_EMAIL);
        signUpRequest.setRole(User.Role.STUDENT);

        signInRequest = new SignInRequest();
        signInRequest.setEmail(USER_EMAIL);
        signInRequest.setPassword(USER_PASSWORD);

        user = new User();
        user.setId(1);
        user.setUsername("testuser");
        user.setPassword(passwordEncoder.encode(USER_PASSWORD));
        user.setEmail(USER_EMAIL);
        user.setRole(User.Role.STUDENT);
    }

    @Test
    void signup_NewUser_ShouldReturnToken() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDetailsService userDetailsService = mock(UserDetailsService.class);
        when(userService.userDetailsService()).thenReturn(userDetailsService);
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(user);

        when(jwtService.generateToken(anyMap(), any(UserDetails.class))).thenReturn("testToken");

        JwtAuthenticationResponse response = authenticationService.signup(signUpRequest);

        assertNotNull(response);
        assertEquals("testToken", response.getToken());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void signup_ExistingEmail_ShouldThrowException() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        assertThrows(ResourceAlreadyExistsException.class,
                () -> authenticationService.signup(signUpRequest));
    }

    @Test
    void signup_ExistingUsername_ShouldThrowException() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        assertThrows(ResourceAlreadyExistsException.class,
                () -> authenticationService.signup(signUpRequest));
    }

    @Test
    void signin_ValidCredentials_ShouldReturnToken() {
        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        UserDetailsService userDetailsService = mock(UserDetailsService.class);
        when(userService.userDetailsService()).thenReturn(userDetailsService);
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(user);

        when(jwtService.generateToken(anyMap(), any(UserDetails.class))).thenReturn("testToken");

        JwtAuthenticationResponse response = authenticationService.signin(signInRequest);

        assertNotNull(response);
        assertEquals("testToken", response.getToken());
    }

    @Test
    void signin_InvalidCredentials_ShouldThrowException() {
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        assertThrows(BadCredentialsException.class,
                () -> authenticationService.signin(signInRequest));
    }

    @Test
    void signin_UserNotFoundAfterAuth_ShouldThrowException() {
        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> authenticationService.signin(signInRequest));
    }
}