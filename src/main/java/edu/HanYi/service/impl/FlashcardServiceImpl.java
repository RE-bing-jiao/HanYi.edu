package edu.HanYi.service.impl;

import edu.HanYi.constants.LoggingConstants;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlashcardServiceImpl implements FlashcardService {
    private static final Marker TO_CONSOLE = LoggingConstants.TO_CONSOLE;
    private final FlashcardRepository flashcardRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public FlashcardResponse createFlashcard(FlashcardCreateRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> {
                    log.error(TO_CONSOLE, LoggingConstants.USER_NOT_FOUND_ID, request.userId());
                    return new ResourceNotFoundException("User not found with id: " + request.userId());
                });

        Flashcard flashcard = new Flashcard();
        flashcard.setFrontText(request.frontText());
        flashcard.setBackText(request.backText());
        flashcard.setCreatedAt(LocalDateTime.now());
        flashcard.setUser(user);

        Flashcard savedFlashcard = flashcardRepository.save(flashcard);
        log.info(TO_CONSOLE, LoggingConstants.FLASHCARD_CREATED, savedFlashcard.getId(), request.userId());
        return mapToResponse(savedFlashcard);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FlashcardResponse> getAllFlashcards() {
        List<Flashcard> flashcards = flashcardRepository.findAll();
        log.info(TO_CONSOLE, LoggingConstants.FLASHCARDS_FETCHED, flashcards.size());
        return flashcards.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public FlashcardResponse getFlashcardById(Integer id) {
        log.debug(LoggingConstants.DEBUG_FETCH_FLASHCARD, id);
        Flashcard flashcard = flashcardRepository.findById(id)
                .orElseThrow(() -> {
                    log.error(TO_CONSOLE, LoggingConstants.FLASHCARD_NOT_FOUND_ID, id);
                    return new ResourceNotFoundException("Flashcard not found with id: " + id);
                });
        return mapToResponse(flashcard);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FlashcardResponse> getFlashcardsByUserId(Integer userId) {
        if (!userRepository.existsById(userId)) {
            log.error(TO_CONSOLE, LoggingConstants.USER_NOT_FOUND_ID, userId);
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        log.debug(LoggingConstants.DEBUG_FETCH_FLASHCARDS_USER, userId);
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
                    log.error(TO_CONSOLE, LoggingConstants.FLASHCARD_NOT_FOUND_ID, id);
                    return new ResourceNotFoundException("Flashcard not found with id: " + id);
                });

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> {
                    log.error(TO_CONSOLE, LoggingConstants.USER_NOT_FOUND_ID, request.userId());
                    return new ResourceNotFoundException("User not found with id: " + request.userId());
                });

        flashcard.setFrontText(request.frontText());
        flashcard.setBackText(request.backText());
        flashcard.setUser(user);

        Flashcard updatedFlashcard = flashcardRepository.save(flashcard);
        log.info(TO_CONSOLE, LoggingConstants.FLASHCARD_UPDATED, id);
        return mapToResponse(updatedFlashcard);
    }

    @Override
    @Transactional
    public void deleteFlashcard(Integer id) {
        log.debug(LoggingConstants.DEBUG_CHECK_FLASHCARD_EXISTENCE, id);
        if (!flashcardRepository.existsById(id)) {
            log.error(TO_CONSOLE, LoggingConstants.FLASHCARD_NOT_FOUND_ID, id);
            throw new ResourceNotFoundException("Flashcard not found with id: " + id);
        }
        flashcardRepository.deleteById(id);
        log.info(TO_CONSOLE, LoggingConstants.FLASHCARD_DELETED, id);
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