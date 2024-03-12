package com.umc.mada.todo.service;

import com.umc.mada.todo.dto.StatisticsResponseDto;
import com.umc.mada.todo.repository.ChartRepository;
import com.umc.mada.todo.repository.statistics.*;
import com.umc.mada.user.domain.User;
import com.umc.mada.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChartService {
    private final ChartRepository chartRepository;
    private final UserRepository userRepository;

    public StatisticsResponseDto dailyStatistics(Authentication authentication, LocalDate date){
        User user  = userRepository.findByAuthId(authentication.getName()).orElseThrow(()-> new RuntimeException("올바른 유저 ID가 아닙니다."));

        //카테고리 통계
        List<CategoryStatisticsVO> categoryStatisticsVOList = chartRepository.statisticsOnCategories(user.getId(), date, date);
        int check = categoryStatisticsVOList.get(0).getCategoryId();
        PreviousCategoryStatisticsVO previousCategoryStatisticsVO = chartRepository.statisticsOnPreviousCategories(user.getId(), date, date, check);

        //막대 그래프 통계
        LocalDate startDate = date.minusDays(3);
        List<TodoBarGraphStatisticsVO> todoBarGraphStatisticsVOList = chartRepository.dayTodoBarGraphStatistics(user.getId(), startDate, date);
        Integer totalCount = chartRepository.countAllByUserIdAndDate(user, date);

        //달성률 통계
        startDate = date.minusDays(5);
        List<AchievementRateStatisticsVO> achievementRateStatisticsVOList = chartRepository.dayStatisticsOnAchievementRate(user.getId(), startDate, date);

        return StatisticsResponseDto.ofDay(categoryStatisticsVOList, previousCategoryStatisticsVO, todoBarGraphStatisticsVOList, totalCount, achievementRateStatisticsVOList);
    }

    public StatisticsResponseDto weeklyStatistics(Authentication authentication, LocalDate date){
        User user  = userRepository.findByAuthId(authentication.getName()).orElseThrow(()-> new RuntimeException("올바른 유저 ID가 아닙니다."));

        //date에 해당하는 주의 시작 날짜와 마지막 날짜 구하기
        LocalDate startDate = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate endDate= date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));

        //카테고리 통계
        List<CategoryStatisticsVO> categoryStatisticsVOList = chartRepository.statisticsOnCategories(user.getId(), startDate, endDate);
        int totalCount = chartRepository.countAllByUserIdAndDateBetween(user, startDate, endDate); //이번주 생성한 투두 개수
        int check = categoryStatisticsVOList.get(0).getCategoryId();
        PreviousCategoryStatisticsVO previousCategoryStatisticsVO = chartRepository.statisticsOnPreviousCategories(user.getId(), startDate, endDate, check);

        //막대 그래프&달성률 통계
        LocalDate startDate2 = date.minusWeeks(6);
        List<WeeklyBarGraphAndRateStatisticsVO> weeklyBarGraphAndRateStatisticsVOList = chartRepository.weeklyTodoBarGraphAndRateStatistics(user.getId(), startDate2, endDate);

        return StatisticsResponseDto.ofWeek(categoryStatisticsVOList, totalCount, previousCategoryStatisticsVO, weeklyBarGraphAndRateStatisticsVOList);
    }

    public StatisticsResponseDto monthlyStatistics(Authentication authentication, LocalDate date){
        User user = userRepository.findByAuthId(authentication.getName()).orElseThrow(()-> new RuntimeException("올바른 유저 ID가 아닙니다."));

        //date에 해당하는 주의 시작 날짜와 마지막 날짜 구하기
        LocalDate startDate = date.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endDate = date.with(TemporalAdjusters.lastDayOfMonth());

        //카테고리 통계
        List<CategoryStatisticsVO> categoryStatisticsVOList = chartRepository.statisticsOnCategories(user.getId(), startDate, endDate);
        int totalCount = chartRepository.countAllByUserIdAndDateBetween(user, startDate, endDate); //이번달 생성한 투두 개수
        int check = categoryStatisticsVOList.get(0).getCategoryId();
        PreviousCategoryStatisticsVO previousCategoryStatisticsVO = chartRepository.statisticsOnPreviousCategories(user.getId(), startDate, endDate, check);

        //막대 그래프&달성률 통계
        LocalDate startDate2 = date.minusMonths(6);
        List<MonthlyBarGraphAndRateStatisticsVO> monthlyBarGraphAndRateStatisticsVOList = chartRepository.monthlyTodoBarGraphAndRateStatistics(user.getId(), startDate2, endDate);

        return StatisticsResponseDto.ofMonth(categoryStatisticsVOList, totalCount, previousCategoryStatisticsVO, monthlyBarGraphAndRateStatisticsVOList);
    }
}
