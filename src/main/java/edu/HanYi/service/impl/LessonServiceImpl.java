package edu.HanYi.service.impl;

import edu.HanYi.dto.request.LessonCreateRequest;
import edu.HanYi.dto.response.LessonResponse;
import edu.HanYi.exception.ResourceAlreadyExistsException;
import edu.HanYi.exception.ResourceNotFoundException;
import edu.HanYi.model.*;
import edu.HanYi.repository.CourseRepository;
import edu.HanYi.repository.LessonRepository;
import edu.HanYi.service.LessonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {
    private static final Marker TO_CONSOLE = MarkerFactory.getMarker("TO_CONSOLE");
    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;

    @Override
    @Transactional
    public LessonResponse createLesson(LessonCreateRequest request) {
        log.debug("Checking lesson existence: header={}, courseId={}",
                request.header(), request.courseId());
        if (lessonRepository.existsByHeaderAndCourseId(request.header(), request.courseId())) {
            log.error(TO_CONSOLE, "Lesson already exists: header={}, courseId={}",
                    request.header(), request.courseId());
            throw new ResourceAlreadyExistsException(
                    "Lesson with header '" + request.header() + "' already exists in this course");
        }

        log.debug("Fetching course ID: {}", request.courseId());
        Course course = courseRepository.findById(request.courseId())
                .orElseThrow(() -> {
                    log.error(TO_CONSOLE, "Course not found: ID={}", request.courseId());
                    return new ResourceNotFoundException("Course not found with id: " + request.courseId());
                });

        Lesson lesson = new Lesson();
        lesson.setLessonOrderNum(request.lessonOrderNum());
        lesson.setHeader(request.header());
        lesson.setDescription(request.description());
        lesson.setUrl(request.url());
        lesson.setCourse(course);

        Lesson savedLesson = lessonRepository.save(lesson);
        log.info(TO_CONSOLE, "Created lesson ID: {}, header: {}, courseId: {}",
                savedLesson.getId(), savedLesson.getHeader(), request.courseId());
        return mapToResponse(savedLesson);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LessonResponse> getAllLessons() {
        List<Lesson> lessons = lessonRepository.findAll();
        log.info(TO_CONSOLE, "Fetched {} lessons", lessons.size());
        return lessons.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public LessonResponse getLessonById(Integer id) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> {
                    log.error(TO_CONSOLE, "Lesson not found: ID={}", id);
                    return new ResourceNotFoundException("Lesson not found with id: " + id);
                });
        return mapToResponse(lesson);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LessonResponse> getLessonsByCourseId(Integer courseId) {
        log.debug("Checking course existence ID: {}", courseId);
        if (!courseRepository.existsById(courseId)) {
            log.error(TO_CONSOLE, "Course not found: ID={}", courseId);
            throw new ResourceNotFoundException("Course not found with id: " + courseId);
        }

        List<Lesson> lessons = lessonRepository.findByCourseId(courseId);
        return lessons.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public LessonResponse updateLesson(Integer id, LessonCreateRequest request) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> {
                    log.error(TO_CONSOLE, "Lesson not found: ID={}", id);
                    return new ResourceNotFoundException("Lesson not found with id: " + id);
                });

        if (!lesson.getHeader().equals(request.header()) ||
                !lesson.getCourse().getId().equals(request.courseId())) {
            log.debug("Checking lesson header uniqueness: header={}, courseId={}",
                    request.header(), request.courseId());
            if (lessonRepository.existsByHeaderAndCourseId(request.header(), request.courseId())) {
                log.error(TO_CONSOLE, "Lesson header conflict: header={}, courseId={}",
                        request.header(), request.courseId());
                throw new ResourceAlreadyExistsException(
                        "Lesson with header '" + request.header() + "' already exists in this course");
            }
        }

        Course course = courseRepository.findById(request.courseId())
                .orElseThrow(() -> {
                    log.error(TO_CONSOLE, "Course not found: ID={}", request.courseId());
                    return new ResourceNotFoundException("Course not found with id: " + request.courseId());
                });

        lesson.setLessonOrderNum(request.lessonOrderNum());
        lesson.setHeader(request.header());
        lesson.setDescription(request.description());
        lesson.setUrl(request.url());
        lesson.setCourse(course);

        Lesson updatedLesson = lessonRepository.save(lesson);
        log.info(TO_CONSOLE, "Updated lesson ID: {}", id);
        return mapToResponse(updatedLesson);
    }

    @Override
    @Transactional
    public void deleteLesson(Integer id) {
        log.debug("Checking lesson existence ID: {}", id);
        if (!lessonRepository.existsById(id)) {
            log.error(TO_CONSOLE, "Lesson not found: ID={}", id);
            throw new ResourceNotFoundException("Lesson not found with id: " + id);
        }
        lessonRepository.deleteById(id);
        log.info(TO_CONSOLE, "Deleted lesson ID: {}", id);
    }

    private LessonResponse mapToResponse(Lesson lesson) {
        return new LessonResponse(
                lesson.getId(),
                lesson.getLessonOrderNum(),
                lesson.getHeader(),
                lesson.getDescription(),
                lesson.getUrl(),
                lesson.getCourse().getId(),
                lesson.getCourse().getHeader()
        );
    }
}