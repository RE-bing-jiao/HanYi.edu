package edu.HanYi.controller.frontend;

import edu.HanYi.dto.request.FlashcardCreateRequest;
import edu.HanYi.dto.response.CourseResponse;
import edu.HanYi.dto.response.FlashcardResponse;
import edu.HanYi.dto.response.LessonResponse;
import edu.HanYi.exception.ResourceNotFoundException;
import edu.HanYi.model.User;
import edu.HanYi.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;

import edu.HanYi.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserAccountController {
    private final FlashcardService flashcardService;
    private final CourseService courseService;
    private final UserRepository userRepository;
    private final LessonService lessonService;

    @GetMapping("/user-account")
    public String getUserAccount(@AuthenticationPrincipal UserDetails userDetails,
                                 Model model,
                                 @RequestParam(required = false) String sort) {
        boolean isAuthenticated = userDetails != null;
        model.addAttribute("isAuthenticated", isAuthenticated);

        log.info("Loading user account page");
        try {
            if (!isAuthenticated) {
                throw new ResourceNotFoundException("User not authenticated");
            }

            String email = userDetails.getUsername();
            log.info("Authenticated user email: {}", email);

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            log.info("Found user: {}", user.getEmail());

            List<FlashcardResponse> flashcards = new ArrayList<>(
                    flashcardService.getFlashcardsByUserId(user.getId())
            );

            if (sort != null) {
                List<FlashcardResponse> mutableFlashcards = new ArrayList<>(flashcards);
                if ("desc".equals(sort)) {
                    mutableFlashcards.sort((f1, f2) -> f2.createdAt().compareTo(f1.createdAt()));
                } else {
                    mutableFlashcards.sort(Comparator.comparing(FlashcardResponse::createdAt));
                }
                model.addAttribute("flashcards", mutableFlashcards);
            } else {
                model.addAttribute("flashcards", flashcards);
            }
            log.info("Found {} flashcards", flashcards.size());

            List<CourseResponse> courses = courseService.getAllCourses().stream()
                    .filter(c -> c.entries().stream()
                            .anyMatch(e -> e.userId().equals(user.getId())))
                    .map(c -> {
                        List<LessonResponse> lessons = lessonService.getLessonsByCourseId(c.id());
                        return new CourseResponse(
                                c.id(),
                                c.header(),
                                c.description(),
                                c.categoryId(),
                                c.categoryName(),
                                c.entryDate(),
                                c.exitDate(),
                                c.progress(),
                                lessons.stream()
                                        .map(l -> new CourseResponse.LessonResponse(
                                                l.id(),
                                                l.lessonOrderNum(),
                                                l.header()))
                                        .toList(),
                                c.entries()
                        );
                    })
                    .toList();

            log.info("Total courses found: {}", courses.size());
            model.addAttribute("flashcards", flashcards);
            model.addAttribute("courses", courses);
            model.addAttribute("user", user);

        } catch (Exception e) {
            log.error("Error loading user account", e);
            model.addAttribute("error", "Error processing data");
            model.addAttribute("flashcards", Collections.emptyList());
            model.addAttribute("courses", Collections.emptyList());
            model.addAttribute("user", null);
        }
        return "user-account";
    }

    @PostMapping("/create")
    public String createFlashcardFromForm(@RequestParam String frontText,
                                          @RequestParam String backText,
                                          @RequestParam Integer userId,
                                          RedirectAttributes redirectAttributes) {
        FlashcardCreateRequest request = new FlashcardCreateRequest(userId, frontText, backText);
        flashcardService.createFlashcard(request);
        redirectAttributes.addFlashAttribute("success", "Карточка создана");
        return "redirect:/user-account";
    }

    @PostMapping("/delete/{id}")
    public String deleteFlashcardFromForm(@PathVariable Integer id,
                                          RedirectAttributes redirectAttributes) {
        flashcardService.deleteFlashcard(id);
        redirectAttributes.addFlashAttribute("success", "Карточка удалена");
        return "redirect:/user-account";
    }

    @PostMapping("/update-flashcard/{id}")
    public String updateFlashcardFromForm(@PathVariable Integer id,
                                          @RequestParam String frontText,
                                          @RequestParam String backText,
                                          @RequestParam Integer userId,
                                          RedirectAttributes redirectAttributes) {
        FlashcardCreateRequest request = new FlashcardCreateRequest(userId, frontText, backText);
        flashcardService.updateFlashcard(id, request);
        redirectAttributes.addFlashAttribute("success", "Карточка обновлена");
        return "redirect:/user-account";
    }
}
