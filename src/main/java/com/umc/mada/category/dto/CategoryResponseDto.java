package com.umc.mada.category.dto;

import com.umc.mada.category.domain.Category;
import lombok.*;
import reactor.util.annotation.Nullable;

import java.time.LocalDateTime;

@Getter
@Setter
//@AllArgsConstructor
@NoArgsConstructor
@Data

public class CategoryResponseDto {
    private int id;
    private String categoryName; // 카테고리명
    private String color; // 카테고리 색상
    private Boolean isInActive; // 카테고리 유효 여부
    private LocalDateTime inActiveTime; // 카테고리 종료 시점
    private Boolean isDeleted; //카테고리 삭제 여부
    private Integer iconId; //아이콘 ID
    @Builder
    public CategoryResponseDto(int id, String categoryName, String color, Boolean isInActive, LocalDateTime inActiveTime, Boolean isDeleted,  Integer iconId){
        this.id = id;
        this.categoryName = categoryName;
        this.color = color;
        this.isInActive = isInActive;
        this.inActiveTime = inActiveTime;
        this.isDeleted = isDeleted;
        this.iconId = iconId;
    }
    public static CategoryResponseDto of(Category category){
        return CategoryResponseDto.builder()
                .id(category.getId())
                .categoryName(category.getCategoryName())
                .color(category.getColor())
                .isInActive(category.getIsInActive())
                .inActiveTime(category.getInActiveTime())
                .isDeleted(category.getIsDeleted())
                .iconId(category.getIcon().getId())
                .build();
    }
}
