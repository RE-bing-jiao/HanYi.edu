package edu.HanYi.service;

import edu.HanYi.dto.request.CategoryCreateRequest;
import edu.HanYi.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse createCategory(CategoryCreateRequest request);
    List<CategoryResponse> getAllCategories();
    CategoryResponse getCategoryById(Integer id);
    CategoryResponse updateCategory(Integer id, CategoryCreateRequest request);
    void deleteCategory(Integer id);
}
