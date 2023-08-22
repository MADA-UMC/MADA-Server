package com.umc.mada.todo.dto;

import lombok.Builder;

@Builder
public class TodoAverageResponseDto {
    Double todosPercent;
    Double completeTodoPercent;
}
