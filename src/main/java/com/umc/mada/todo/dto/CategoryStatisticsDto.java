package com.umc.mada.todo.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CategoryStatisticsDto {
    private final String categoryName;
    private final String color;
    private final Float rate;

    public static CategoryStatisticsDto of(String categoryName, String color, Float rate){
        return CategoryStatisticsDto.builder()
                .categoryName(categoryName)
                .color(color)
                .rate(rate)
                .build();
    }
}
