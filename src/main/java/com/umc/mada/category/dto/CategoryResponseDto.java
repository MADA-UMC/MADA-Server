package com.umc.mada.category.dto;

import com.umc.mada.category.domain.Category;
import lombok.*;

@Getter
@Setter
//@AllArgsConstructor
@NoArgsConstructor
@Data
// 카테고리 조회 시 응답 데이터를 전달하는 DTO 클래스
public class CategoryResponseDto {
    private int id;
    private String categoryName; // 카테고리명
    private String color; // 카테고리 색상
    private int iconId; //아이콘 ID
    @Builder
    public CategoryResponseDto(int id, String categoryName, String color, Integer iconId){
        this.id = id;
        this.categoryName = categoryName;
        this.color = color;
        this.iconId = iconId;
    }
    public static CategoryResponseDto of(Category category){
        return CategoryResponseDto.builder()
                .id(category.getId())
                .categoryName(category.getCategoryName())
                .color(category.getColor())
                .iconId(category.getIcon().getId())
                .build();
    }
}
