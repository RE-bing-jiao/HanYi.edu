package edu.HanYi.service.impl;

import edu.HanYi.dto.request.CourseCreateRequest;
import edu.HanYi.dto.response.CourseResponse;
import edu.HanYi.exception.ResourceAlreadyExistsException;
import edu.HanYi.exception.ResourceNotFoundException;
import edu.HanYi.model.*;
import edu.HanYi.repository.CategoryRepository;
import edu.HanYi.repository.CourseRepository;
import edu.HanYi.service.CourseService;
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
public class CourseServiceImpl implements CourseService {
    private static final Marker TO_CONSOLE = MarkerFactory.getMarker("TO_CONSOLE");
    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public CourseResponse createCourse(CourseCreateRequest request) {
        log.debug("Checking if course exists with header: {}", request.header());
        if (courseRepository.existsByHeader(request.header())) {
            log.error(TO_CONSOLE, "Course already exists with header: {}", request.header());
            throw new ResourceAlreadyExistsException("Course with header '" + request.header() + "' already exists");
        }

        log.debug("Fetching category with ID: {}", request.categoryId());
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> {
                    log.error(TO_CONSOLE, "Category not found with ID: {}", request.categoryId());
                    return new ResourceNotFoundException("Category not found with id: " + request.categoryId());
                });

        log.debug("Creating new course: {}", request.header());
        Course course = new Course();
        course.setHeader(request.header());
        course.setDescription(request.description());
        course.setCategory(category);
        course.setEntryDate(request.entryDate());
        course.setExitDate(request.exitDate());
        course.setProgress(request.progress());

        Course savedCourse = courseRepository.save(course);
        log.info(TO_CONSOLE, "Created course ID: {}, header: {}", savedCourse.getId(), savedCourse.getHeader());
        return mapToResponse(savedCourse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CourseResponse getCourseById(Integer id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> {
                    log.error(TO_CONSOLE, "Course not found with ID: {}", id);
                    return new ResourceNotFoundException("Course not found with id: " + id);
                });
        log.debug("Found course: {}", course.getHeader());
        return mapToResponse(course);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> getCoursesByCategoryId(Integer categoryId) {
        log.debug("Fetching courses for category ID: {}", categoryId);
        if (!categoryRepository.existsById(categoryId)) {
            log.error(TO_CONSOLE, "Category not found with ID: {}", categoryId);
            throw new ResourceNotFoundException("Category not found with id: " + categoryId);
        }

        log.debug("Category exists, fetching courses");
        List<Course> courses = courseRepository.findByCategoryId(categoryId);
        log.info(TO_CONSOLE, "Found {} courses for category ID: {}", courses.size(), categoryId);
        return courses.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public CourseResponse updateCourse(Integer id, CourseCreateRequest request) {
        log.debug("Fetching course for update ID: {}", id);
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> {
                    log.error(TO_CONSOLE, "Update failed - course not found with ID: {}", id);
                    return new ResourceNotFoundException("Course not found with id: " + id);
                });

        if (!course.getHeader().equals(request.header())) {
            log.debug("Checking header availability: {}", request.header());
            if (courseRepository.existsByHeader(request.header())) {
                log.error(TO_CONSOLE, "Update failed - header already exists: {}", request.header());
                throw new ResourceAlreadyExistsException("Course with header '" + request.header() + "' already exists");
            }
            course.setHeader(request.header());
        }

        log.debug("Fetching category with ID: {}", request.categoryId());
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> {
                    log.error(TO_CONSOLE, "Update failed - category not found with ID: {}", request.categoryId());
                    return new ResourceNotFoundException("Category not found with id: " + request.categoryId());
                });

        course.setDescription(request.description());
        course.setCategory(category);
        course.setEntryDate(request.entryDate());
        course.setExitDate(request.exitDate());
        course.setProgress(request.progress());

        Course updatedCourse = courseRepository.save(course);
        log.info(TO_CONSOLE, "Updated course ID: {}, new header: {}", id, request.header());
        return mapToResponse(updatedCourse);
    }

    @Override
    @Transactional
    public void deleteCourse(Integer id) {
        if (!courseRepository.existsById(id)) {
            log.error(TO_CONSOLE, "Delete failed - course not found with ID: {}", id);
            throw new ResourceNotFoundException("Course not found with id: " + id);
        }
        courseRepository.deleteById(id);
        log.info(TO_CONSOLE, "Deleted course ID: {}", id);
    }

    private CourseResponse mapToResponse(Course course) {
        return new CourseResponse(
                course.getId(),
                course.getHeader(),
                course.getDescription(),
                course.getCategory().getId(),
                course.getCategory().getName(),
                course.getEntryDate(),
                course.getExitDate(),
                course.getProgress(),
                course.getLessons().stream()
                        .map(lesson -> new CourseResponse.LessonResponse(
                                lesson.getId(),
                                lesson.getLessonOrderNum(),
                                lesson.getHeader()
                        ))
                        .toList(),
                course.getEntries().stream()
                        .map(entry -> new CourseResponse.EntryResponse(
                                entry.getId(),
                                entry.getEntryDate(),
                                entry.getUser().getId()
                        ))
                        .toList()
        );
    }
}