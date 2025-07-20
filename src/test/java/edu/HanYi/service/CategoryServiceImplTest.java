package edu.HanYi.service;

import edu.HanYi.dto.request.CategoryCreateRequest;
import edu.HanYi.dto.response.CategoryResponse;
import edu.HanYi.exception.ResourceAlreadyExistsException;
import edu.HanYi.exception.ResourceNotFoundException;
import edu.HanYi.model.Category;
import edu.HanYi.repository.CategoryRepository;
import edu.HanYi.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private CategoryCreateRequest validRequest;
    private Category existingCategory;
    private final Integer NON_EXISTING_ID = 10000;

    @BeforeEach
    void setUp() {
        validRequest = new CategoryCreateRequest("Name", "Desc");
        existingCategory = new Category();
        existingCategory.setId(1);
        existingCategory.setName(validRequest.name());
        existingCategory.setDescription(validRequest.description());
    }

    @Test
    void createCategory_InvalidRequest_ThrowsResourceAlreadyExistsExceptionException() {
        when(categoryRepository.existsByName(validRequest.name())).thenReturn(true);

        assertThrows(
                ResourceAlreadyExistsException.class,
                () -> categoryService.createCategory(validRequest)
        );
        verify(categoryRepository).existsByName(validRequest.name());
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void createCategory_ValidRequest_ShouldCreate() {
        when(categoryRepository.existsByName(validRequest.name())).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(existingCategory);

        CategoryResponse response = categoryService.createCategory(validRequest);

        assertNotNull(response);
        assertEquals(validRequest.name(), response.name());
        verify(categoryRepository).existsByName(validRequest.name());
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void getAllCategories() {
        when(categoryRepository.findAll()).thenReturn(List.of(existingCategory));

        List<CategoryResponse> result = categoryService.getAllCategories();

        assertEquals(1, result.size());
        assertEquals(existingCategory.getName(), result.get(0).name());
        verify(categoryRepository).findAll();
    }

    @Test
    void getCategoryById_ValidRequest_ShouldFind() {
        when(categoryRepository.findById(1)).thenReturn(Optional.of(existingCategory));

        CategoryResponse result = categoryService.getCategoryById(1);

        assertEquals(existingCategory.getName(), result.name());
        verify(categoryRepository).findById(1);
    }

    @Test
    void getCategoryById_InvalidRequest_ThrowsResourceNotFoundException() {
        when(categoryRepository.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> categoryService.getCategoryById(NON_EXISTING_ID)
        );
        verify(categoryRepository).findById(NON_EXISTING_ID);
    }

    @Test
    void updateCategory_InvalidRequest_ThrowsResourceAlreadyExistsException() {
        when(categoryRepository.findById(1)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.existsByName("Existing Name")).thenReturn(true);

        CategoryCreateRequest conflictRequest = new CategoryCreateRequest("Existing Name", "Desc");
        assertThrows(
                ResourceAlreadyExistsException.class,
                () -> categoryService.updateCategory(1, conflictRequest)
        );
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void updateCategory_ValidRequest_ShouldUpdate() {
        when(categoryRepository.findById(1)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.existsByName("New Name")).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(existingCategory);

        CategoryCreateRequest updateRequest = new CategoryCreateRequest("New Name", "Updated desc");
        CategoryResponse result = categoryService.updateCategory(1, updateRequest);

        assertEquals(updateRequest.name(), result.name());
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void deleteCategory_ValidRequest_ShouldDelete() {
        when(categoryRepository.existsById(1)).thenReturn(true);
        doNothing().when(categoryRepository).deleteById(1);

        categoryService.deleteCategory(1);
        verify(categoryRepository).deleteById(1);
    }

    @Test
    void deleteCategory_InvalidRequest_ThrowsResourceNotFoundException() {
        when(categoryRepository.existsById(NON_EXISTING_ID)).thenReturn(false);

        assertThrows(
                ResourceNotFoundException.class,
                () -> categoryService.deleteCategory(NON_EXISTING_ID)
        );
        verify(categoryRepository, never()).deleteById(any());
    }
}