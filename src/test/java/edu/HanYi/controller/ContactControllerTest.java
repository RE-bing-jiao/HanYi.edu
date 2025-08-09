package edu.HanYi.controller;

import edu.HanYi.exception.GlobalExceptionHandler;
import edu.HanYi.service.ContactService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ContactControllerTest {
    private MockMvc mockMvc;

    @Mock
    private ContactService contactService;

    @InjectMocks
    private ContactController contactController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(contactController)
                .setControllerAdvice(GlobalExceptionHandler.class)
                .build();
    }

    @Test
    void createContact_InvalidRequest_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/submit-contact")
                        .param("name", "")
                        .param("email", "invalid-email")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void createContact_ValidRequest_ReturnsSuccess() throws Exception {
        mockMvc.perform(post("/submit-contact")
                        .param("name", "Valid Name")
                        .param("email", "valid@email.com")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("success"));
    }
}
