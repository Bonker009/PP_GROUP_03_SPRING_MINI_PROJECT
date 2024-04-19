package org.example.miniprojectspring.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.example.miniprojectspring.exception.PageLimitException;
import org.example.miniprojectspring.exception.SearchNotFoundException;
import org.example.miniprojectspring.model.entity.Category;
import org.example.miniprojectspring.model.entity.CustomUserDetail;
import org.example.miniprojectspring.model.request.CategoryRequest;
import org.example.miniprojectspring.model.response.ApiResponse;
import org.example.miniprojectspring.security.JwtService;
import org.example.miniprojectspring.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/categories")
@SecurityRequirement(name = "bearerAuth")
@AllArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    @Operation(summary = "Get All Categories")
    public ResponseEntity<?> getAllCategory(@RequestParam(defaultValue = "3") @Positive(message = "size cannot be negative or 0") Integer size,
                                            @RequestParam(defaultValue = "1") @Positive(message = "Page cannot be negative or 0") Integer page) throws SearchNotFoundException, PageLimitException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
        UUID userId = customUserDetail.getAppUserDTO().getUserId();
        List<Category> categoryList = categoryService.getAllCategories(userId, size, page);

        ApiResponse<List<Category>> apiResponse = ApiResponse.<List<Category>>builder().message("Get all Categories successful").code(201).status(HttpStatus.OK).payload(categoryList).build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Category By id")
    public ResponseEntity<?> getCategoryById(@PathVariable UUID id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
        UUID userId = customUserDetail.getAppUserDTO().getUserId();
        Category category = categoryService.getCategoryById(userId, id);
        ApiResponse<Category> apiResponse = ApiResponse.<Category>builder().status(HttpStatus.OK).code(201).message("Get Category with Id : " + id + " successful").payload(category).build();
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping
    @Operation(summary = "Create new Category")
    public ResponseEntity<?> createCategory(@RequestBody CategoryRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
        UUID userId = customUserDetail.getAppUserDTO().getUserId();
        System.out.println("User ID: " + userId);
        Category category = categoryService.postCategory(request, userId);
        ApiResponse<Category> apiResponse = ApiResponse.<Category>builder().payload(category).status(HttpStatus.CREATED).code(201).message("Create new Category successful").build();
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Category By Id")
    public ResponseEntity<?> updateCategoryById(@PathVariable UUID id, @RequestBody CategoryRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
        UUID userId = customUserDetail.getAppUserDTO().getUserId();
        Category category = categoryService.updateCategoryById(id, userId, request);
        ApiResponse<Category> apiResponse = ApiResponse.<Category>builder().payload(category).message("Update a category with Id : " + id + " successful").status(HttpStatus.OK).code(201).build();
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Category By Id")
    public ResponseEntity<?> deleteCategoryById(@PathVariable UUID id) {
        System.out.println(id + " in delete method of category");
        Category category = categoryService.deleteCategoryById(id);
        ApiResponse<Category> apiResponse = ApiResponse.<Category>builder().payload(category).code(201).status(HttpStatus.OK).message("Delete a category with Id : " + id + " successful").build();
        return ResponseEntity.ok(apiResponse);
    }
}
