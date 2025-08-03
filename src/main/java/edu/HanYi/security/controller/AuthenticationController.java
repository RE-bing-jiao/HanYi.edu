package edu.HanYi.security.controller;

import edu.HanYi.exception.ResourceAlreadyExistsException;
import edu.HanYi.security.request.SignInRequest;
import edu.HanYi.security.request.SignUpRequest;
import edu.HanYi.security.response.JwtAuthenticationResponse;
import edu.HanYi.security.service.impl.AuthenticationServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationServiceImpl authenticationService;

    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String processSignUpForm(SignUpRequest request,
                                    RedirectAttributes redirectAttributes) {
        try {
            authenticationService.signup(request);
            redirectAttributes.addFlashAttribute("success", "Регистрация прошла успешно!");
            return "redirect:/home";
        } catch (ResourceAlreadyExistsException e) {
            log.error("Signup error for email: {}", request.getEmail(), e);
            redirectAttributes.addFlashAttribute("error", "Пользователь с таким email уже существует");
            return "redirect:/signup";
        } catch (Exception e) {
            log.error("Unexpected signup error", e);
            redirectAttributes.addFlashAttribute("error", "Ошибка регистрации");
            return "redirect:/signup";
        }
    }

    @PostMapping(value = "/signin", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String processSignInForm(SignInRequest request,
                                    HttpServletResponse response,
                                    RedirectAttributes redirectAttributes) {
        try {
            JwtAuthenticationResponse authResponse = authenticationService.signin(request);

            ResponseCookie jwtCookie = ResponseCookie.from("JWT", authResponse.getToken())
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .maxAge(6 * 60 * 60)
                    .build();

            response.addHeader("Set-Cookie", jwtCookie.toString());
            log.info("JWT cookie set for user: {}", request.getEmail());

            redirectAttributes.addFlashAttribute("success", "Вход выполнен успешно");
            return "redirect:/home";

        } catch (BadCredentialsException e) {
            log.warn("Invalid login attempt for email: {}", request.getEmail());
            redirectAttributes.addFlashAttribute("error", "Неверный email или пароль");
            return "redirect:/signin";
        } catch (Exception e) {
            log.error("Login error", e);
            redirectAttributes.addFlashAttribute("error", "Ошибка входа");
            return "redirect:/signin";
        }
    }
}