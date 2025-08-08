package edu.HanYi.controller;

import edu.HanYi.constants.LoggingConstants;
import edu.HanYi.dto.request.LessonCreateRequest;
import edu.HanYi.dto.response.LessonResponse;
import edu.HanYi.service.LessonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
@RestController
public class LessonController {
    private static final Marker TO_CONSOLE = LoggingConstants.TO_CONSOLE;
    private final LessonService lessonService;

    @PostMapping
    public ResponseEntity<LessonResponse> createLesson(@Valid @RequestBody LessonCreateRequest request) {
        log.info(TO_CONSOLE, LoggingConstants.LESSON_CREATE, request.courseId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(lessonService.createLesson(request));
    }

    @GetMapping
    public ResponseEntity<List<LessonResponse>> getAllLessons() {
        log.debug(LoggingConstants.LESSON_FETCH);
        return ResponseEntity.ok(lessonService.getAllLessons());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LessonResponse> getLessonById(@PathVariable Integer id) {
        log.debug(LoggingConstants.LESSON_FETCH, id);
        return ResponseEntity.ok(lessonService.getLessonById(id));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<LessonResponse>> getLessonsByCourseId(@PathVariable Integer courseId) {
        log.debug(LoggingConstants.LESSONS_BY_COURSE, courseId);
        return ResponseEntity.ok(lessonService.getLessonsByCourseId(courseId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LessonResponse> updateLesson(
            @PathVariable Integer id,
            @Valid @RequestBody LessonCreateRequest request) {
        log.info(TO_CONSOLE, LoggingConstants.LESSON_UPDATE, id);
        return ResponseEntity.ok(lessonService.updateLesson(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Integer id) {
        log.info(TO_CONSOLE, LoggingConstants.LESSON_DELETE, id);
        lessonService.deleteLesson(id);
        return ResponseEntity.noContent().build();
    }
}