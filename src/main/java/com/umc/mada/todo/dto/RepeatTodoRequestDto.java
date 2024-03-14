package com.umc.mada.todo.dto;

import lombok.Data;

@Data
public class RepeatTodoRequestDto {
    private Boolean complete; // 완료 여부 (y: 오늘 할 일 완료, n: 오늘 할 일 미완료)
}
