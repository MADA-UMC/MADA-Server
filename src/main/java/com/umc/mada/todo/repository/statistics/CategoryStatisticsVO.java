package com.umc.mada.todo.repository.statistics;

public interface CategoryStatisticsVO {
    String getCategoryName();
    String getColor();
    Integer getCount();
    float getRate();
    Integer getCategoryId();
}
