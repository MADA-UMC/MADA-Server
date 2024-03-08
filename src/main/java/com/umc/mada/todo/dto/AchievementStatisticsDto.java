package com.umc.mada.todo.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class AchievementStatisticsDto {
    private final LocalDate date;
    private final float achievementRate;

    public static AchievementStatisticsDto of(LocalDate date, float achievementRate){
        return AchievementStatisticsDto.builder()
                .date(date)
                .achievementRate(achievementRate)
                .build();
    }
}
