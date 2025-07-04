package edu.HanYi.controller;

import edu.HanYi.dto.request.EntryCreateRequest;
import edu.HanYi.dto.response.EntryResponse;
import edu.HanYi.service.EntryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/entries")
@RequiredArgsConstructor
public class EntryController {

    private final EntryService entryService;

    @PostMapping
    public ResponseEntity<EntryResponse> createEntry(@Valid @RequestBody EntryCreateRequest request) {
        EntryResponse response = entryService.createEntry(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<EntryResponse>> getAllEntries() {
        return ResponseEntity.ok(entryService.getAllEntries());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntryResponse> getEntryById(@PathVariable Integer id) {
        return ResponseEntity.ok(entryService.getEntryById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<EntryResponse>> getEntriesByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(entryService.getEntriesByUserId(userId));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<EntryResponse>> getEntriesByCourseId(@PathVariable Integer courseId) {
        return ResponseEntity.ok(entryService.getEntriesByCourseId(courseId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntry(@PathVariable Integer id) {
        entryService.deleteEntry(id);
        return ResponseEntity.noContent().build();
    }
}