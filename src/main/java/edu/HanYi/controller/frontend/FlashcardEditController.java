package edu.HanYi.controller.frontend;

import edu.HanYi.dto.response.FlashcardResponse;
import edu.HanYi.service.FlashcardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/edit-flashcard")
@RequiredArgsConstructor
public class FlashcardEditController {
    private final FlashcardService flashcardService;

    @GetMapping(value="/{id}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String showEditForm(@PathVariable Integer id, Model model) {
        FlashcardResponse flashcard = flashcardService.getFlashcardById(id);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = auth != null && auth.isAuthenticated()
                && !(auth instanceof AnonymousAuthenticationToken);

        model.addAttribute("isAuthenticated", isAuthenticated);
        model.addAttribute("flashcard", flashcard);
        return "edit-flashcard";
    }
}
