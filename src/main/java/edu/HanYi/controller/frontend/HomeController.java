package edu.HanYi.controller.frontend;

import edu.HanYi.utils.AuthStatusUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@RequiredArgsConstructor
@Controller
public class HomeController {
    private final AuthStatusUtils authStatus;

    @GetMapping("/signin")
    public String showSignInForm(Model model, @ModelAttribute("error") String error) {
        if (!error.isEmpty()) {
            model.addAttribute("error", error);
        }
        return "signin";
    }

    @GetMapping("/signup")
    public String showSignUpForm(Model model, @ModelAttribute("error") String error) {
        if (!error.isEmpty()) {
            model.addAttribute("error", error);
        }
        return "signup";
    }

    @GetMapping("/home")
    public String home(Model model, @ModelAttribute("success") String success) {
        authStatus.addAuthStatusToModel(model);
        if (!success.isEmpty()) {
            model.addAttribute("success", success);
        }
        return "home";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminPanel() {
        return "redirect:/swagger-ui.html";
    }
}
