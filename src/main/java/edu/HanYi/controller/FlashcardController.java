package edu.HanYi.controller;

import edu.HanYi.dto.request.FlashcardCreateRequest;
import edu.HanYi.dto.response.FlashcardResponse;
import edu.HanYi.exception.ResourceNotFoundException;
import edu.HanYi.service.FlashcardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flashcards")
@RequiredArgsConstructor
public class FlashcardController {
    private final FlashcardService flashcardService;

    @PostMapping
    public ResponseEntity<FlashcardResponse> createFlashcard(@Valid @RequestBody FlashcardCreateRequest request) {
        FlashcardResponse response = flashcardService.createFlashcard(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<FlashcardResponse>> getAllFlashcards() {
        return ResponseEntity.ok(flashcardService.getAllFlashcards());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlashcardResponse> getFlashcardById(@PathVariable Integer id) {
        return ResponseEntity.ok(flashcardService.getFlashcardById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FlashcardResponse>> getFlashcardsByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(flashcardService.getFlashcardsByUserId(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FlashcardResponse> updateFlashcard(
            @PathVariable Integer id,
            @Valid @RequestBody FlashcardCreateRequest request) {
        return ResponseEntity.ok(flashcardService.updateFlashcard(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlashcard(@PathVariable Integer id) {
        flashcardService.deleteFlashcard(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNotFound(ResourceNotFoundException ex) {
        return ex.getMessage();
    }
}
