package edu.HanYi.service.impl;

import edu.HanYi.dto.request.FlashcardCreateRequest;
import edu.HanYi.dto.response.FlashcardResponse;
import edu.HanYi.exception.ResourceNotFoundException;
import edu.HanYi.model.*;
import edu.HanYi.repository.FlashcardRepository;
import edu.HanYi.repository.UserRepository;
import edu.HanYi.service.FlashcardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlashcardServiceImpl implements FlashcardService {

    private final FlashcardRepository flashcardRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public FlashcardResponse createFlashcard(FlashcardCreateRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.userId()));

        Flashcard flashcard = new Flashcard();
        flashcard.setFrontText(request.frontText());
        flashcard.setBackText(request.backText());
        flashcard.setCreatedAt(LocalDateTime.now());
        flashcard.setUser(user);

        Flashcard savedFlashcard = flashcardRepository.save(flashcard);
        return mapToResponse(savedFlashcard);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FlashcardResponse> getAllFlashcards() {
        return flashcardRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public FlashcardResponse getFlashcardById(Integer id) {
        Flashcard flashcard = flashcardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flashcard not found with id: " + id));
        return mapToResponse(flashcard);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FlashcardResponse> getFlashcardsByUserId(Integer userId) {
        return flashcardRepository.findByUserId(userId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public FlashcardResponse updateFlashcard(Integer id, FlashcardCreateRequest request) {
        Flashcard flashcard = flashcardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flashcard not found with id: " + id));

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.userId()));

        flashcard.setFrontText(request.frontText());
        flashcard.setBackText(request.backText());
        flashcard.setUser(user);

        Flashcard updatedFlashcard = flashcardRepository.save(flashcard);
        return mapToResponse(updatedFlashcard);
    }

    @Override
    @Transactional
    public void deleteFlashcard(Integer id) {
        if (!flashcardRepository.existsById(id)) {
            throw new ResourceNotFoundException("Flashcard not found with id: " + id);
        }
        flashcardRepository.deleteById(id);
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
