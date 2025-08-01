package edu.HanYi.controller.frontend;

import edu.HanYi.controller.frontend.utils.AuthStatus;
import edu.HanYi.dto.response.LessonResponse;
import edu.HanYi.exception.ResourceNotFoundException;
import edu.HanYi.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class LessonPageController {
    private final LessonService lessonService;
    private final AuthStatus authStatus;

    @GetMapping("/lesson/{orderNum}/course/{courseId}")
    public String getLesson(@PathVariable Integer orderNum,
                            @PathVariable Integer courseId,
                            Model model) {
        authStatus.addAuthStatusToModel(model);
        LessonResponse lesson = lessonService.getLessonsByCourseId(courseId).stream()
                .filter(l -> l.lessonOrderNum().equals(orderNum))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));

        List<LessonResponse> allLessons = lessonService.getLessonsByCourseId(courseId);
        boolean isLastLesson = orderNum.equals(allLessons.get(allLessons.size() - 1).lessonOrderNum());

        model.addAttribute("isLastLesson", isLastLesson);
        model.addAttribute("lesson", lesson);
        return "lesson";
    }
}
