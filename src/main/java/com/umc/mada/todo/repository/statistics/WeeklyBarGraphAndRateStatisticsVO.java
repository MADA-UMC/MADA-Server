package com.umc.mada.todo.repository.statistics;

import java.time.LocalDate;

public interface WeeklyBarGraphAndRateStatisticsVO {
    LocalDate getStartDate();
    LocalDate getEndDate();
    String getWeekDate();
    Integer getCount();
    float getRate();
}
