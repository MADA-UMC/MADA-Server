package com.umc.mada.todo.repository.statistics;

public interface CategoryStatisticsVO {
    Integer getCategoryId();
    String getCategoryName();
    String getColor();
    Integer getCount();
    float getRate();
}
