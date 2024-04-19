package org.example.miniprojectspring.service.ServiceImplementation;

import org.example.miniprojectspring.model.entity.Category;
import org.example.miniprojectspring.model.request.CategoryRequest;
import org.example.miniprojectspring.repository.CategoryRepository;
import org.example.miniprojectspring.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getAllCategories(UUID userId,Integer size, Integer page) {
        return categoryRepository.getAllCategories(userId,size,page);
    }

    @Override
    public Category getCategoryById(UUID userId,UUID categoryId) {
        return categoryRepository.getCategoryById(userId,categoryId);
    }

    @Override
    public Category deleteCategoryById(UUID categoryId) {
        System.out.println(categoryId + " in service impl");
        return categoryRepository.deleteCategoryById(categoryId);
    }

    @Override
    public Category updateCategoryById(UUID categoryId,UUID userId, CategoryRequest request) {
        return categoryRepository.updateCategoryById(categoryId,userId,request);
    }

    @Override
    public Category postCategory(CategoryRequest request,UUID userId) {
        return categoryRepository.createNewCategory(request,userId);
    }
}
