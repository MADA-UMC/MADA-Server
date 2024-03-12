package com.umc.mada.todo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class TodoStatisticsDto {
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private final LocalDate date;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private final LocalDate startDate;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private final LocalDate endDate;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private final String monthDate;
    private final int countCompleted;

    public static TodoStatisticsDto of(LocalDate date, int count){
        return TodoStatisticsDto.builder()
                .date(date)
                .countCompleted(count)
                .build();
    }

    public static TodoStatisticsDto of(LocalDate startDate, LocalDate endDate, int count){
        return TodoStatisticsDto.builder()
                .startDate(startDate)
                .endDate(endDate)
                .countCompleted(count)
                .build();
    }

    public static TodoStatisticsDto of(String monthDate, int count){
        return TodoStatisticsDto.builder()
                .monthDate(monthDate)
                .countCompleted(count)
                .build();
    }
}
