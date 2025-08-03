package edu.HanYi.controller.frontend;

import edu.HanYi.exception.ResourceNotFoundException;
import edu.HanYi.utils.AuthStatusUtils;
import edu.HanYi.dto.response.CourseResponse;
import edu.HanYi.service.CategoryService;
import edu.HanYi.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
@Controller
@RequestMapping("/courses-info")
@RequiredArgsConstructor
public class AllCoursesPageController {
    private final AuthStatusUtils authStatus;
    private final CourseService courseService;
    private final CategoryService categoryService;

    @GetMapping
    public String getAllCourses(
            @RequestParam(required = false) Integer categoryId,
            Model model,
            RedirectAttributes redirectAttributes,
            @ModelAttribute("error") String error) {

        authStatus.addAuthStatusToModel(model);

        model.addAttribute("categories", categoryService.getAllCategories());

        if (!error.isEmpty()) {
            model.addAttribute("error", error);
        }

        try {
            List<CourseResponse> courses = (categoryId != null) ?
                    courseService.getCoursesByCategoryId(categoryId) :
                    courseService.getAllCourses();

            model.addAttribute("courses", courses);
            return "courses-info";

        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", "Категория не найдена");
            return "redirect:/courses-info";
        }
    }
}

