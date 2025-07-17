package edu.HanYi.controller;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.HanYi.dto.request.UserCreateRequest;
import edu.HanYi.dto.response.UserResponse;
import edu.HanYi.exception.GlobalExceptionHandler;
import edu.HanYi.model.User;
import edu.HanYi.service.UserService;
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

class UserControllerTest {
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void createUser_ValidRequest_ReturnsCreated() throws Exception {
        UserCreateRequest request =
                new UserCreateRequest("user123", "pass12345", "emailTEST@mail.ru", User.Role.STUDENT);
        UserResponse response = new UserResponse(
                1,
                "user123",
                "emailTEST@mail.ru",
                User.Role.STUDENT,
                List.of(),
                List.of());

        when(userService.createUser(any()))
                .thenReturn(response);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void createUser_InvalidEmail_ReturnsBadRequest() throws Exception {
        UserCreateRequest request =
                new UserCreateRequest("user123", "pass12345", "invalid-email", User.Role.STUDENT);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getUserById_NotFound_ReturnsNotFound() throws Exception {
        when(userService.getUserById(100)).thenThrow(new ResourceNotFoundException("Not found"));

        mockMvc.perform(get("/api/users/100"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateUser_InvalidEMail_ReturnsBadRequest() throws Exception {
        UserCreateRequest invalidRequest =
                new UserCreateRequest("user123", "pass12345", "invalid", User.Role.STUDENT);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUser_ValidRequest_ReturnsOk() throws Exception {
        UserCreateRequest request =
                new UserCreateRequest("user123", "pass12345", "emailTEST@mail.ru", User.Role.STUDENT);
        UserResponse response = new UserResponse(
                1,
                "user123",
                "emailTEST@mail.ru",
                User.Role.STUDENT,
                List.of(),
                List.of());

        when(userService.updateUser(eq(1), any()))
                .thenReturn(response);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getUserById_ValidRequest_ReturnsFound() throws Exception {
        UserResponse response = new UserResponse(
                1,
                "user123",
                "emailTEST@gmail.com",
                User.Role.STUDENT,
                List.of(),
                List.of());

        when(userService.getUserById(1)).thenReturn(response);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("user123"));
    }

    @Test
    void getAllUsers() throws Exception {
        List<UserResponse> expectedResponse = Arrays.asList(
                new UserResponse(
                        1,
                        "user123",
                        "emailTEST1@gmail.com",
                        User.Role.STUDENT,
                        List.of(),
                        List.of()),
                new UserResponse(
                        2,
                        "user123",
                        "emailTEST2@gmail.com",
                        User.Role.STUDENT,
                        List.of(),
                        List.of())
        );

        when(userService.getAllUsers()).thenReturn(expectedResponse);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[0].username").value("user123"))
                .andExpect(jsonPath("$[1].username").value("user123"));
    }

    @Test
    void deleteUser_ValidRequest_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteUser_InvalidRequest_ReturnsNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Not found"))
                .when(userService).deleteUser(100);

        mockMvc.perform(delete("/api/users/100"))
                .andExpect(status().isNotFound());
    }
}