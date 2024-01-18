package com.umc.mada.todo.dto;

import com.umc.mada.todo.domain.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
public class RepeatTodoResponseDto {
    private int id;
    private int todoId;
    private LocalDate date;
    private Boolean complete;

    @Builder
    public RepeatTodoResponseDto(int id, int todoId, LocalDate date, Boolean complete){
        this.id = id;
        this.todoId = todoId;
        this.date = date;
        this.complete = complete;
    }

    public static RepeatTodoResponseDto of(RepeatTodo repeatTodo) {
        return RepeatTodoResponseDto.builder()
                .id(repeatTodo.getId())
                .todoId(repeatTodo.getTodoId().getId())
                .date(repeatTodo.getDate())
                .complete(repeatTodo.getComplete())
                .build();
    }
}


