package com.umc.mada.todo.repository.statistics;

public class DefaultPreviousCategoryStatisticsVO implements PreviousCategoryStatisticsVO{
    private Integer count;

    public DefaultPreviousCategoryStatisticsVO() {
        this.count = 0;
    }

    @Override
    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
