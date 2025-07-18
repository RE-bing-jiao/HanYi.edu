package edu.HanYi.service.impl;

import edu.HanYi.dto.request.CategoryCreateRequest;
import edu.HanYi.dto.response.CategoryResponse;
import edu.HanYi.exception.ResourceAlreadyExistsException;
import edu.HanYi.exception.ResourceNotFoundException;
import edu.HanYi.model.Category;
import edu.HanYi.repository.CategoryRepository;
import edu.HanYi.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private static final Marker TO_CONSOLE = MarkerFactory.getMarker("TO_CONSOLE");

    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryCreateRequest request) {
        log.debug("Checking if category exists with name: {}", request.name());
        if (categoryRepository.existsByName(request.name())) {
            log.error(TO_CONSOLE, "Category already exists: {}", request.name());
            throw new ResourceAlreadyExistsException("Category with name '" + request.name() + "' already exists");
        }

        log.debug("Creating new category: {}", request.name());
        Category category = new Category();
        category.setName(request.name());
        category.setDescription(request.description());

        Category savedCategory = categoryRepository.save(category);
        log.info(TO_CONSOLE, "Created category ID: {}, name: {}", savedCategory.getId(), savedCategory.getName());
        return mapToResponse(savedCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.error(TO_CONSOLE, "Category not found with ID: {}", id);
                    return new ResourceNotFoundException("Category not found with id: " + id);
                });
        log.debug("Found category: {}", category.getName());
        return mapToResponse(category);
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(Integer id, CategoryCreateRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.error(TO_CONSOLE, "Update failed - category not found with ID: {}", id);
                    return new ResourceNotFoundException("Category not found with id: " + id);
                });

        if (!category.getName().equals(request.name())) {
            if (categoryRepository.existsByName(request.name())) {
                log.error(TO_CONSOLE, "Update failed - name already exists: {}", request.name());
                throw new ResourceAlreadyExistsException("Category with name '" + request.name() + "' already exists");
            }
            category.setName(request.name());
        }

        category.setDescription(request.description());
        Category updatedCategory = categoryRepository.save(category);
        log.info(TO_CONSOLE, "Updated category ID: {}, new name: {}", id, request.name());
        return mapToResponse(updatedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(Integer id) {
        if (!categoryRepository.existsById(id)) {
            log.error(TO_CONSOLE, "Delete failed - category not found with ID: {}", id);
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
        log.info(TO_CONSOLE, "Deleted category ID: {}", id);
    }

    private CategoryResponse mapToResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getCourses().stream()
                        .map(course -> new CategoryResponse.CourseResponse(
                                course.getId(),
                                course.getHeader()
                        ))
                        .toList()
        );
    }
}