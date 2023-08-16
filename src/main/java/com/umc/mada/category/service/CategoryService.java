package com.umc.mada.category.service;

import com.umc.mada.category.domain.Category;
import com.umc.mada.category.dto.CategoryRequestDto;
import com.umc.mada.category.dto.CategoryResponseDto;
import com.umc.mada.category.repository.CategoryRepository;
import com.umc.mada.file.domain.File;
import com.umc.mada.file.repository.FileRepository;
import com.umc.mada.user.domain.User;
import com.umc.mada.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.umc.mada.global.BaseResponseStatus;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final FileRepository fileRepository;
    private final UserRepository userRepository;

    @Autowired
    public CategoryService(UserRepository userRepository, CategoryRepository categoryRepository, FileRepository fileRepository) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.fileRepository = fileRepository;
    }

    /**
     * 카테고리 생성 로직
     *
     * @param categoryRequestDto 카테고리 생성 요청 데이터
     * @return 생성된 카테고리 정보
     */
    public CategoryResponseDto createCategory(User user, CategoryRequestDto categoryRequestDto) {
        // 카테고리 이름이 없거나 길이를 초과한 경우 예외 처리
        validateUserId(user);
        validateCategoryName(categoryRequestDto.getCategoryName());

        // 존재하지 않는 아이콘인 경우 예외 처리
        //validateIconId(categoryRequestDto.getIcon_id());

        File icon = fileRepository.findFileById(categoryRequestDto.getIconId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이콘 ID입니다."));

        // Category 엔티티 생성
        Category category = new Category(user, categoryRequestDto.getCategoryName(), categoryRequestDto.getColor(), icon);
        // 카테고리를 저장하고 저장된 카테고리 엔티티 반환
        Category savedCategory = categoryRepository.save(category);
        // 저장된 카테고리 정보를 기반으로 CategoryResponseDto 생성하여 반환
        return CategoryResponseDto.of(savedCategory);
    }

    /**
     * 카테고리 수정 로직
     *
     * @param categoryId 카테고리 ID
     * @param categoryRequestDto 카테고리 수정 요청 데이터
     * @return 수정된 카테고리 정보
     */
    @Transactional
    public CategoryResponseDto updateCategory(User user, int categoryId, CategoryRequestDto categoryRequestDto) {
        // 주어진 categoryId가 유효한지 검사
        if (categoryId <= 0) {
            throw new IllegalArgumentException("유효하지 않은 카테고리 ID입니다.");
        }

        // 주어진 categoryId를 이용하여 카테고리 엔티티 조회
        Category category = categoryRepository.findCategoryByUserIdAndId(user, categoryId)
                .orElseThrow(() -> new IllegalArgumentException("NOT_FOUND_ERROR"));

        // 이름 변경 처리
        if (categoryRequestDto.getCategoryName() != null) {
            category.setCategoryName(categoryRequestDto.getCategoryName());
        }

        // 색상 변경 처리
        if (categoryRequestDto.getColor() != null) {
            category.setColor(categoryRequestDto.getColor());
        }

        // iconId 변경 처리
        File icon = fileRepository.findById(categoryRequestDto.getIconId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이콘 ID입니다."));
        category.setIcon(icon);
//        if (categoryRequestDto.getIconId() != null) {
//            File icon = fileRepository.findById(categoryRequestDto.getIconId())
//                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이콘 ID입니다."));
//            category.setIconId(icon);
//        }

        // 수정된 카테고리를 저장하고 저장된 카테고리 엔티티 반환
        Category updatedCategory = categoryRepository.save(category);

        // 저장된 카테고리 정보를 기반으로 CategoryResponseDto 생성하여 반환
        return CategoryResponseDto.of(updatedCategory);
    }

    /**
     * 카테고리 삭제 로직
     *
     * @param categoryId 카테고리 ID
     */
    public void deleteCategory(User userId, int categoryId) {
        // 주어진 categoryId를 이용하여 카테고리 엔티티 조회
        Optional<Category> optionalCategory = categoryRepository.deleteCategoryByUserIdAndId(userId, categoryId);
        if (optionalCategory.isPresent()) {
            // 주어진 categoryId에 해당하는 카테고리가 존재하는 경우 삭제
            categoryRepository.deleteCategoryByUserIdAndId(userId, categoryId);
        } else {
            // 해당 ID의 카테고리가 존재하지 않을 경우에 대한 처리 (예외 처리 등)
            throw new IllegalArgumentException(BaseResponseStatus.NOT_FOUND.getMessage());
        }
    }

    /**
     * 사용자 카테고리 목록 조회 로직
     *
     * @return 해당 유저의 카테고리 정보 목록
     */
    public List<CategoryResponseDto> getUserCategories(User userId) {
        // 모든 카테고리 엔티티 조회
        List<Category> userCategories = categoryRepository.findCategoriesByUserId(userId);
        // 각 카테고리 엔티티를 CategoryResponseDto로 매핑하여 리스트로 반환
        return userCategories.stream()
                .map(CategoryResponseDto::of)
                .collect(Collectors.toList());
    }

    // 유저 ID 유효성 검사 메서드
    private void validateUserId(User userId) {
        Optional<User> optionalUser = userRepository.findUserById(userId.getId());
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 유저 ID입니다.");
        }
    }

    // 카테고리 이름 유효성 검사 메서드
    private void validateCategoryName(String categoryName) {
        if (categoryName == null || categoryName.isEmpty()) {
            throw new IllegalArgumentException("카테고리 이름이 없거나 길이를 초과하였습니다.");
        }
    }

    // 아이콘 ID 유효성 검사 메서드
    private void validateIconId(int iconId) {
        Optional<File> optionalFile = fileRepository.findFileById(iconId);
        if (optionalFile.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 아이콘 ID입니다.");
        }
    }
}
