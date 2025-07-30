package edu.HanYi.controller.frontend;

import edu.HanYi.dto.response.LessonResponse;
import edu.HanYi.exception.ResourceNotFoundException;
import edu.HanYi.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller("frontendLessonController")
@RequiredArgsConstructor
public class LessonController {
    private final LessonService lessonService;

    @GetMapping("/lesson/{orderNum}/course{courseId}")
    public String getLesson(@PathVariable Integer orderNum,
                            @PathVariable Integer courseId,
                            Model model,
                            @CookieValue(name = "JWT", required = false) String token) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = auth != null && auth.isAuthenticated()
                && !(auth instanceof AnonymousAuthenticationToken);
        model.addAttribute("isAuthenticated", isAuthenticated);
        LessonResponse lesson = lessonService.getLessonsByCourseId(courseId).stream()
                .filter(l -> l.lessonOrderNum().equals(orderNum))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));

        model.addAttribute("lesson", lesson);
        return "lesson";
    }
}
