package com.umc.mada.todo.service;

import com.umc.mada.todo.dto.StatisticsResponseDto;
import com.umc.mada.todo.repository.ChartRepository;
import com.umc.mada.todo.repository.RepeatTodoRepository;
import com.umc.mada.todo.repository.TodoRepository;
import com.umc.mada.todo.repository.statistics.*;
import com.umc.mada.user.domain.User;
import com.umc.mada.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ChartService {
    private final ChartRepository chartRepository;
    private final UserRepository userRepository;
    private final TodoRepository todoRepository;
    private final RepeatTodoRepository repeatTodoRepository;

    public StatisticsResponseDto dailyStatistics(Authentication authentication, LocalDate date){
        User user  = userRepository.findByAuthId(authentication.getName()).orElseThrow(()-> new RuntimeException("올바른 유저 ID가 아닙니다."));

        //카테고리 통계
        List<CategoryStatisticsVO> categoryStatisticsVOList = chartRepository.statisticsOnCategories(user.getId(), date, date);

        PreviousCategoryStatisticsVO previousCategoryStatisticsVO;
        if (!categoryStatisticsVOList.isEmpty()) {
            int check = categoryStatisticsVOList.get(0).getCategoryId();
            previousCategoryStatisticsVO = chartRepository.statisticsOnPreviousCategories(user.getId(), date, date, check);
        } else {
            previousCategoryStatisticsVO = new DefaultPreviousCategoryStatisticsVO();
        }

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

        PreviousCategoryStatisticsVO previousCategoryStatisticsVO;
        if (!categoryStatisticsVOList.isEmpty()) {
            int check = categoryStatisticsVOList.get(0).getCategoryId();
            previousCategoryStatisticsVO = chartRepository.statisticsOnPreviousCategories(user.getId(), startDate, endDate, check);
        } else {
            // categoryStatisticsVOList가 비어 있을 때
            previousCategoryStatisticsVO = new DefaultPreviousCategoryStatisticsVO();
        }

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

        PreviousCategoryStatisticsVO previousCategoryStatisticsVO;
        if (!categoryStatisticsVOList.isEmpty()) {
            int check = categoryStatisticsVOList.get(0).getCategoryId();
            previousCategoryStatisticsVO = chartRepository.statisticsOnPreviousCategories(user.getId(), startDate, endDate, check);
        } else {
            // categoryStatisticsVOList가 비어 있을 때
            previousCategoryStatisticsVO = new DefaultPreviousCategoryStatisticsVO();
        }

        //막대 그래프&달성률 통계
        LocalDate startDate2 = date.minusMonths(6);
        List<MonthlyBarGraphAndRateStatisticsVO> monthlyBarGraphAndRateStatisticsVOList = chartRepository.monthlyTodoBarGraphAndRateStatistics(user.getId(), startDate2, endDate);

        return StatisticsResponseDto.ofMonth(categoryStatisticsVOList, totalCount, previousCategoryStatisticsVO, monthlyBarGraphAndRateStatisticsVOList);
    }

    public Map<String, Object> findDatesWithTodosByMonth(Authentication authentication, YearMonth yearMonth) {
        User user = userRepository.findByAuthId(authentication.getName()).orElseThrow(()-> new RuntimeException("올바른 유저 ID가 아닙니다."));

        // 투두
        Set<Integer> datesWithTodosSet = new HashSet<>(todoRepository.findDistinctDaysByUserIdAndYearMonth(user.getId(), yearMonth.toString()));

        // 반복 투두
        datesWithTodosSet.addAll(repeatTodoRepository.findDistinctDaysByUserIdAndYearMonth(user.getId(), yearMonth.toString()));

        List<Integer> datesWithTodos = new ArrayList<>(datesWithTodosSet);
        Collections.sort(datesWithTodos);

        Map<String, Object> map = new LinkedHashMap<>();
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("datesWithTodos", datesWithTodos);
        map.put("data", data);
        return map;
    }
}
