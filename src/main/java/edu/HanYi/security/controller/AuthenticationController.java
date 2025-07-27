package edu.HanYi.security.controller;

import edu.HanYi.security.request.SignInRequest;
import edu.HanYi.security.request.SignUpRequest;
import edu.HanYi.security.response.JwtAuthenticationResponse;
import edu.HanYi.security.service.impl.AuthenticationServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationServiceImpl authenticationService;

    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String processSignUpForm(SignUpRequest request) {
        authenticationService.signup(request);
        return "redirect:/home";
    }

    @PostMapping(value = "/signin", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String processSignInForm(SignInRequest request, HttpServletResponse response) {
        JwtAuthenticationResponse authResponse = authenticationService.signin(request);

        ResponseCookie jwtCookie = ResponseCookie.from("JWT", authResponse.getToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(24 * 60 * 60)
                .build();

        response.addHeader("Set-Cookie", jwtCookie.toString());
        log.info("JWT cookie set for user: {}", request.getEmail());
        return "redirect:/home";
    }
}