package edu.HanYi.service;

import edu.HanYi.dto.request.LessonCreateRequest;
import edu.HanYi.dto.response.LessonResponse;
import edu.HanYi.exception.ResourceAlreadyExistsException;
import edu.HanYi.exception.ResourceNotFoundException;
import edu.HanYi.model.*;
import edu.HanYi.repository.CourseRepository;
import edu.HanYi.repository.LessonRepository;
import edu.HanYi.service.impl.LessonServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LessonServiceImplTest {

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private LessonServiceImpl lessonService;

    private LessonCreateRequest validRequest;
    private Lesson existingLesson;
    private Course existingCourse;
    private final Integer NON_EXISTING_ID = 10000;

    @BeforeEach
    void setUp() {
        validRequest = new LessonCreateRequest(
                1,
                "Lesson Header",
                "Lesson Description",
                "http://youtube.com",
                1
        );

        existingCourse = new Course();
        existingCourse.setId(1);
        existingCourse.setHeader("Course Header");

        existingLesson = new Lesson();
        existingLesson.setId(1);
        existingLesson.setLessonOrderNum(1);
        existingLesson.setHeader("Lesson Header");
        existingLesson.setDescription("Lesson Description");
        existingLesson.setUrl("http://youtube.com");
        existingLesson.setCourse(existingCourse);
    }

    @Test
    void createLesson_ValidRequest_ShouldCreate() {
        when(lessonRepository.existsByHeaderAndCourseId(validRequest.header(), validRequest.courseId()))
                .thenReturn(false);
        when(courseRepository.findById(validRequest.courseId())).thenReturn(Optional.of(existingCourse));
        when(lessonRepository.save(any(Lesson.class))).thenReturn(existingLesson);

        LessonResponse response = lessonService.createLesson(validRequest);

        assertNotNull(response);
        assertEquals(existingCourse.getId(), response.courseId());
        verify(lessonRepository).existsByHeaderAndCourseId(validRequest.header(), validRequest.courseId());
        verify(lessonRepository).save(any(Lesson.class));
    }

    @Test
    void createLesson_InvalidRequest_ThrowsResourceAlreadyExistsException() {
        when(lessonRepository.existsByHeaderAndCourseId(validRequest.header(), validRequest.courseId()))
                .thenReturn(true);

        assertThrows(
                ResourceAlreadyExistsException.class,
                () -> lessonService.createLesson(validRequest)
        );
        verify(lessonRepository).existsByHeaderAndCourseId(validRequest.header(), validRequest.courseId());
        verify(lessonRepository, never()).save(any());
    }

    @Test
    void createLesson_InvalidRequest_ThrowsResourceNotFoundException() {
        when(lessonRepository.existsByHeaderAndCourseId(validRequest.header(), validRequest.courseId()))
                .thenReturn(false);
        when(courseRepository.findById(validRequest.courseId())).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> lessonService.createLesson(validRequest)
        );
        verify(courseRepository).findById(validRequest.courseId());
        verify(lessonRepository, never()).save(any());
    }

    @Test
    void getLessonById_ValidRequest_ShouldFind() {
        when(lessonRepository.findById(1)).thenReturn(Optional.of(existingLesson));

        LessonResponse response = lessonService.getLessonById(1);

        assertEquals(existingLesson.getId(), response.id());
        verify(lessonRepository).findById(1);
    }

    @Test
    void getLessonById_InvalidRequest_ThrowsResourceNotFoundException() {
        when(lessonRepository.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> lessonService.getLessonById(NON_EXISTING_ID)
        );
        verify(lessonRepository).findById(NON_EXISTING_ID);
    }

    @Test
    void getLessonsByCourseId_ValidRequest_ShouldFind() {
        when(courseRepository.existsById(1)).thenReturn(true);
        when(lessonRepository.findByCourseId(1)).thenReturn(List.of(existingLesson));

        List<LessonResponse> response = lessonService.getLessonsByCourseId(1);

        assertEquals(1, response.size());
        assertEquals(existingLesson.getId(), response.get(0).id());
        verify(courseRepository).existsById(1);
        verify(lessonRepository).findByCourseId(1);
    }

    @Test
    void getLessonsByCourseId_InvalidRequest_ThrowsResourceNotFoundException() {
        when(courseRepository.existsById(NON_EXISTING_ID)).thenReturn(false);

        assertThrows(
                ResourceNotFoundException.class,
                () -> lessonService.getLessonsByCourseId(NON_EXISTING_ID)
        );
        verify(courseRepository).existsById(NON_EXISTING_ID);
        verify(lessonRepository, never()).findByCourseId(any());
    }

    @Test
    void updateLesson_ValidRequest_ShouldUpdate() {
        when(lessonRepository.findById(1)).thenReturn(Optional.of(existingLesson));
        when(courseRepository.findById(validRequest.courseId())).thenReturn(Optional.of(existingCourse));
        when(lessonRepository.save(any(Lesson.class))).thenReturn(existingLesson);

        LessonResponse response = lessonService.updateLesson(1, validRequest);

        assertEquals(existingLesson.getId(), response.id());
        verify(lessonRepository).save(any(Lesson.class));
    }

    @Test
    void updateLesson_InvalidRequest_ThrowsResourceNotFoundExceptionForLesson() {
        when(lessonRepository.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> lessonService.updateLesson(NON_EXISTING_ID, validRequest)
        );
        verify(lessonRepository).findById(NON_EXISTING_ID);
        verify(lessonRepository, never()).save(any());
    }

    @Test
    void updateLesson_InvalidRequest_ThrowsResourceNotFoundExceptionForCourse() {
        when(lessonRepository.findById(1)).thenReturn(Optional.of(existingLesson));
        when(courseRepository.findById(validRequest.courseId())).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> lessonService.updateLesson(1, validRequest)
        );
        verify(courseRepository).findById(validRequest.courseId());
        verify(lessonRepository, never()).save(any());
    }

    @Test
    void deleteLesson_ValidRequest_ShouldDelete() {
        when(lessonRepository.existsById(1)).thenReturn(true);
        doNothing().when(lessonRepository).deleteById(1);

        lessonService.deleteLesson(1);
        verify(lessonRepository).deleteById(1);
    }

    @Test
    void deleteLesson_InvalidRequest_ThrowsResourceNotFoundException() {
        when(lessonRepository.existsById(NON_EXISTING_ID)).thenReturn(false);

        assertThrows(
                ResourceNotFoundException.class,
                () -> lessonService.deleteLesson(NON_EXISTING_ID)
        );
        verify(lessonRepository, never()).deleteById(any());
    }
}