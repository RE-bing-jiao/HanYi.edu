package edu.HanYi.controller;

import edu.HanYi.dto.request.LessonCreateRequest;
import edu.HanYi.dto.response.LessonResponse;
import edu.HanYi.service.LessonService;
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
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
public class LessonController {
    private static final Marker TO_CONSOLE = MarkerFactory.getMarker("TO_CONSOLE");
    private final LessonService lessonService;

    @PostMapping
    public ResponseEntity<LessonResponse> createLesson(@Valid @RequestBody LessonCreateRequest request) {
        log.info(TO_CONSOLE, "Creating lesson for course ID: {}", request.courseId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(lessonService.createLesson(request));
    }

    @GetMapping
    public ResponseEntity<List<LessonResponse>> getAllLessons() {
        log.debug("Fetching all lessons");
        return ResponseEntity.ok(lessonService.getAllLessons());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LessonResponse> getLessonById(@PathVariable Integer id) {
        log.debug("Fetching lesson ID: {}", id);
        return ResponseEntity.ok(lessonService.getLessonById(id));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<LessonResponse>> getLessonsByCourseId(@PathVariable Integer courseId) {
        log.debug("Fetching lessons for course ID: {}", courseId);
        return ResponseEntity.ok(lessonService.getLessonsByCourseId(courseId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LessonResponse> updateLesson(
            @PathVariable Integer id,
            @Valid @RequestBody LessonCreateRequest request) {
        log.info(TO_CONSOLE, "Updating lesson ID: {}", id);
        return ResponseEntity.ok(lessonService.updateLesson(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Integer id) {
        log.info(TO_CONSOLE, "Deleting lesson ID: {}", id);
        lessonService.deleteLesson(id);
        return ResponseEntity.noContent().build();
    }
}