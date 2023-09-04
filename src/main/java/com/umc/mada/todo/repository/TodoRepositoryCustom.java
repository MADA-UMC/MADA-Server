package com.umc.mada.todo.repository;

import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TodoRepositoryCustom {
    List<StatisticsVO> getStatistics(Long uid, String weekOrMonth,LocalDate endDate, LocalDate startDate);
}
