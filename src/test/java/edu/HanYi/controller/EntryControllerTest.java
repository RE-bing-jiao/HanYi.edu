package edu.HanYi.controller;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.HanYi.dto.request.EntryCreateRequest;
import edu.HanYi.dto.response.CategoryResponse;
import edu.HanYi.dto.response.EntryResponse;
import edu.HanYi.service.EntryService;
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
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class EntryControllerTest {
    private MockMvc mockMvc;

    @Mock
    private EntryService entryService;

    @InjectMocks
    private EntryController entryController;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(entryController).build();
    }

    @Test
    void createEntry_ValidRequest_ReturnsCreated() throws Exception {
        EntryCreateRequest request = new EntryCreateRequest(1, 1, LocalDateTime.now().plusDays(1));
        EntryResponse response =
                new EntryResponse(
                        1,
                        1,
                        "user",
                        1,
                        "course",
                        LocalDateTime.now().plusDays(1));

        when(entryService.createEntry(any())).thenReturn(response);

        mockMvc.perform(post("/api/entries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void createEntry_PastDate_ReturnsBadRequest() throws Exception {
        EntryCreateRequest request = new EntryCreateRequest(1, 1, LocalDateTime.now());

        mockMvc.perform(post("/api/entries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllCategories() throws Exception {
        List<EntryResponse> expectedResponse = Arrays.asList(
                new EntryResponse(
                        1,
                        1,
                        "user",
                        1,
                        "course",
                        LocalDateTime.now().plusDays(1)),
                new EntryResponse(
                        2,
                        1,
                        "user",
                        1,
                        "course",
                        LocalDateTime.now().plusDays(1))
        );

        when(entryService.getAllEntries()).thenReturn(expectedResponse);

        mockMvc.perform(get("/api/entries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[0].username").value("user"))
                .andExpect(jsonPath("$[1].username").value("user"));
    }

    @Test
    void getEntryById_ValidRequest_ReturnsFound() throws Exception {
        EntryResponse response =
                new EntryResponse(
                        1,
                        1,
                        "user",
                        1,
                        "course",
                        LocalDateTime.now().plusDays(1));

        when(entryService.getEntryById(1)).thenReturn(response);

        mockMvc.perform(get("/api/entries/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getEntryById_InvalidRequest_ReturnsNotFound() throws Exception {
        when(entryService.getEntryById(100)).thenThrow(new ResourceNotFoundException("Not found"));

        mockMvc.perform(get("/api/entries/100"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteEntry_ValidRequest_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/entries/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteEntry_NotFound_ReturnsNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Not found")).when(entryService).deleteEntry(100);

        mockMvc.perform(delete("/api/entries/100"))
                .andExpect(status().isNotFound());
    }
}
