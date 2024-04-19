package org.example.miniprojectspring.service;

import org.example.miniprojectspring.exception.SearchNotFoundException;
import org.example.miniprojectspring.model.entity.Category;
import org.example.miniprojectspring.model.request.CategoryRequest;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    List<Category> getAllCategories(UUID userId,Integer offset,Integer page);
    Category getCategoryById(UUID userId,UUID categoryId) throws SearchNotFoundException;
    Category deleteCategoryById(UUID categoryId);
    Category updateCategoryById(UUID categoryId, UUID userId,CategoryRequest request);
    Category postCategory(CategoryRequest request,UUID userId);

}
