package edu.HanYi.service;

import edu.HanYi.dto.request.CourseCreateRequest;
import edu.HanYi.dto.response.CourseResponse;
import edu.HanYi.exception.ResourceAlreadyExistsException;
import edu.HanYi.exception.ResourceNotFoundException;
import edu.HanYi.model.*;
import edu.HanYi.repository.CategoryRepository;
import edu.HanYi.repository.CourseRepository;
import edu.HanYi.service.impl.CourseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CourseServiceImpl courseService;

    private CourseCreateRequest validRequest;
    private Course existingCourse;
    private Category existingCategory;
    private final Integer NON_EXISTING_ID = 10000;

    @BeforeEach
    void setUp() {
        validRequest = new CourseCreateRequest(
                "Header",
                "Desc",
                1,
                LocalDateTime.now(),
                LocalDateTime.now().plusMonths(3),
                BigDecimal.ZERO
        );
        existingCategory = new Category();
        existingCategory.setId(1);
        existingCategory.setName("Name");

        existingCourse = new Course();
        existingCourse.setId(1);
        existingCourse.setHeader(validRequest.header());
        existingCourse.setDescription(validRequest.description());
        existingCourse.setCategory(existingCategory);
        existingCourse.setEntryDate(validRequest.entryDate());
        existingCourse.setExitDate(validRequest.exitDate());
        existingCourse.setProgress(validRequest.progress());
    }

    @Test
    void createCourse_ValidRequest_ShouldCreate() {
        when(courseRepository.existsByHeader(validRequest.header())).thenReturn(false);
        when(categoryRepository.findById(validRequest.categoryId())).thenReturn(Optional.of(existingCategory));
        when(courseRepository.save(any(Course.class))).thenReturn(existingCourse);

        CourseResponse response = courseService.createCourse(validRequest);

        assertNotNull(response);
        assertEquals(validRequest.header(), response.header());
        verify(courseRepository).existsByHeader(validRequest.header());
        verify(courseRepository).save(any(Course.class));
    }

    @Test
    void createCourse_InvalidRequest_ThrowsResourceAlreadyExistsExceptionException() {
        when(courseRepository.existsByHeader(validRequest.header())).thenReturn(true);

        assertThrows(
                ResourceAlreadyExistsException.class,
                () -> courseService.createCourse(validRequest)
        );
        verify(courseRepository).existsByHeader(validRequest.header());
        verify(courseRepository, never()).save(any());
    }

    @Test
    void createCourse_InvalidRequest_ThrowsResourceNotFoundException() {
        when(courseRepository.existsByHeader(validRequest.header())).thenReturn(false);
        when(categoryRepository.findById(validRequest.categoryId())).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> courseService.createCourse(validRequest)
        );
        verify(categoryRepository).findById(validRequest.categoryId());
        verify(courseRepository, never()).save(any());
    }

    @Test
    void getCourseById_ValidRequest_ShouldFind() {
        when(courseRepository.findById(1)).thenReturn(Optional.of(existingCourse));

        CourseResponse response = courseService.getCourseById(1);

        assertEquals(existingCourse.getHeader(), response.header());
        verify(courseRepository).findById(1);
    }

    @Test
    void getCourseById_InvalidRequest_ThrowsResourceNotFoundException() {
        when(courseRepository.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> courseService.getCourseById(NON_EXISTING_ID)
        );
        verify(courseRepository).findById(NON_EXISTING_ID);
    }

    @Test
    void getCoursesByCategoryId_validRequest_ShouldFind() {
        when(categoryRepository.existsById(1)).thenReturn(true);
        when(courseRepository.findByCategoryId(1)).thenReturn(List.of(existingCourse));

        List<CourseResponse> response = courseService.getCoursesByCategoryId(1);

        assertEquals(1, response.size());
        assertEquals(existingCourse.getHeader(), response.get(0).header());
        verify(categoryRepository).existsById(1);
        verify(courseRepository).findByCategoryId(1);
    }

    @Test
    void getCoursesByCategoryId_InvalidRequest_ThrowsResourceNotFoundException() {
        when(categoryRepository.existsById(NON_EXISTING_ID)).thenReturn(false);

        assertThrows(
                ResourceNotFoundException.class,
                () -> courseService.getCoursesByCategoryId(NON_EXISTING_ID)
        );
        verify(categoryRepository).existsById(NON_EXISTING_ID);
        verify(courseRepository, never()).findByCategoryId(any());
    }

    @Test
    void updateCourse_ValidRequest_ShouldUpdate() {
        when(courseRepository.findById(1)).thenReturn(Optional.of(existingCourse));
        when(courseRepository.existsByHeader("New Header")).thenReturn(false);
        when(categoryRepository.findById(1)).thenReturn(Optional.of(existingCategory));
        when(courseRepository.save(any(Course.class))).thenReturn(existingCourse);

        CourseCreateRequest updateRequest = new CourseCreateRequest(
                "New Header",
                "Updated Description",
                1,
                LocalDateTime.now(),
                LocalDateTime.now().plusMonths(4),
                BigDecimal.ZERO
        );

        CourseResponse response = courseService.updateCourse(1, updateRequest);

        assertEquals(updateRequest.header(), response.header());
        verify(courseRepository).save(any(Course.class));
    }

    @Test
    void updateCourse_InvalidRequest_ThrowsResourceAlreadyExistsException() {
        when(courseRepository.findById(1)).thenReturn(Optional.of(existingCourse));
        when(courseRepository.existsByHeader("Existing Header")).thenReturn(true);

        CourseCreateRequest conflictRequest = new CourseCreateRequest(
                "Existing Header",
                "Description",
                1,
                LocalDateTime.now(),
                LocalDateTime.now().plusMonths(3),
                BigDecimal.ZERO
        );

        assertThrows(
                ResourceAlreadyExistsException.class,
                () -> courseService.updateCourse(1, conflictRequest)
        );
        verify(courseRepository, never()).save(any());
    }

    @Test
    void updateCourse_InvalidRequest_ThrowsResourceNotFoundException() {
        when(courseRepository.findById(1)).thenReturn(Optional.of(existingCourse));
        when(courseRepository.existsByHeader("New Header")).thenReturn(false);
        when(categoryRepository.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());

        CourseCreateRequest updateRequest = new CourseCreateRequest(
                "New Header",
                "Description",
                NON_EXISTING_ID,
                LocalDateTime.now(),
                LocalDateTime.now().plusMonths(3),
                BigDecimal.ZERO
        );

        assertThrows(
                ResourceNotFoundException.class,
                () -> courseService.updateCourse(1, updateRequest)
        );
        verify(courseRepository, never()).save(any());
    }

    @Test
    void deleteCourse_ValidRequest_ShouldDelete() {
        when(courseRepository.existsById(1)).thenReturn(true);
        doNothing().when(courseRepository).deleteById(1);

        courseService.deleteCourse(1);
        verify(courseRepository).deleteById(1);
    }

    @Test
    void deleteCourse_InvalidRequest_ThrowsResourceNotFoundException() {
        when(courseRepository.existsById(NON_EXISTING_ID)).thenReturn(false);

        assertThrows(
                ResourceNotFoundException.class,
                () -> courseService.deleteCourse(NON_EXISTING_ID)
        );
        verify(courseRepository, never()).deleteById(any());
    }
}
