package edu.HanYi.controller.frontend;

import edu.HanYi.controller.frontend.utils.AuthStatus;
import edu.HanYi.dto.response.CategoryResponse;
import edu.HanYi.dto.response.CourseResponse;
import edu.HanYi.service.CategoryService;
import edu.HanYi.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/courses-info")
@RequiredArgsConstructor
public class AllCoursesPageController {
    private final AuthStatus authStatus;
    private final CourseService courseService;
    private final CategoryService categoryService;

    @GetMapping
    public String getAllCourses(
            @RequestParam(required = false) Integer categoryId,
            Model model) {

        authStatus.addAuthStatusToModel(model);

        List<CategoryResponse> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);

        List<CourseResponse> courses;
        if (categoryId != null) {
            courses = courseService.getCoursesByCategoryId(categoryId);
        } else {
            courses = courseService.getAllCourses();
        }

        model.addAttribute("courses", courses);
        return "courses-info";
    }
}
