package edu.HanYi.controller;

import edu.HanYi.dto.request.CourseCreateRequest;
import edu.HanYi.dto.response.CourseResponse;
import edu.HanYi.exception.GlobalExceptionHandler;
import edu.HanYi.service.CourseService;
import edu.HanYi.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CourseControllerTest {
    private MockMvc mockMvc;

    @Mock
    private CourseService courseService;

    @InjectMocks
    private CourseController courseController;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(courseController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void createCourse_ValidRequest_ReturnsCreated() throws Exception {
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);

        CourseCreateRequest request = new CourseCreateRequest(
                "Beginner", "Courses for beginners", 1,
                futureDate, futureDate, BigDecimal.ZERO);
        CourseResponse response = new CourseResponse(
                1, "Beginner", "Courses for beginners", 1, "Category",
                futureDate, futureDate,
                BigDecimal.ZERO, List.of(), List.of());

        when(courseService.createCourse(any())).thenReturn(response);

        mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void createCourse_InvalidDates_ReturnsBadRequest() throws Exception {
        CourseCreateRequest request = new CourseCreateRequest(
                "Beginner", "Courses for beginners", 1,
                LocalDateTime.now().minusDays(1), LocalDateTime.now(), BigDecimal.ZERO);

        mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateCourse_InvalidProgress_ReturnsBadRequest() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        CourseCreateRequest invalidRequest = new CourseCreateRequest(
                "Invalid",
                "Should fail",
                1,
                now.plusDays(1),
                now.plusDays(2),
                new BigDecimal("150.00"));

        mockMvc.perform(put("/api/courses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateCourse_ValidRequest_ReturnsOk() throws Exception {
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);

        CourseCreateRequest request = new CourseCreateRequest(
                "Updated Header",
                "Updated Description",
                2,
                futureDate,
                futureDate,
                new BigDecimal("50.00"));

        CourseResponse response = new CourseResponse(
                1,
                "Updated Header",
                "Updated Description",
                2,
                "Advanced",
                futureDate,
                futureDate,
                new BigDecimal("50.00"),
                List.of(),
                List.of());

        when(courseService.updateCourse(eq(1), any()))
                .thenReturn(response);

        mockMvc.perform(put("/api/courses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header").value("Updated Header"))
                .andExpect(jsonPath("$.description").value("Updated Description"))
                .andExpect(jsonPath("$.progress").value(50.00));
    }

    @Test
    void getCourseById_NotFound_ReturnsNotFound() throws Exception {
        when(courseService.getCourseById(100)).thenThrow(new ResourceNotFoundException("Not found"));

        mockMvc.perform(get("/api/courses/100"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getCourseById_ValidRequest_ReturnsFound() throws Exception {
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);

        CourseResponse response = new CourseResponse(
                1,
                "Updated Header",
                "Updated Description",
                2,
                "Advanced",
                futureDate,
                futureDate,
                new BigDecimal("50.00"),
                List.of(),
                List.of());

        when(courseService.getCourseById(1)).thenReturn(response);

        mockMvc.perform(get("/api/courses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.header").value("Updated Header"));
    }

    @Test
    void getAllCourses() throws Exception {
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);
        List<CourseResponse> expectedResponse = Arrays.asList(
                new CourseResponse(
                        1,
                        "Updated Header",
                        "Updated Description",
                        2,
                        "Advanced",
                        futureDate,
                        futureDate,
                        new BigDecimal("50.00"),
                        List.of(),
                        List.of()),
                new CourseResponse(
                        2,
                        "Updated Header",
                        "Updated Description",
                        2,
                        "Advanced",
                        futureDate,
                        futureDate,
                        new BigDecimal("50.00"),
                        List.of(),
                        List.of())
        );

        when(courseService.getAllCourses()).thenReturn(expectedResponse);

        mockMvc.perform(get("/api/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[0].header").value("Updated Header"))
                .andExpect(jsonPath("$[1].header").value("Updated Header"));
    }

    @Test
    void deleteCourse_ValidRequest_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/courses/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteCourse_InvalidRequest_ReturnsNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Not found"))
                .when(courseService).deleteCourse(100);

        mockMvc.perform(delete("/api/courses/100"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getCoursesByCategoryId_ValidRequest_ReturnsFound() throws Exception {
        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);
        List<CourseResponse> expectedResponse = List.of(
                new CourseResponse(
                        1,
                        "Header",
                        "Description",
                        2,
                        "Advanced",
                        futureDate,
                        futureDate,
                        new BigDecimal("50.00"),
                        List.of(),
                        List.of()));

        when(courseService.getCoursesByCategoryId(1)).thenReturn(expectedResponse);

        mockMvc.perform(get("/api/courses/category/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].description").value("Description"));
    }

    @Test
    void getCoursesByCourseId_NotFound_Returns404() throws Exception {
        when(courseService.getCoursesByCategoryId(10000)).thenThrow(new ResourceNotFoundException("Course not found"));

        mockMvc.perform(get("/api/courses/category/10000"))
                .andExpect(status().isNotFound());
    }
}