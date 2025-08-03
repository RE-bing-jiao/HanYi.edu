package edu.HanYi.controller.frontend;

import edu.HanYi.utils.AuthStatusUtils;
import edu.HanYi.dto.response.CourseResponse;
import edu.HanYi.exception.ResourceNotFoundException;
import edu.HanYi.model.User;
import edu.HanYi.repository.UserRepository;
import edu.HanYi.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CoursePageController {
    private final AuthStatusUtils authStatus;
    private final CourseService courseService;
    private final UserRepository userRepository;

    @GetMapping
    public String getUserCourses(@AuthenticationPrincipal UserDetails userDetails,
                                 Model model,
                                 @ModelAttribute("error") String error) {
        authStatus.addAuthStatusToModel(model);

        if (!error.isEmpty()) {
            model.addAttribute("error", error);
        }

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<CourseResponse> courses = courseService.getAllCourses().stream()
                .filter(c -> c.entries().stream()
                        .anyMatch(e -> e.userId().equals(user.getId())))
                .toList();

        model.addAttribute("courses", courses);
        return "courses";
    }
}
