package edu.HanYi.controller;

import edu.HanYi.constants.LoggingConstants;
import edu.HanYi.dto.request.CategoryCreateRequest;
import edu.HanYi.dto.response.CategoryResponse;
import edu.HanYi.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private static final Marker TO_CONSOLE = LoggingConstants.TO_CONSOLE;

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryCreateRequest request) {
        log.info(TO_CONSOLE, LoggingConstants.CATEGORY_CREATE, request.name());
        CategoryResponse response = categoryService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        log.debug(LoggingConstants.CATEGORY_FETCH);
        List<CategoryResponse> categories = categoryService.getAllCategories();
        log.info(TO_CONSOLE, LoggingConstants.CATEGORIES_FETCHED, categories.size());
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Integer id) {
        log.debug(LoggingConstants.CATEGORY_FETCH, id);
        CategoryResponse response = categoryService.getCategoryById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable Integer id,
            @Valid @RequestBody CategoryCreateRequest request) {
        log.info(TO_CONSOLE, LoggingConstants.CATEGORY_UPDATE, id, request.name());
        CategoryResponse response = categoryService.updateCategory(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {
        log.info(TO_CONSOLE, LoggingConstants.CATEGORY_DELETE, id);
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}