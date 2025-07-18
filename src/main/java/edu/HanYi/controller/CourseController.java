package edu.HanYi.controller;

import edu.HanYi.dto.request.CourseCreateRequest;
import edu.HanYi.dto.response.CourseResponse;
import edu.HanYi.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {
    private static final Marker TO_CONSOLE = MarkerFactory.getMarker("TO_CONSOLE");
    private final CourseService courseService;

    @PostMapping
    public ResponseEntity<CourseResponse> createCourse(@Valid @RequestBody CourseCreateRequest request) {
        log.info(TO_CONSOLE, "Creating course with header: {}", request.header());
        CourseResponse response = courseService.createCourse(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CourseResponse>> getAllCourses() {
        log.debug("Fetching all courses");
        List<CourseResponse> courses = courseService.getAllCourses();
        log.info(TO_CONSOLE, "Fetched {} courses", courses.size());
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> getCourseById(@PathVariable Integer id) {
        log.debug("Fetching course by ID: {}", id);
        CourseResponse response = courseService.getCourseById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<CourseResponse>> getCoursesByCategoryId(@PathVariable Integer categoryId) {
        List<CourseResponse> courses = courseService.getCoursesByCategoryId(categoryId);
        log.info(TO_CONSOLE, "Fetched {} courses for category ID: {}", courses.size(), categoryId);
        return ResponseEntity.ok(courses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseResponse> updateCourse(
            @PathVariable Integer id,
            @Valid @RequestBody CourseCreateRequest request) {
        log.info(TO_CONSOLE, "Updating course ID: {}, new header: {}", id, request.header());
        CourseResponse response = courseService.updateCourse(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Integer id) {
        log.info(TO_CONSOLE, "Deleting course ID: {}", id);
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}