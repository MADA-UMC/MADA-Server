package com.umc.mada.todo.repository.statistics;

import java.time.LocalDate;

public interface TodoBarGraphStatisticsVO {
    LocalDate getTodoDate();
    Integer getCount();
}
