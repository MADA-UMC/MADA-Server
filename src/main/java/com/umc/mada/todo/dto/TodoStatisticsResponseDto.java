package com.umc.mada.todo.dto;

import com.umc.mada.todo.repository.CategoryStatisticsVO;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class TodoStatisticsResponseDto {
    private final String nickName;
    private final Double todosPercent;
    private final Double completeTodoPercent;
    private final List<CategoryStatisticsDto> categoryStatistics;

    @Builder
    public TodoStatisticsResponseDto(String nickName, Double todosPercent, Double completeTodoPercent, List<CategoryStatisticsDto> categoryStatistics){
        this.nickName = nickName;
        this.todosPercent = todosPercent;
        this.completeTodoPercent = completeTodoPercent;
        this.categoryStatistics = categoryStatistics;
    }

    public static TodoStatisticsResponseDto of(String nickName,Double todosPercent, Double completeTodoPercent, List<CategoryStatisticsVO> categoryStatistics){
        return TodoStatisticsResponseDto.builder()
                .nickName(nickName)
                .todosPercent(todosPercent)
                .completeTodoPercent(completeTodoPercent)
                .categoryStatistics(categoryStatistics.stream()
                        .map(i -> CategoryStatisticsDto.of(i.getCategoryName(),i.getColor(), i.getRate()))
                        .collect(Collectors.toList()))
                .build();
    }
}
