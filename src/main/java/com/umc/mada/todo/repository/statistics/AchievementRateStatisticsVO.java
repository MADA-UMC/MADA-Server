package com.umc.mada.todo.repository.statistics;

import java.time.LocalDate;

public interface AchievementRateStatisticsVO {
    LocalDate getDate();
    Integer getCount();
    float getRate();

}
