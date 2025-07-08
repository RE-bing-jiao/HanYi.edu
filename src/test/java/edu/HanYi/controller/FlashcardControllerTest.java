package edu.HanYi.controller;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.HanYi.dto.request.FlashcardCreateRequest;
import edu.HanYi.dto.response.FlashcardResponse;
import edu.HanYi.service.FlashcardService;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class FlashcardControllerTest {
    private MockMvc mockMvc;

    @Mock
    private FlashcardService flashcardService;

    @InjectMocks
    private FlashcardController flashcardController;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(flashcardController).build();
    }

    @Test
    void createFlashcard_ValidRequest_ReturnsCreated() throws Exception {
        FlashcardCreateRequest request = new FlashcardCreateRequest(1, "你好", "Hello");
        FlashcardResponse response = new FlashcardResponse(
                1,
                1,
                "user",
                "你好",
                "Hello",
                LocalDateTime.now().plusDays(1));

        when(flashcardService.createFlashcard(any())).thenReturn(response);

        mockMvc.perform(post("/api/flashcards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void createFlashcard_EmptyFrontText_ReturnsBadRequest() throws Exception {
        FlashcardCreateRequest request = new FlashcardCreateRequest(1, "", "Hello");

        mockMvc.perform(post("/api/flashcards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getFlashcardById_NotFound_ReturnsNotFound() throws Exception {
        when(flashcardService.getFlashcardById(100)).thenThrow(new ResourceNotFoundException("Not found"));

        mockMvc.perform(get("/api/flashcards/100"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateFlashcard_InvalidFrontText_ReturnsBadRequest() throws Exception {
        FlashcardCreateRequest invalidRequest = new FlashcardCreateRequest(1, null, "Hello");

        mockMvc.perform(put("/api/flashcards/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateFlashcard_ValidRequest_ReturnsOk() throws Exception {
        FlashcardCreateRequest request = new FlashcardCreateRequest(1, "你好", "Hello");
        FlashcardResponse response = new FlashcardResponse(
                1,
                1,
                "user",
                "你好",
                "Hello",
                LocalDateTime.now().plusDays(1));

        when(flashcardService.updateFlashcard(eq(1), any(FlashcardCreateRequest.class)))
                .thenReturn(response);

        mockMvc.perform(put("/api/flashcards/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.frontText").value("你好"));
    }

    @Test
    void getFlashcardById_ValidRequest_ReturnsFound() throws Exception {
        FlashcardResponse response = new FlashcardResponse(
                1,
                1,
                "user",
                "你好",
                "Hello",
                LocalDateTime.now().plusDays(1));

        when(flashcardService.getFlashcardById(1)).thenReturn(response);

        mockMvc.perform(get("/api/flashcards/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.frontText").value("你好"));
    }

    @Test
    void getAllFlashcards() throws Exception {
        List<FlashcardResponse> expectedResponse = List.of(
                new FlashcardResponse(
                        1,
                        1,
                        "user",
                        "你好",
                        "Hello",
                        LocalDateTime.now().plusDays(1))
        );

        when(flashcardService.getAllFlashcards()).thenReturn(expectedResponse);

        mockMvc.perform(get("/api/flashcards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].backText").value("Hello"));

    }

    @Test
    void deleteFlashcard_ValidRequest_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/flashcards/1"))
                .andExpect(status().isNoContent());
    }


    @Test
    void deleteFlashcard_InvalidRequest_ReturnsNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Not found"))
                .when(flashcardService).deleteFlashcard(100);

        mockMvc.perform(delete("/api/flashcards/100"))
                .andExpect(status().isNotFound());
    }
}