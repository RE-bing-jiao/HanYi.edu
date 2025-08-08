package edu.HanYi.service.impl;

import edu.HanYi.constants.LoggingConstants;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private static final Marker TO_CONSOLE = LoggingConstants.TO_CONSOLE;

    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryCreateRequest request) {
        log.debug(LoggingConstants.DEBUG_CHECK_CATEGORY_EXISTS, request.name());
        if (categoryRepository.existsByName(request.name())) {
            log.error(TO_CONSOLE, LoggingConstants.CATEGORY_EXISTS_NAME, request.name());
            throw new ResourceAlreadyExistsException("Category with name '" + request.name() + "' already exists");
        }

        log.debug(LoggingConstants.DEBUG_CREATE_CATEGORY, request.name());
        Category category = new Category();
        category.setName(request.name());
        category.setDescription(request.description());

        Category savedCategory = categoryRepository.save(category);
        log.info(TO_CONSOLE, LoggingConstants.CATEGORY_CREATED, savedCategory.getId(), savedCategory.getName());
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
                    log.error(TO_CONSOLE, LoggingConstants.CATEGORY_NOT_FOUND_ID, id);
                    return new ResourceNotFoundException("Category not found with id: " + id);
                });
        log.debug(LoggingConstants.DEBUG_FOUND_CATEGORY, category.getName());
        return mapToResponse(category);
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(Integer id, CategoryCreateRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.error(TO_CONSOLE, LoggingConstants.CATEGORY_UPDATE_FAILED, id);
                    return new ResourceNotFoundException("Category not found with id: " + id);
                });

        if (!category.getName().equals(request.name())) {
            if (categoryRepository.existsByName(request.name())) {
                log.error(TO_CONSOLE, LoggingConstants.CATEGORY_UPDATE_FAILED_NAME, request.name());
                throw new ResourceAlreadyExistsException("Category with name '" + request.name() + "' already exists");
            }
            category.setName(request.name());
        }

        category.setDescription(request.description());
        Category updatedCategory = categoryRepository.save(category);
        log.info(TO_CONSOLE, LoggingConstants.CATEGORY_UPDATED, id, request.name());
        return mapToResponse(updatedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(Integer id) {
        if (!categoryRepository.existsById(id)) {
            log.error(TO_CONSOLE, LoggingConstants.CATEGORY_DELETE_FAILED, id);
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
        log.info(TO_CONSOLE, LoggingConstants.CATEGORY_DELETED, id);
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