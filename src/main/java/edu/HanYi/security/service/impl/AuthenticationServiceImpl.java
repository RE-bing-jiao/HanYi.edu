package edu.HanYi.security.service.impl;

import edu.HanYi.constants.LoggingConstants;
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
        log.info(LoggingConstants.SIGNUP_ATTEMPT, request.getEmail());

        if(userRepository.findByEmail(request.getEmail()).isPresent()) {
            log.warn(LoggingConstants.SIGNUP_EMAIL_EXISTS, request.getEmail());
            throw new ResourceAlreadyExistsException("Email already in use");
        }
        if(userRepository.findByUsername(request.getUsername()).isPresent()) {
            log.warn(LoggingConstants.SIGNUP_USERNAME_EXISTS, request.getUsername());
            throw new ResourceAlreadyExistsException("Username already in use");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setRole(request.getRole() != null ? request.getRole() : User.Role.STUDENT);
        userRepository.save(user);
        log.info(LoggingConstants.USER_CREATION_SUCCESS, user.getEmail());

        UserDetails userDetails = userService.userDetailsService().loadUserByUsername(user.getEmail());
        String token = jwtService.generateToken(new HashMap<>(), userDetails);

        log.info(LoggingConstants.TOKEN_GENERATED, user.getEmail());
        return JwtAuthenticationResponse.builder().token(token).build();
    }

    @Override
    public JwtAuthenticationResponse signin(SignInRequest request) {
        log.info(LoggingConstants.SIGNIN_ATTEMPT, request.getEmail());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> {
                        log.error(LoggingConstants.USER_NOT_FOUND_AFTER_AUTH, request.getEmail());
                        return new ResourceNotFoundException("User not found");
                    });

            UserDetails userDetails = userService.userDetailsService().loadUserByUsername(user.getEmail());
            String token = jwtService.generateToken(new HashMap<>(), userDetails);

            log.info(LoggingConstants.SIGNIN_SUCCESS, request.getEmail());
            return JwtAuthenticationResponse.builder().token(token).build();

        } catch (AuthenticationException e) {
            log.error(LoggingConstants.AUTH_FAILED, request.getEmail(), e);
            throw new BadCredentialsException("Invalid credentials");
        }
    }
}
