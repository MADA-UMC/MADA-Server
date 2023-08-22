package com.umc.mada.todo.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TodoAverageResponseDto {
    Double todosPercent;
    Double completeTodoPercent;
}
