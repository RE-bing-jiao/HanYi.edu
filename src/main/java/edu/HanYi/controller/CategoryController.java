package edu.HanYi.controller;

import edu.HanYi.dto.request.CategoryCreateRequest;
import edu.HanYi.dto.response.CategoryResponse;
import edu.HanYi.service.CategoryService;
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
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private static final Marker TO_CONSOLE = MarkerFactory.getMarker("TO_CONSOLE");

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryCreateRequest request) {
        log.info(TO_CONSOLE, "Creating category with name: {}", request.name());
        CategoryResponse response = categoryService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        log.debug("Fetching all categories");
        List<CategoryResponse> categories = categoryService.getAllCategories();
        log.info(TO_CONSOLE, "Fetched {} categories", categories.size());
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Integer id) {
        log.debug("Fetching category by ID: {}", id);
        CategoryResponse response = categoryService.getCategoryById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable Integer id,
            @Valid @RequestBody CategoryCreateRequest request) {
        log.info(TO_CONSOLE, "Updating category ID: {}, new name: {}", id, request.name());
        CategoryResponse response = categoryService.updateCategory(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {
        log.info(TO_CONSOLE, "Deleting category ID: {}", id);
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
