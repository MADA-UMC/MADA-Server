package com.umc.mada.category.controller;


import com.umc.mada.category.dto.CategoryRequestDto;
import com.umc.mada.category.dto.CategoryResponseDto;
import com.umc.mada.category.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
    public ResponseEntity<String> createCategory(@RequestBody CategoryRequestDto categoryRequestDto) {
        // 카테고리 생성 API
        categoryService.createCategory(categoryRequestDto);
        return new ResponseEntity<>("카테고리 생성 완료", HttpStatus.CREATED);
    }

    @PatchMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseDto> updateCategory(@PathVariable int categoryId, @RequestBody CategoryRequestDto categoryRequestDto) {
        // 카테고리 수정 API
        CategoryResponseDto updatedCategory = categoryService.updateCategory(categoryId, categoryRequestDto);
        if (updatedCategory != null) {
            return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable int categoryId) {
        // 카테고리 삭제 API
        categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>("카테고리 삭제 완료", HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {
        // 모든 카테고리 목록 조회 API
        List<CategoryResponseDto> categories = categoryService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseDto> getCategoryById(@PathVariable int categoryId) {
        // 특정 카테고리 조회 API
        try {
            CategoryResponseDto category = categoryService.getCategoryById(categoryId);
            return new ResponseEntity<>(category, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

