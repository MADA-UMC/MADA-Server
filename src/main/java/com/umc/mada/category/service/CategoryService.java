package com.umc.mada.category.service;


import com.umc.mada.category.domain.Category;
import com.umc.mada.category.dto.CategoryRequestDto;
import com.umc.mada.category.dto.CategoryResponseDto;
import com.umc.mada.category.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * 카테고리 생성 로직
     *
     * @param categoryRequestDto 카테고리 생성 요청 데이터
     * @return 생성된 카테고리 정보
     */
    public CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto) {
        // 카테고리 이름이 없거나 길이를 초과한 경우 예외 처리
        validateCategoryName(categoryRequestDto.getCategoryName());

        // 존재하지 않는 아이콘인 경우 예외 처리
        if (!isValidIconId(categoryRequestDto.getIconId())) {
            throw new IllegalArgumentException("존재하지 않는 아이콘입니다.");
        }

        // Category 엔티티 생성
        Category category = new Category(categoryRequestDto.getCategoryName(), categoryRequestDto.getColor(), categoryRequestDto.getIconId());
        // 카테고리를 저장하고 저장된 카테고리 엔티티 반환
        Category savedCategory = categoryRepository.save(category);
        // 저장된 카테고리 정보를 기반으로 CategoryResponseDto 생성하여 반환
        return new CategoryResponseDto(savedCategory.getCategoryName(), savedCategory.getColor(), savedCategory.getId());
    }

    /**
     * 카테고리 수정 로직
     *
     * @param category_id 카테고리 ID
     * @param categoryRequestDto 카테고리 수정 요청 데이터
     * @return 수정된 카테고리 정보
     */
    public CategoryResponseDto updateCategory(int category_id, CategoryRequestDto categoryRequestDto) {
        // 주어진 categoryId를 이용하여 카테고리 엔티티 조회
        Optional<Category> optionalCategory = categoryRepository.findById(category_id);
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();

            // 카테고리 이름이 없거나 길이를 초과한 경우 예외 처리
            validateCategoryName(categoryRequestDto.getCategoryName());

            // 요청 데이터로 카테고리 엔티티 수정
            category.setCategoryName(categoryRequestDto.getCategoryName());
            category.setColor(categoryRequestDto.getColor());
            // 수정된 카테고리를 저장하고 저장된 카테고리 엔티티 반환
            Category updatedCategory = categoryRepository.save(category);
            // 저장된 카테고리 정보를 기반으로 CategoryResponseDto 생성하여 반환
            return new CategoryResponseDto(updatedCategory.getCategoryName(), updatedCategory.getColor(), updatedCategory.getId());
        }
        // 해당 ID의 카테고리가 존재하지 않을 경우에 대한 처리 (예외 처리 등)
        throw new IllegalArgumentException("존재하지 않는 카테고리입니다.");
    }

    /**
     * 카테고리 삭제 로직
     *
     * @param category_id 카테고리 ID
     */
    public void deleteCategory(int category_id) {
        // 주어진 categoryId를 이용하여 카테고리 엔티티 조회
        Optional<Category> optionalCategory = categoryRepository.findById(category_id);
        if (optionalCategory.isPresent()) {
            // 주어진 categoryId에 해당하는 카테고리가 존재하는 경우 삭제
            categoryRepository.deleteById(category_id);
        } else {
            // 해당 ID의 카테고리가 존재하지 않을 경우에 대한 처리 (예외 처리 등)
            throw new IllegalArgumentException("존재하지 않는 카테고리입니다.");
        }
    }

    /**
     * 모든 카테고리 목록 조회 로직
     *
     * @return 모든 카테고리 정보 목록
     */
    public List<CategoryResponseDto> getAllCategories() {
        // 모든 카테고리 엔티티 조회
        List<Category> categories = categoryRepository.findAll();
        // 각 카테고리 엔티티를 CategoryResponseDto로 매핑하여 리스트로 반환
        return categories.stream()
                .map(category -> new CategoryResponseDto(category.getCategoryName(), category.getColor(), category.getId()))
                .collect(Collectors.toList());
    }

    /**
     * 특정 카테고리 조회 로직
     *
     * @param category_id 카테고리 ID
     * @return 특정 카테고리 정보
     */
    public CategoryResponseDto getCategoryById(int category_id) {
        // 주어진 categoryId를 이용하여 카테고리 엔티티 조회
        Optional<Category> optionalCategory = categoryRepository.findById(category_id);
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            // 카테고리 엔티티 정보를 기반으로 CategoryResponseDto 생성하여 반환
            return new CategoryResponseDto(category.getCategoryName(), category.getColor(), category.getId());
        }
        // 해당 ID의 카테고리가 존재하지 않을 경우에 대한 처리 (예외 처리 등)
        throw new IllegalArgumentException("존재하지 않는 카테고리입니다.");
    }

    // 카테고리 이름 유효성 검사 메서드
    private void validateCategoryName(String categoryName) {
        if (categoryName == null || categoryName.isEmpty() || categoryName.length() > 45) {
            throw new IllegalArgumentException("카테고리 이름이 없거나 길이를 초과하였습니다.");
        }
    }

    // 아이콘 ID 유효성 검사 메서드
    private boolean isValidIconId(int iconId) {
        // 아이콘 ID가 유효한지 여부를 확인하는 로직을 추가하여 해당 아이콘 ID가 존재하는지 체크
        // 여기서는 아이콘 ID가 1부터 1000까지의 범위라고 가정
        return iconId >= 1 && iconId <= 1000;
    }
}
