package edu.HanYi.controller;

import edu.HanYi.constants.LoggingConstants;
import edu.HanYi.dto.request.CourseCreateRequest;
import edu.HanYi.dto.response.CourseResponse;
import edu.HanYi.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {
    private static final Marker TO_CONSOLE = LoggingConstants.TO_CONSOLE;
    private final CourseService courseService;

    @PostMapping
    public ResponseEntity<CourseResponse> createCourse(@Valid @RequestBody CourseCreateRequest request) {
        log.info(TO_CONSOLE, LoggingConstants.COURSE_CREATE, request.header());
        CourseResponse response = courseService.createCourse(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CourseResponse>> getAllCourses() {
        log.debug(LoggingConstants.COURSE_FETCH);
        List<CourseResponse> courses = courseService.getAllCourses();
        log.info(TO_CONSOLE, LoggingConstants.COURSES_FETCHED, courses.size());
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> getCourseById(@PathVariable Integer id) {
        log.debug(LoggingConstants.COURSE_FETCH, id);
        CourseResponse response = courseService.getCourseById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<CourseResponse>> getCoursesByCategoryId(@PathVariable Integer categoryId) {
        List<CourseResponse> courses = courseService.getCoursesByCategoryId(categoryId);
        log.info(TO_CONSOLE, LoggingConstants.COURSES_BY_CATEGORY, courses.size(), categoryId);
        return ResponseEntity.ok(courses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseResponse> updateCourse(
            @PathVariable Integer id,
            @Valid @RequestBody CourseCreateRequest request) {
        log.info(TO_CONSOLE, LoggingConstants.COURSE_UPDATE, id, request.header());
        CourseResponse response = courseService.updateCourse(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Integer id) {
        log.info(TO_CONSOLE, LoggingConstants.COURSE_DELETE, id);
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}