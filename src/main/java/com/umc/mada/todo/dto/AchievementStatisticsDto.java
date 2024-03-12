package com.umc.mada.todo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class AchievementStatisticsDto {
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private final LocalDate date;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private final LocalDate startDate;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private final LocalDate endDate;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private final String monthDate;
    private final float achievementRate;

    public static AchievementStatisticsDto of(LocalDate date, float achievementRate){
        return AchievementStatisticsDto.builder()
                .date(date)
                .achievementRate(achievementRate)
                .build();
    }

    public static AchievementStatisticsDto of(LocalDate startDate, LocalDate endDate, float achievementRate){
        return AchievementStatisticsDto.builder()
                .startDate(startDate)
                .endDate(endDate)
                .achievementRate(achievementRate)
                .build();
    }

    public static AchievementStatisticsDto of(String monthDate, float achievementRate){
        return AchievementStatisticsDto.builder()
                .monthDate(monthDate)
                .achievementRate(achievementRate)
                .build();
    }
}
