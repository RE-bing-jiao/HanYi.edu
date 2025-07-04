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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;

    @Override
    @Transactional
    public LessonResponse createLesson(LessonCreateRequest request) {
        if (lessonRepository.existsByHeaderAndCourseId(request.header(), request.courseId())) {
            throw new ResourceAlreadyExistsException("Lesson with header '" + request.header() + "' already exists in this course");
        }

        Course course = courseRepository.findById(request.courseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + request.courseId()));

        Lesson lesson = new Lesson();
        lesson.setLessonOrderNum(request.lessonOrderNum());
        lesson.setHeader(request.header());
        lesson.setDescription(request.description());
        lesson.setUrl(request.url());
        lesson.setCourse(course);

        Lesson savedLesson = lessonRepository.save(lesson);
        return mapToResponse(savedLesson);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LessonResponse> getAllLessons() {
        return lessonRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public LessonResponse getLessonById(Integer id) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found with id: " + id));
        return mapToResponse(lesson);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LessonResponse> getLessonsByCourseId(Integer courseId) {
        return lessonRepository.findByCourseId(courseId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public LessonResponse updateLesson(Integer id, LessonCreateRequest request) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found with id: " + id));

        if (!lesson.getHeader().equals(request.header()) ||
                !lesson.getCourse().getId().equals(request.courseId())) {
            if (lessonRepository.existsByHeaderAndCourseId(request.header(), request.courseId())) {
                throw new ResourceAlreadyExistsException("Lesson with header '" + request.header() + "' already exists in this course");
            }
        }

        Course course = courseRepository.findById(request.courseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + request.courseId()));

        lesson.setLessonOrderNum(request.lessonOrderNum());
        lesson.setHeader(request.header());
        lesson.setDescription(request.description());
        lesson.setUrl(request.url());
        lesson.setCourse(course);

        Lesson updatedLesson = lessonRepository.save(lesson);
        return mapToResponse(updatedLesson);
    }

    @Override
    @Transactional
    public void deleteLesson(Integer id) {
        if (!lessonRepository.existsById(id)) {
            throw new ResourceNotFoundException("Lesson not found with id: " + id);
        }
        lessonRepository.deleteById(id);
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
