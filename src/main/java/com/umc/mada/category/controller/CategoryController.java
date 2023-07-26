package com.umc.mada.category.controller;


import com.umc.mada.category.dto.CategoryRequestDto;
import com.umc.mada.category.dto.CategoryResponseDto;
import com.umc.mada.category.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.umc.mada.global.BaseResponse;
import com.umc.mada.global.BaseResponseStatus;

import java.util.List;

@RestController
@RequestMapping("/api/home/category")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public BaseResponse<String> createCategory(@RequestBody CategoryRequestDto categoryRequestDto) {
        // 카테고리 생성 API
        categoryService.createCategory(categoryRequestDto);
        return new BaseResponse<>("카테고리 생성 완료");
    }

    @PatchMapping("/{category_id}")
    public BaseResponse<CategoryResponseDto> updateCategory(@PathVariable int category_id, @RequestBody CategoryRequestDto categoryRequestDto) {
        // 카테고리 수정 API
        CategoryResponseDto updatedCategory = categoryService.updateCategory(category_id, categoryRequestDto);
        if (updatedCategory != null) {
            return new BaseResponse<>(updatedCategory);
        } else {
            return new BaseResponse<>(BaseResponseStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{category_id}")
    public BaseResponse<String> deleteCategory(@PathVariable int category_id) {
        // 카테고리 삭제 API
        categoryService.deleteCategory(category_id);
        return new BaseResponse<>("카테고리 삭제 완료");
    }

    @GetMapping
    public BaseResponse<List<CategoryResponseDto>> getAllCategories() {
        // 모든 카테고리 목록 조회 API
        List<CategoryResponseDto> categories = categoryService.getAllCategories();
        return new BaseResponse<>(categories);
    }

    @GetMapping("/{category_id}")
    public BaseResponse<CategoryResponseDto> getCategoryById(@PathVariable int category_id) {
        // 특정 카테고리 조회 API
        try {
            CategoryResponseDto category = categoryService.getCategoryById(category_id);
            return new BaseResponse<>(category);
        } catch (IllegalArgumentException e) {
            return new BaseResponse<>(BaseResponseStatus.NOT_FOUND);
        }
    }
}

