package edu.HanYi.controller.frontend;

import edu.HanYi.controller.frontend.utils.AuthStatus;
import edu.HanYi.dto.request.FlashcardCreateRequest;
import edu.HanYi.dto.response.FlashcardResponse;
import edu.HanYi.exception.ResourceNotFoundException;
import edu.HanYi.model.User;
import edu.HanYi.repository.UserRepository;
import edu.HanYi.service.FlashcardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/edit-flashcard")
@RequiredArgsConstructor
public class FlashcardEditController {
    private final FlashcardService flashcardService;
    private final UserRepository userRepository;
    private final AuthStatus authStatus;

    @GetMapping("/{id}")
    public String showEditForm(@PathVariable Integer id,
                               @AuthenticationPrincipal UserDetails userDetails,
                               Model model) {
        authStatus.addAuthStatusToModel(model);
        userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        FlashcardResponse flashcard = flashcardService.getFlashcardById(id);

        model.addAttribute("flashcard", flashcard);
        return "edit-flashcard";
    }

    @GetMapping("/new")
    public String showCreateForm(@AuthenticationPrincipal UserDetails userDetails,
                                 Model model) {
        authStatus.addAuthStatusToModel(model);
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        model.addAttribute("flashcard", new FlashcardResponse(
                0,
                user.getId(),
                user.getUsername(),
                "",
                "",
                LocalDateTime.now()
        ));
        return "edit-flashcard";
    }

    @PostMapping("/process-form")
    public String processFlashcardForm(
            @RequestParam Integer id,
            @RequestParam String frontText,
            @RequestParam String backText,
            @RequestParam Integer userId,
            RedirectAttributes redirectAttributes) {

        FlashcardCreateRequest request = new FlashcardCreateRequest(
                userId, frontText, backText
        );

        try {
            if (id == 0) {
                flashcardService.createFlashcard(request);
                redirectAttributes.addFlashAttribute("success", "Карточка успешно создана");
            } else {
                flashcardService.updateFlashcard(id, request);
                redirectAttributes.addFlashAttribute("success", "Карточка успешно обновлена");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при сохранении карточки");
        }

        return "redirect:/flashcards";
    }
}

