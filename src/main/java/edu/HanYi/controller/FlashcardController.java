package edu.HanYi.controller;

import edu.HanYi.dto.request.FlashcardCreateRequest;
import edu.HanYi.dto.response.FlashcardResponse;
import edu.HanYi.service.FlashcardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/flashcards")
@RequiredArgsConstructor
public class FlashcardController {
    private static final Marker TO_CONSOLE = MarkerFactory.getMarker("TO_CONSOLE");
    private final FlashcardService flashcardService;

    @PostMapping
    public ResponseEntity<FlashcardResponse> createFlashcard(@Valid @RequestBody FlashcardCreateRequest request) {
        log.info(TO_CONSOLE, "Creating flashcard for user ID: {}", request.userId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(flashcardService.createFlashcard(request));
    }

    @GetMapping
    public ResponseEntity<List<FlashcardResponse>> getAllFlashcards() {
        log.debug("Fetching all flashcards");
        return ResponseEntity.ok(flashcardService.getAllFlashcards());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlashcardResponse> getFlashcardById(@PathVariable Integer id) {
        return ResponseEntity.ok(flashcardService.getFlashcardById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FlashcardResponse>> getFlashcardsByUserId(@PathVariable Integer userId) {
        log.debug("Request received for flashcards by user ID: {}", userId);
        return ResponseEntity.ok(flashcardService.getFlashcardsByUserId(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FlashcardResponse> updateFlashcard(
            @PathVariable Integer id,
            @Valid @RequestBody FlashcardCreateRequest request) {
        log.info(TO_CONSOLE, "Updating flashcard ID: {}", id);
        return ResponseEntity.ok(flashcardService.updateFlashcard(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlashcard(@PathVariable Integer id) {
        log.info(TO_CONSOLE, "Deleting flashcard ID: {}", id);
        flashcardService.deleteFlashcard(id);
        return ResponseEntity.noContent().build();
    }
}