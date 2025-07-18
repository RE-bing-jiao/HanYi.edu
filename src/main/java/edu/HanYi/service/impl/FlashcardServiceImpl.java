package edu.HanYi.service.impl;

import edu.HanYi.dto.request.FlashcardCreateRequest;
import edu.HanYi.dto.response.FlashcardResponse;
import edu.HanYi.exception.ResourceNotFoundException;
import edu.HanYi.model.*;
import edu.HanYi.repository.FlashcardRepository;
import edu.HanYi.repository.UserRepository;
import edu.HanYi.service.FlashcardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlashcardServiceImpl implements FlashcardService {
    private static final Marker TO_CONSOLE = MarkerFactory.getMarker("TO_CONSOLE");
    private final FlashcardRepository flashcardRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public FlashcardResponse createFlashcard(FlashcardCreateRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> {
                    log.error(TO_CONSOLE, "User not found with ID: {}", request.userId());
                    return new ResourceNotFoundException("User not found with id: " + request.userId());
                });

        Flashcard flashcard = new Flashcard();
        flashcard.setFrontText(request.frontText());
        flashcard.setBackText(request.backText());
        flashcard.setCreatedAt(LocalDateTime.now());
        flashcard.setUser(user);

        Flashcard savedFlashcard = flashcardRepository.save(flashcard);
        log.info(TO_CONSOLE, "Created flashcard ID: {} for user ID: {}",
                savedFlashcard.getId(), request.userId());
        return mapToResponse(savedFlashcard);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FlashcardResponse> getAllFlashcards() {
        List<Flashcard> flashcards = flashcardRepository.findAll();
        log.info(TO_CONSOLE, "Fetched {} flashcards", flashcards.size());
        return flashcards.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public FlashcardResponse getFlashcardById(Integer id) {
        log.debug("Fetching flashcard ID: {}", id);
        Flashcard flashcard = flashcardRepository.findById(id)
                .orElseThrow(() -> {
                    log.error(TO_CONSOLE, "Flashcard not found with ID: {}", id);
                    return new ResourceNotFoundException("Flashcard not found with id: " + id);
                });
        return mapToResponse(flashcard);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FlashcardResponse> getFlashcardsByUserId(Integer userId) {
        if (!userRepository.existsById(userId)) {
            log.error(TO_CONSOLE, "User not found with ID: {}", userId);
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        log.debug("Fetching flashcards for user ID: {}", userId);
        List<Flashcard> flashcards = flashcardRepository.findByUserId(userId);
        return flashcards.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public FlashcardResponse updateFlashcard(Integer id, FlashcardCreateRequest request) {
        Flashcard flashcard = flashcardRepository.findById(id)
                .orElseThrow(() -> {
                    log.error(TO_CONSOLE, "Flashcard not found with ID: {}", id);
                    return new ResourceNotFoundException("Flashcard not found with id: " + id);
                });

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> {
                    log.error(TO_CONSOLE, "User not found with ID: {}", request.userId());
                    return new ResourceNotFoundException("User not found with id: " + request.userId());
                });

        flashcard.setFrontText(request.frontText());
        flashcard.setBackText(request.backText());
        flashcard.setUser(user);

        Flashcard updatedFlashcard = flashcardRepository.save(flashcard);
        log.info(TO_CONSOLE, "Updated flashcard ID: {}", id);
        return mapToResponse(updatedFlashcard);
    }

    @Override
    @Transactional
    public void deleteFlashcard(Integer id) {
        log.debug("Checking flashcard existence ID: {}", id);
        if (!flashcardRepository.existsById(id)) {
            log.error(TO_CONSOLE, "Flashcard not found with ID: {}", id);
            throw new ResourceNotFoundException("Flashcard not found with id: " + id);
        }
        flashcardRepository.deleteById(id);
        log.info(TO_CONSOLE, "Deleted flashcard ID: {}", id);
    }

    private FlashcardResponse mapToResponse(Flashcard flashcard) {
        return new FlashcardResponse(
                flashcard.getId(),
                flashcard.getUser().getId(),
                flashcard.getUser().getUsername(),
                flashcard.getFrontText(),
                flashcard.getBackText(),
                flashcard.getCreatedAt()
        );
    }
}