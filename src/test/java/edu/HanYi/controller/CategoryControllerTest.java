package edu.HanYi.controller;

import edu.HanYi.dto.request.CategoryCreateRequest;
import edu.HanYi.dto.response.CategoryResponse;
import edu.HanYi.exception.GlobalExceptionHandler;
import edu.HanYi.service.CategoryService;
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

class CategoryControllerTest {
    private MockMvc mockMvc;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(categoryController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void createCategory_ValidRequest_ReturnsCreated() throws Exception {
        CategoryCreateRequest request =
                new CategoryCreateRequest("Beginner", "Courses for beginners");
        CategoryResponse response =
                new CategoryResponse(1, "Beginner", "Courses for beginners", List.of());

        when(categoryService.createCategory(any())).thenReturn(response);

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void createCategory_InvalidRequest_ReturnsBadRequest() throws Exception {
        CategoryCreateRequest request = new CategoryCreateRequest("", null);

        mockMvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllCategories() throws Exception {
        List<CategoryResponse> expectedResponse = Arrays.asList(
                new CategoryResponse(1, "Beginner", "Courses for beginners", List.of()),
                new CategoryResponse(2, "Beginner", "Courses for beginners", List.of())
        );

        when(categoryService.getAllCategories()).thenReturn(expectedResponse);

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[0].name").value("Beginner"))
                .andExpect(jsonPath("$[1].name").value("Beginner"));
    }

    @Test
    void getCategoryById_ValidRequest_ReturnsFound() throws Exception {
        CategoryResponse response =
                new CategoryResponse(1, "Beginner", "Courses for beginners", List.of());

        when(categoryService.getCategoryById(1)).thenReturn(response);

        mockMvc.perform(get("/api/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Beginner"));
    }

    @Test
    void getCategoryById_InvalidRequest_ReturnsNotFound() throws Exception {
        when(categoryService.getCategoryById(100)).thenThrow(new ResourceNotFoundException("Not found"));

        mockMvc.perform(get("/api/categories/100"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateCategory_ValidRequest_ReturnsOk() throws Exception {
        CategoryCreateRequest request =
                new CategoryCreateRequest("Beginner", "Courses for beginners");
        CategoryResponse response =
                new CategoryResponse(1, "Beginner", "Courses for beginners", List.of());

        when(categoryService.updateCategory(anyInt(), any())).thenReturn(response);

        mockMvc.perform(put("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void updateCategory_InvalidRequest_ReturnsBadRequest() throws Exception {
        CategoryCreateRequest request = new CategoryCreateRequest("", null);

        when(categoryService.updateCategory(anyInt(), any())).thenThrow(new ResourceNotFoundException("Not found"));

        mockMvc.perform(put("/api/categories/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteCategory_ValidRequest_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/categories/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteCategory_InvalidRequest_ReturnsNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Not found"))
                .when(categoryService).deleteCategory(100);

        mockMvc.perform(delete("/api/categories/100"))
                .andExpect(status().isNotFound());
    }
}
