package edu.HanYi.controller.frontend;

import edu.HanYi.controller.frontend.utils.AuthStatus;
import edu.HanYi.dto.response.FlashcardResponse;
import edu.HanYi.exception.ResourceNotFoundException;
import edu.HanYi.model.User;
import edu.HanYi.repository.UserRepository;
import edu.HanYi.service.FlashcardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/flashcards")
@RequiredArgsConstructor
public class FlashcardPageController {
    private final FlashcardService flashcardService;
    private final UserRepository userRepository;
    private final AuthStatus authStatus;

    @GetMapping
    public String getUserFlashcards(@AuthenticationPrincipal UserDetails userDetails,
                                    @RequestParam(required = false) String sort,
                                    Model model) {
        try {
            authStatus.addAuthStatusToModel(model);

            User user = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            List<FlashcardResponse> flashcards = new ArrayList<>(
                    flashcardService.getFlashcardsByUserId(user.getId())
            );

            if ("desc".equals(sort)) {
                flashcards.sort((f1, f2) -> f2.createdAt().compareTo(f1.createdAt()));
            } else {
                flashcards.sort(Comparator.comparing(FlashcardResponse::createdAt));
            }

            model.addAttribute("flashcards", flashcards);
            return "flashcards";

        } catch (Exception e) {
            log.error("Error loading flashcards", e);
            model.addAttribute("flashcards", Collections.emptyList());
            model.addAttribute("error", "Ошибка загрузки карточек");
            return "flashcards";
        }
    }

    @DeleteMapping("/delete/{id}")
    public String deleteFlashcard(@PathVariable Integer id,
                                  @AuthenticationPrincipal UserDetails userDetails,
                                  RedirectAttributes redirectAttributes) {
        try {
            userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            flashcardService.deleteFlashcard(id);
            redirectAttributes.addFlashAttribute("success", "Карточка удалена");
        } catch (Exception e) {
            log.error("Error deleting flashcard", e);
            redirectAttributes.addFlashAttribute("error", "Ошибка при удалении карточки");
        }
        return "redirect:/flashcards";
    }
}
