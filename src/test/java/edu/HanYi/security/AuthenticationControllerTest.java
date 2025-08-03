package edu.HanYi.security;

import edu.HanYi.model.User;
import edu.HanYi.repository.UserRepository;
import edu.HanYi.security.controller.AuthenticationController;
import edu.HanYi.security.request.SignInRequest;
import edu.HanYi.security.request.SignUpRequest;
import edu.HanYi.security.service.JwtService;
import edu.HanYi.security.service.impl.AuthenticationServiceImpl;
import edu.HanYi.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {
    private static final String USER_EMAIL = "test@mail.ru";
    private static final String USER_PASSWORD = "password";

    @Mock
    private UserRepository userRepository;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthenticationServiceImpl authenticationServiceImpl;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        AuthenticationController authenticationController = new AuthenticationController(authenticationServiceImpl);
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
    }

    @Test
    void processSignUpForm_ShouldRedirectToHome() throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("testuser");
        signUpRequest.setPassword(USER_PASSWORD);
        signUpRequest.setEmail(USER_EMAIL);
        signUpRequest.setRole(User.Role.STUDENT);

        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.empty());
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode(USER_PASSWORD)).thenReturn("encodedPassword");

        User user = new User();
        user.setEmail(USER_EMAIL);
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDetails userDetails = mock(UserDetails.class);
        when(userService.userDetailsService()).thenReturn(email -> userDetails);
        when(jwtService.generateToken(anyMap(), any(UserDetails.class))).thenReturn("testToken");

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", "testuser")
                        .param("password", USER_PASSWORD)
                        .param("email", USER_EMAIL)
                        .param("role", "STUDENT"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }

    @Test
    void processSignInForm_ShouldSetCookieAndRedirect() throws Exception {
        SignInRequest signInRequest = new SignInRequest();
        signInRequest.setEmail(USER_EMAIL);
        signInRequest.setPassword(USER_PASSWORD);

        User user = new User();
        user.setEmail(USER_EMAIL);
        user.setPassword("encodedPassword");

        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(user));

        UserDetails userDetails = mock(UserDetails.class);
        when(userService.userDetailsService()).thenReturn(email -> userDetails);
        when(jwtService.generateToken(anyMap(), any(UserDetails.class))).thenReturn("testToken");

        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);

        mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", USER_EMAIL)
                        .param("password", USER_PASSWORD))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"))
                .andExpect(cookie().exists("JWT"))
                .andExpect(cookie().value("JWT", "testToken"));
    }
}