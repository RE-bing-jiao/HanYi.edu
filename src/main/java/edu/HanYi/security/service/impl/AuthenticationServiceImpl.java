package edu.HanYi.security.service.impl;

import edu.HanYi.exception.ResourceAlreadyExistsException;
import edu.HanYi.exception.ResourceNotFoundException;
import edu.HanYi.model.User;
import edu.HanYi.repository.UserRepository;
import edu.HanYi.security.request.SignInRequest;
import edu.HanYi.security.request.SignUpRequest;
import edu.HanYi.security.response.JwtAuthenticationResponse;
import edu.HanYi.security.service.AuthenticationService;
import edu.HanYi.security.service.JwtService;
import edu.HanYi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @Override
    public JwtAuthenticationResponse signup(SignUpRequest request) {
        log.info("Attempting to sign up user: {}", request.getEmail());

        if(userRepository.findByEmail(request.getEmail()).isPresent()) {
            log.warn("Signup failed - email already exists: {}", request.getEmail());
            throw new ResourceAlreadyExistsException("Email already in use");
        }
        if(userRepository.findByUsername(request.getUsername()).isPresent()) {
            log.warn("Signup failed - username already exists: {}", request.getUsername());
            throw new ResourceAlreadyExistsException("Username already in use");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setRole(request.getRole() != null ? request.getRole() : User.Role.STUDENT);
        userRepository.save(user);
        log.info("User created successfully: {}", user.getEmail());

        UserDetails userDetails = userService.userDetailsService().loadUserByUsername(user.getEmail());
        String token = jwtService.generateToken(new HashMap<>(), userDetails);

        log.info("JWT token generated for user: {}", user.getEmail());
        return JwtAuthenticationResponse.builder().token(token).build();
    }

    @Override
    public JwtAuthenticationResponse signin(SignInRequest request) {
        log.info("Attempting to sign in user: {}", request.getEmail());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> {
                        log.error("User not found after successful authentication: {}", request.getEmail());
                        return new ResourceNotFoundException("User not found");
                    });

            UserDetails userDetails = userService.userDetailsService().loadUserByUsername(user.getEmail());
            String token = jwtService.generateToken(new HashMap<>(), userDetails);

            log.info("User successfully signed in: {}", request.getEmail());
            return JwtAuthenticationResponse.builder().token(token).build();

        } catch (AuthenticationException e) {
            log.error("Authentication failed for user: {}", request.getEmail(), e);
            throw new BadCredentialsException("Invalid credentials");
        }
    }
}
