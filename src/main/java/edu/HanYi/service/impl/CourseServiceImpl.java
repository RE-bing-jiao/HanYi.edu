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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public CourseResponse createCourse(CourseCreateRequest request) {
        if (courseRepository.existsByHeader(request.header())) {
            throw new ResourceAlreadyExistsException("Course with header '" + request.header() + "' already exists");
        }

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.categoryId()));

        Course course = new Course();
        course.setHeader(request.header());
        course.setDescription(request.description());
        course.setCategory(category);
        course.setEntryDate(request.entryDate());
        course.setExitDate(request.exitDate());
        course.setProgress(request.progress());

        Course savedCourse = courseRepository.save(course);
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
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
        return mapToResponse(course);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> getCoursesByCategoryId(Integer categoryId) {
        return courseRepository.findByCategoryId(categoryId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public CourseResponse updateCourse(Integer id, CourseCreateRequest request) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));

        if (!course.getHeader().equals(request.header())) {
            if (courseRepository.existsByHeader(request.header())) {
                throw new ResourceAlreadyExistsException("Course with header '" + request.header() + "' already exists");
            }
            course.setHeader(request.header());
        }

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.categoryId()));

        course.setDescription(request.description());
        course.setCategory(category);
        course.setEntryDate(request.entryDate());
        course.setExitDate(request.exitDate());
        course.setProgress(request.progress());

        Course updatedCourse = courseRepository.save(course);
        return mapToResponse(updatedCourse);
    }

    @Override
    @Transactional
    public void deleteCourse(Integer id) {
        if (!courseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Course not found with id: " + id);
        }
        courseRepository.deleteById(id);
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