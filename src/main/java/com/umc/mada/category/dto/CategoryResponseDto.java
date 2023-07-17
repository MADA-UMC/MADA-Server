package com.umc.mada.category.dto;

import lombok.*;

// 카테고리 조회 시 응답 데이터를 전달하는 DTO 클래스
public class CategoryResponseDto {
    private String category_name; // 카테고리명
    private String color; // 카테고리 색상
    private int icon_Id; // 아이콘 ID

    // 생성자
    public CategoryResponseDto(String category_name, String color, int icon_Id) {
        this.category_name = category_name;
        this.color = color;
        this.icon_Id = icon_Id;
    }

    // 카테고리명 Getter
    public String getCategoryName() {
        return category_name;
    }

    // 카테고리 색상 Getter
    public String getColor() {
        return color;
    }

    // 아이콘 ID Getter
    public int getIconId() {
        return icon_Id;
    }

}
