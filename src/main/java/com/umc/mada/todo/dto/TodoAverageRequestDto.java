package com.umc.mada.todo.dto;

import lombok.Getter;
import java.time.LocalDate;

@Getter
public class TodoAverageRequestDto {
    // month, week
    private String option;
    private LocalDate date;
}
