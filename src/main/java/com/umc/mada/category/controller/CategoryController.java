package com.umc.mada.category.controller;


import com.umc.mada.category.dto.CategoryRequestDto;
import com.umc.mada.category.dto.CategoryResponseDto;
import com.umc.mada.category.service.CategoryService;
import com.umc.mada.user.domain.User;
import com.umc.mada.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/home/category")
public class CategoryController {

    private final CategoryService categoryService;
    private final UserRepository userRepository;

    @Autowired
    public CategoryController(CategoryService categoryService, UserRepository userRepository) {
        this.categoryService = categoryService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createCategory(Authentication authentication, @Valid @RequestBody CategoryRequestDto categoryRequestDto) {
        // 카테고리 생성 API
        Optional<User> userOptional = userRepository.findByAuthId(authentication.getName());
        User user = userOptional.get();
        CategoryResponseDto newCategory = categoryService.createCategory(user, categoryRequestDto);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("Category", newCategory);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("data", data);
        //result.put("status", 200);
        //result.put("success", true);
        //result.put("message", "카테고리 생성이 완료되었습니다.");
        return ResponseEntity.ok().body(result);
    }

    @PatchMapping("/{categoryId}")
    public ResponseEntity<Map<String, Object>> updateCategory(Authentication authentication,@PathVariable int categoryId, @Valid @RequestBody CategoryRequestDto categoryRequestDto) {
        // 카테고리 수정 API
        Optional<User> userOptional = userRepository.findByAuthId(authentication.getName());
        User user = userOptional.get();
        CategoryResponseDto updatedCategory = categoryService.updateCategory(user, categoryId, categoryRequestDto);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("Category", updatedCategory);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("data", data);
        //result.put("status", 200);
        //result.put("success", true);
        //result.put("message", "카테고리 수정이 완료되었습니다.");
        if (updatedCategory != null) {
            return ResponseEntity.ok().body(result);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("delete/{categoryId}")
    public ResponseEntity<Map<String, Object>> deleteCategory(Authentication authentication, @PathVariable int categoryId) {
        // 카테고리 삭제 API
        try{
            Optional<User> userOptional = userRepository.findByAuthId(authentication.getName());
            User user = userOptional.get();
            categoryService.deleteCategory(user,categoryId);
            Map<String, Object> result = new LinkedHashMap<>();
            //result.put("status", 200);
            //result.put("success", true);
            //result.put("message", "카테고리 삭제가 완료되었습니다.");
            return ResponseEntity.ok().body(result);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("active/{categoryId}")
    public ResponseEntity<Map<String, Object>> activeCategory(Authentication authentication, @PathVariable int categoryId) {
        //카테고리 종료 API
        try{
            Optional<User> userOptional = userRepository.findByAuthId(authentication.getName());
            User user = userOptional.get();
            categoryService.activeCategory(user, categoryId);
            Map<String, Object> result = new LinkedHashMap<>();
            return ResponseEntity.ok().body(result);
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getUserCategories(Authentication authentication) {
        // 특정 유저 카테고리 목록 조회 API
        try {
            Optional<User> userOptional = userRepository.findByAuthId(authentication.getName());
            User user = userOptional.get();
            List<CategoryResponseDto> userCategories = categoryService.getUserCategories(user);
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("CategoryList", userCategories);
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("data", data);
            //result.put("status", 200);
            //result.put("success", true);
            //result.put("message", "카테고리가 정상적으로 조회되었습니다.");
            return ResponseEntity.ok().body(result);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

//    @GetMapping
//    public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {
//        // 모든 유저 카테고리 목록 조회 API
//        List<CategoryResponseDto> categories = categoryService.getAllCategories();
//        return new ResponseEntity<>(categories, HttpStatus.OK);
//    }
//
//    @GetMapping("/{categoryId}")
//    public ResponseEntity<CategoryResponseDto> getCategoryById(@PathVariable int categoryId) {
//        // 특정 카테고리 조회 API
//        try {
//            CategoryResponseDto category = categoryService.getCategoryById(categoryId);
//            return new ResponseEntity<>(category, HttpStatus.OK);
//        } catch (IllegalArgumentException e) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
}

