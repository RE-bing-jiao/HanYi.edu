package edu.HanYi.controller.frontend;

import edu.HanYi.utils.AuthStatusUtils;
import edu.HanYi.dto.response.LessonResponse;
import edu.HanYi.exception.ResourceNotFoundException;
import edu.HanYi.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class LessonPageController {
    private final LessonService lessonService;
    private final AuthStatusUtils authStatus;

    @GetMapping("/lesson/{orderNum}/course/{courseId}")
    public String getLesson(@PathVariable Integer orderNum,
                            @PathVariable Integer courseId,
                            Model model,
                            RedirectAttributes redirectAttributes) { 
        try {
            authStatus.addAuthStatusToModel(model);

            List<LessonResponse> allLessons = lessonService.getLessonsByCourseId(courseId);

            LessonResponse lesson = allLessons.stream()
                    .filter(l -> l.lessonOrderNum().equals(orderNum))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Урок не найден"));

            boolean isLastLesson = orderNum.equals(allLessons.get(allLessons.size() - 1).lessonOrderNum());

            model.addAttribute("isLastLesson", isLastLesson);
            model.addAttribute("lesson", lesson);
            return "lesson";

        } catch (ResourceNotFoundException ex) {
            redirectAttributes.addFlashAttribute("error", "Урок не найден");
            return "redirect:/courses";
        }
    }
}
