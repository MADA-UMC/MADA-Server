package com.umc.mada.category.dto;

import lombok.*;
import reactor.util.annotation.Nullable;

import java.time.LocalDateTime;

@Data
public class CategoryRequestDto {
    private String categoryName; // 카테고리명
    private String color;
    private Integer iconId;
    private Boolean isInActive; // 카테고리 종료 여부
    @Nullable
    private LocalDateTime inActiveTime; // 카테고리 종료 시점
    private Boolean isDeleted; // 카테고리 삭제 여부
}