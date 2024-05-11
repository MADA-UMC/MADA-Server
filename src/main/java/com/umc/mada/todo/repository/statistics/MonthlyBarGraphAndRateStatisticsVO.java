package com.umc.mada.todo.repository.statistics;

public interface MonthlyBarGraphAndRateStatisticsVO {
    String getMonthDate();
    int getCount();
    float getRate();
}
