package edu.HanYi.security;

import edu.HanYi.model.User;
import edu.HanYi.repository.UserRepository;

import edu.HanYi.security.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    private static final String USER_EMAIL = "test@mail.ru";
    private static final String USER_PASSWORD = "password";

    @BeforeEach
    void setUp() {
        userRepository.findByEmail(USER_EMAIL).ifPresent(user -> userRepository.delete(user));

        User user = new User();
        user.setUsername("testuser");
        user.setPassword(passwordEncoder.encode(USER_PASSWORD));
        user.setEmail(USER_EMAIL);
        user.setRole(User.Role.STUDENT);
        userRepository.save(user);
    }

    @Test
    void accessPublicEndpoint_WithoutAuth_ShouldSucceed() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(status().isOk());
    }

    @Test
    void accessProtectedEndpoint_WithoutAuth_ShouldFail() throws Exception {
        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().isForbidden());
    }

    @Test
    void accessProtectedEndpoint_WithValidToken_ShouldSucceed() throws Exception {
        User user = userRepository.findByEmail(USER_EMAIL)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_STUDENT")));

        String token = jwtService.generateToken(new HashMap<>(), userDetails);

        mockMvc.perform(get("/api/users/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());

        user.setRole(User.Role.ADMIN);
        userRepository.saveAndFlush(user);

        User adminUser = userRepository.findByEmail(USER_EMAIL)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDetails adminDetails = new org.springframework.security.core.userdetails.User(
                adminUser.getEmail(),
                adminUser.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));

        String adminToken = jwtService.generateToken(new HashMap<>(), adminDetails);

        mockMvc.perform(get("/api/users")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());
    }

    @Test
    void signin_WithValidCredentials_ShouldReturnToken() throws Exception {
        mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", USER_EMAIL)
                        .param("password", USER_PASSWORD))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"))
                .andExpect(cookie().exists("JWT"));
    }

    @Test
    void signin_WithInvalidCredentials_ShouldFail() throws Exception {
        mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", USER_EMAIL)
                        .param("password", "wrongpassword"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/signin"));
    }
}