package com.umc.mada.category.dto;

import lombok.*;

// 카테고리 생성 또는 수정 시 요청 데이터를 전달하는 DTO 클래스
public class CategoryRequestDto {
    private String category_name; // 카테고리명
    private String color; // 카테고리 색상
    private int icon_id; //아이콘 ID

    // 카테고리명 Getter
    public String getCategoryName() {
        return category_name;
    }

    // 카테고리명 Setter
    public void setCategoryName(String category_name) {
        this.category_name = category_name;
    }

    // 카테고리 색상 Getter
    public String getColor() {
        return color;
    }

    // 카테고리 색상 Setter
    public void setColor(String color) {
        this.color = color;
    }

    // 아이콘 ID Getter
    public int getIconId() {
        return icon_id;
    }

    // 아이콘 ID Setter
    public void setIconId(int icon_id) {
        this.icon_id = icon_id;
    }

}