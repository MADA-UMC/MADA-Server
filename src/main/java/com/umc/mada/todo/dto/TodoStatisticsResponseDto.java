package com.umc.mada.todo.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TodoStatisticsResponseDto {
    Double todosPercent;
    Double completeTodoPercent;

    @Builder
    public TodoStatisticsResponseDto(Double todosPercent, Double completeTodoPercent){
        this.todosPercent = todosPercent;
        this.completeTodoPercent = completeTodoPercent;
    }

    public static TodoStatisticsResponseDto of(Double todosPercent, Double completeTodoPercent){
        return TodoStatisticsResponseDto.builder()
                .todosPercent(todosPercent)
                .completeTodoPercent(completeTodoPercent)
                .build();
    }
}
