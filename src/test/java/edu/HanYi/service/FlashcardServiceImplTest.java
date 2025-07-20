package edu.HanYi.service;

import edu.HanYi.dto.request.FlashcardCreateRequest;
import edu.HanYi.dto.response.FlashcardResponse;
import edu.HanYi.exception.ResourceNotFoundException;
import edu.HanYi.model.*;
import edu.HanYi.repository.FlashcardRepository;
import edu.HanYi.repository.UserRepository;
import edu.HanYi.service.impl.FlashcardServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FlashcardServiceImplTest {

    @Mock
    private FlashcardRepository flashcardRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FlashcardServiceImpl flashcardService;

    private FlashcardCreateRequest validRequest;
    private Flashcard existingFlashcard;
    private User existingUser;
    private final Integer NON_EXISTING_ID = 10000;

    @BeforeEach
    void setUp() {
        validRequest = new FlashcardCreateRequest(
                1,
                "Front text",
                "Back text"
        );

        existingUser = new User();
        existingUser.setId(1);
        existingUser.setUsername("user");

        existingFlashcard = new Flashcard();
        existingFlashcard.setId(1);
        existingFlashcard.setFrontText("Front text");
        existingFlashcard.setBackText("Back text");
        existingFlashcard.setCreatedAt(LocalDateTime.now());
        existingFlashcard.setUser(existingUser);
    }

    @Test
    void createFlashcard_ValidRequest_ShouldCreate() {
        when(userRepository.findById(validRequest.userId())).thenReturn(Optional.of(existingUser));
        when(flashcardRepository.save(any(Flashcard.class))).thenReturn(existingFlashcard);

        FlashcardResponse response = flashcardService.createFlashcard(validRequest);

        assertNotNull(response);
        assertEquals(existingUser.getId(), response.userId());
        verify(userRepository).findById(validRequest.userId());
        verify(flashcardRepository).save(any(Flashcard.class));
    }

    @Test
    void createFlashcard_InvalidRequest_ThrowsResourceNotFoundException() {
        when(userRepository.findById(validRequest.userId())).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> flashcardService.createFlashcard(validRequest)
        );
        verify(userRepository).findById(validRequest.userId());
        verify(flashcardRepository, never()).save(any());
    }

    @Test
    void getFlashcardById_ValidRequest_ShouldFind() {
        when(flashcardRepository.findById(1)).thenReturn(Optional.of(existingFlashcard));

        FlashcardResponse response = flashcardService.getFlashcardById(1);

        assertEquals(existingFlashcard.getId(), response.id());
        verify(flashcardRepository).findById(1);
    }

    @Test
    void getFlashcardById_InvalidRequest_ThrowsResourceNotFoundException() {
        when(flashcardRepository.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> flashcardService.getFlashcardById(NON_EXISTING_ID)
        );
        verify(flashcardRepository).findById(NON_EXISTING_ID);
    }

    @Test
    void getFlashcardsByUserId_ValidRequest_ShouldFind() {
        when(userRepository.existsById(1)).thenReturn(true);
        when(flashcardRepository.findByUserId(1)).thenReturn(List.of(existingFlashcard));

        List<FlashcardResponse> response = flashcardService.getFlashcardsByUserId(1);

        assertEquals(1, response.size());
        assertEquals(existingFlashcard.getId(), response.get(0).id());
        verify(userRepository).existsById(1);
        verify(flashcardRepository).findByUserId(1);
    }

    @Test
    void getFlashcardsByUserId_InvalidRequest_ThrowsResourceNotFoundException() {
        when(userRepository.existsById(NON_EXISTING_ID)).thenReturn(false);

        assertThrows(
                ResourceNotFoundException.class,
                () -> flashcardService.getFlashcardsByUserId(NON_EXISTING_ID)
        );
        verify(userRepository).existsById(NON_EXISTING_ID);
        verify(flashcardRepository, never()).findByUserId(any());
    }

    @Test
    void updateFlashcard_ValidRequest_ShouldUpdate() {
        when(flashcardRepository.findById(1)).thenReturn(Optional.of(existingFlashcard));
        when(userRepository.findById(validRequest.userId())).thenReturn(Optional.of(existingUser));
        when(flashcardRepository.save(any(Flashcard.class))).thenReturn(existingFlashcard);

        FlashcardResponse response = flashcardService.updateFlashcard(1, validRequest);

        assertEquals(existingFlashcard.getId(), response.id());
        verify(flashcardRepository).save(any(Flashcard.class));
    }

    @Test
    void updateFlashcard_InvalidRequest_ThrowsResourceNotFoundExceptionForFlashcard() {
        when(flashcardRepository.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> flashcardService.updateFlashcard(NON_EXISTING_ID, validRequest)
        );
        verify(flashcardRepository).findById(NON_EXISTING_ID);
        verify(flashcardRepository, never()).save(any());
    }

    @Test
    void updateFlashcard_InvalidRequest_ThrowsResourceNotFoundExceptionForUser() {
        when(flashcardRepository.findById(1)).thenReturn(Optional.of(existingFlashcard));
        when(userRepository.findById(validRequest.userId())).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> flashcardService.updateFlashcard(1, validRequest)
        );
        verify(userRepository).findById(validRequest.userId());
        verify(flashcardRepository, never()).save(any());
    }

    @Test
    void deleteFlashcard_ValidRequest_ShouldDelete() {
        when(flashcardRepository.existsById(1)).thenReturn(true);
        doNothing().when(flashcardRepository).deleteById(1);

        flashcardService.deleteFlashcard(1);
        verify(flashcardRepository).deleteById(1);
    }

    @Test
    void deleteFlashcard_InvalidRequest_ThrowsResourceNotFoundException() {
        when(flashcardRepository.existsById(NON_EXISTING_ID)).thenReturn(false);

        assertThrows(
                ResourceNotFoundException.class,
                () -> flashcardService.deleteFlashcard(NON_EXISTING_ID)
        );
        verify(flashcardRepository, never()).deleteById(any());
    }
}
