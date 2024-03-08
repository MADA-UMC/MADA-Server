package com.umc.mada.todo.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class TodoStatisticsDto {
    private final LocalDate date;
    private final int countCompleted;

    public static TodoStatisticsDto of(LocalDate date, int count){
        return TodoStatisticsDto.builder()
                .date(date)
                .countCompleted(count)
                .build();
    }
}
