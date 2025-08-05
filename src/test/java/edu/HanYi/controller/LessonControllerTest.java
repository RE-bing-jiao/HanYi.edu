package edu.HanYi.controller;

import edu.HanYi.dto.request.LessonCreateRequest;
import edu.HanYi.dto.response.LessonResponse;
import edu.HanYi.exception.GlobalExceptionHandler;
import edu.HanYi.service.LessonService;
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

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class LessonControllerTest {
    private MockMvc mockMvc;

    @Mock
    private LessonService lessonService;

    @InjectMocks
    private LessonController lessonController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(lessonController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void createLesson_ValidRequest_ReturnsCreated() throws Exception {
        LessonCreateRequest request =
                new LessonCreateRequest(1, "Title", "Desc", "url", 1);
        LessonResponse response =
                new LessonResponse(
                        1,
                        1,
                        "Title",
                        "Desc",
                        "url",
                        1,
                        "Course");

        when(lessonService.createLesson(any())).thenReturn(response);

        mockMvc.perform(post("/api/lessons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void getAllCategories() throws Exception {
        List<LessonResponse> expectedResponse = Arrays.asList(
                new LessonResponse(
                        1,
                        1,
                        "Title",
                        "Desc",
                        "url",
                        1,
                        "Course"),
                new LessonResponse(
                        2,
                        2,
                        "Title",
                        "Desc",
                        "url",
                        1,
                        "Course")
        );

        when(lessonService.getAllLessons()).thenReturn(expectedResponse);

        mockMvc.perform(get("/api/lessons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[0].header").value("Title"))
                .andExpect(jsonPath("$[1].header").value("Title"));
    }

    @Test
    void createLesson_InvalidRequest_ReturnsBadRequest() throws Exception {
        LessonCreateRequest request =
                new LessonCreateRequest(1, null, "Desc", "url", 1);

        mockMvc.perform(post("/api/lessons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getLessonById_NotFound_ReturnsNotFound() throws Exception {
        when(lessonService.getLessonById(100)).thenThrow(new ResourceNotFoundException("Not found"));

        mockMvc.perform(get("/api/lessons/100"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getLessonById_ValidRequest_ReturnsFound() throws Exception {
        LessonResponse response =
                new LessonResponse(
                        1,
                        1,
                        "Title",
                        "Desc",
                        "url",
                        1,
                        "Course");

        when(lessonService.getLessonById(1)).thenReturn(response);

        mockMvc.perform(get("/api/lessons/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.header").value("Title"));
    }

    @Test
    void getLessonById_InvalidRequest_ReturnsNotFound() throws Exception {
        when(lessonService.getLessonById(100)).thenThrow(new ResourceNotFoundException("Not found"));

        mockMvc.perform(get("/api/lessons/100"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateLesson_ValidRequest_ReturnsOk() throws Exception {
        LessonCreateRequest request =
                new LessonCreateRequest(1, "Title", "Desc", "url", 1);
        LessonResponse response =
                new LessonResponse(
                        1,
                        1,
                        "Title",
                        "Desc",
                        "url",
                        1,
                        "Course");

        when(lessonService.updateLesson(anyInt(), any())).thenReturn(response);

        mockMvc.perform(put("/api/lessons/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteLesson_ValidRequest_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/lessons/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteLesson_InvalidRequest_ReturnsNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Not found"))
                .when(lessonService).deleteLesson(100);

        mockMvc.perform(delete("/api/lessons/100"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getLessonsByCourseId_ValidRequest_ReturnsFound() throws Exception {
        List<LessonResponse> expectedResponse = List.of(
                new LessonResponse(
                        1,
                        1,
                        "Title",
                        "Desc",
                        "url",
                        1,
                        "Course"));

        when(lessonService.getLessonsByCourseId(1)).thenReturn(expectedResponse);

        mockMvc.perform(get("/api/lessons/course/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].header").value("Title"));
    }

    @Test
    void getLessonsByCourseId_NotFound_Returns404() throws Exception {
        when(lessonService.getLessonsByCourseId(10000)).thenThrow(new ResourceNotFoundException("Course not found"));

        mockMvc.perform(get("/api/lessons/course/10000"))
                .andExpect(status().isNotFound());
    }
}
