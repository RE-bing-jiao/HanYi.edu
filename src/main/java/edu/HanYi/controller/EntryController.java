package edu.HanYi.controller;

import edu.HanYi.dto.request.EntryCreateRequest;
import edu.HanYi.dto.response.EntryResponse;
import edu.HanYi.service.EntryService;
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
@RequestMapping("/api/entries")
@RequiredArgsConstructor
public class EntryController {
    private static final Marker TO_CONSOLE = MarkerFactory.getMarker("TO_CONSOLE");
    private final EntryService entryService;

    @PostMapping
    public ResponseEntity<EntryResponse> createEntry(@Valid @RequestBody EntryCreateRequest request) {
        log.info(TO_CONSOLE, "Creating entry for user ID: {} and course ID: {}",
                request.userId(), request.courseId());
        EntryResponse response = entryService.createEntry(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<EntryResponse>> getAllEntries() {
        log.debug("Fetching all entries");
        List<EntryResponse> entries = entryService.getAllEntries();
        log.info(TO_CONSOLE, "Fetched {} entries", entries.size());
        return ResponseEntity.ok(entries);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntryResponse> getEntryById(@PathVariable Integer id) {
        log.debug("Fetching entry by ID: {}", id);
        EntryResponse response = entryService.getEntryById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<EntryResponse>> getEntriesByUserId(@PathVariable Integer userId) {
        List<EntryResponse> entries = entryService.getEntriesByUserId(userId);
        log.info(TO_CONSOLE, "Fetched {} entries for user ID: {}", entries.size(), userId);
        return ResponseEntity.ok(entries);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<EntryResponse>> getEntriesByCourseId(@PathVariable Integer courseId) {
        List<EntryResponse> entries = entryService.getEntriesByCourseId(courseId);
        log.info(TO_CONSOLE, "Fetched {} entries for course ID: {}", entries.size(), courseId);
        return ResponseEntity.ok(entries);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntry(@PathVariable Integer id) {
        log.info(TO_CONSOLE, "Deleting entry ID: {}", id);
        entryService.deleteEntry(id);
        return ResponseEntity.noContent().build();
    }
}