package com.umc.mada.todo.dto;

import lombok.Builder;
import lombok.Getter;

import java.awt.datatransfer.FlavorEvent;

@Getter
public class CategoryStatisticsDto {
    private final String categoryName;
    private final String color;
    private final Float rate;

    @Builder
    public CategoryStatisticsDto(String categoryName, String color, Float rate) {
        this.categoryName = categoryName;
        this.color = color;
        this.rate = rate;
    }

    public static CategoryStatisticsDto of(String categoryName, String color, Float rate){
        return CategoryStatisticsDto.builder()
                .categoryName(categoryName)
                .color(color)
                .rate(rate)
                .build();
    }
}
