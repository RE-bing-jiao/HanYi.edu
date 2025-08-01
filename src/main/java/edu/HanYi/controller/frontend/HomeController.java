package edu.HanYi.controller.frontend;

import edu.HanYi.controller.frontend.utils.AuthStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class HomeController {
    private final AuthStatus authStatus;

    @GetMapping("/signin")
    public String showSignInForm() {
        return "signin";
    }

    @GetMapping("/signup")
    public String showSignUpForm() {
        return "signup";
    }

    @GetMapping("/home")
    public String home(Model model) {
       authStatus.addAuthStatusToModel(model);
       return "home";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminPanel(){
        return "redirect:/swagger-ui.html";
    }
}
