package com.umc.mada.todo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.umc.mada.todo.repository.statistics.*;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Collections;

@Builder
@Getter
public class StatisticsResponseDto {
    private final List<CategoryStatisticsDto> categoryStatistics;
    private final String mostCategory; //가장 많이 완료한 카테고리
    private final float nowCategoryCount; //오늘, 이번주, 이번달 가장 많이 완료한 카테고리 수
    private final float beforeCategoryCount;

    private final List<TodoStatisticsDto> todoStatistics;
    private final float nowTotalCount; //오늘, 이번주, 이번달 전체 투두 수
    private final float nowCountCompleted; // 완료한 투두 수
    private final float diffCount; //어제, 지난주, 지난 달에 완료한 투두 수보다 얼마나 더 많이 했는지

    private final List<AchievementStatisticsDto> achievementStatistics;
    private final float nowAchievementRate; //오늘, 이번주, 이번달 투두 달성도의 상승, 하락 비율
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private final float nowCountCompletedA;

    public static StatisticsResponseDto ofDay(List<CategoryStatisticsVO> categoryStatisticsVOList, PreviousCategoryStatisticsVO previousCategoryStatisticsVO,
                                           List<TodoBarGraphStatisticsVO> todoBarGraphStatisticsVOList, int totalCount, List<AchievementRateStatisticsVO> achievementRateStatisticsVOList){
        if (!categoryStatisticsVOList.isEmpty()) {
            //각 list의 index 0은 오늘, 1은 어제이다.
            return StatisticsResponseDto.builder()
                    .categoryStatistics(categoryStatisticsVOList.stream().map(vo -> CategoryStatisticsDto.of(vo.getCategoryName(), vo.getColor(), vo.getRate())).collect(Collectors.toList()))
                    .mostCategory(categoryStatisticsVOList.get(0).getCategoryName())
                    .nowCategoryCount(categoryStatisticsVOList.get(0).getCount())
                    .beforeCategoryCount(previousCategoryStatisticsVO.getCount())
                    .todoStatistics(todoBarGraphStatisticsVOList.stream().map(vo -> TodoStatisticsDto.of(vo.getTodoDate(), vo.getCount())).collect(Collectors.toList()))
                    .nowTotalCount(totalCount)
                    .nowCountCompleted(todoBarGraphStatisticsVOList.get(0).getCount())
                    .diffCount(todoBarGraphStatisticsVOList.get(0).getCount() - todoBarGraphStatisticsVOList.get(1).getCount())
                    .achievementStatistics(achievementRateStatisticsVOList.stream().map(vo -> AchievementStatisticsDto.of(vo.getDate(), vo.getRate())).collect(Collectors.toList()))
                    .nowAchievementRate(achievementRateStatisticsVOList.get(0).getRate() - achievementRateStatisticsVOList.get(1).getRate())
                    .nowCountCompletedA(achievementRateStatisticsVOList.get(0).getCount())
                    .build();
        } else{
            return StatisticsResponseDto.builder()
                    .categoryStatistics(Collections.emptyList())
                    .mostCategory("기본 카테고리")
                    .nowCategoryCount(0)
                    .beforeCategoryCount(0)
                    .todoStatistics(Collections.emptyList())
                    .nowTotalCount(0)
                    .nowCountCompleted(0)
                    .diffCount(0)
                    .achievementStatistics(Collections.emptyList())
                    .nowAchievementRate(0.0f)
                    .nowCountCompletedA(0.0f)
                    .build();
        }
    }

    public static StatisticsResponseDto ofWeek(List<CategoryStatisticsVO> categoryStatisticsVOList, int totalCount,  PreviousCategoryStatisticsVO previousCategoryStatisticsVO,
                                               List<WeeklyBarGraphAndRateStatisticsVO> weeklyBarGraphAndRateStatisticsVOList){
        if (!categoryStatisticsVOList.isEmpty()) {
            return StatisticsResponseDto.builder()
                    .categoryStatistics(categoryStatisticsVOList.stream().map(vo -> CategoryStatisticsDto.of(vo.getCategoryName(), vo.getColor(), vo.getRate())).collect(Collectors.toList()))
                    .mostCategory(categoryStatisticsVOList.get(0).getCategoryName())
                    .nowCategoryCount(categoryStatisticsVOList.get(0).getCount())
                    .beforeCategoryCount(previousCategoryStatisticsVO.getCount())
                    .todoStatistics(weeklyBarGraphAndRateStatisticsVOList.stream().map(vo -> TodoStatisticsDto.of(vo.getStartDate(), vo.getEndDate(), vo.getCount())).collect(Collectors.toList()))
                    .nowTotalCount(totalCount)
                    .nowCountCompleted(weeklyBarGraphAndRateStatisticsVOList.get(0).getCount())
                    .diffCount(weeklyBarGraphAndRateStatisticsVOList.get(0).getCount() - weeklyBarGraphAndRateStatisticsVOList.get(1).getCount())
                    .achievementStatistics(weeklyBarGraphAndRateStatisticsVOList.stream().map(vo -> AchievementStatisticsDto.of(vo.getStartDate(), vo.getEndDate(), vo.getRate())).collect(Collectors.toList()))
                    .nowAchievementRate(weeklyBarGraphAndRateStatisticsVOList.get(0).getRate() - weeklyBarGraphAndRateStatisticsVOList.get(1).getRate())
                    .nowCountCompletedA(weeklyBarGraphAndRateStatisticsVOList.get(0).getCount())
                    .build();
        } else{
            return StatisticsResponseDto.builder()
                    .categoryStatistics(Collections.emptyList())
                    .mostCategory("기본 카테고리")
                    .nowCategoryCount(0)
                    .beforeCategoryCount(0)
                    .todoStatistics(Collections.emptyList())
                    .nowTotalCount(0)
                    .nowCountCompleted(0)
                    .diffCount(0)
                    .achievementStatistics(Collections.emptyList())
                    .nowAchievementRate(0.0f)
                    .nowCountCompletedA(0.0f)
                    .build();
        }
    }

    public static StatisticsResponseDto ofMonth(List<CategoryStatisticsVO> categoryStatisticsVOList, int totalCount,  PreviousCategoryStatisticsVO previousCategoryStatisticsVO,
                                                List<MonthlyBarGraphAndRateStatisticsVO> monthlyBarGraphAndRateStatisticsVOList){
        if (!categoryStatisticsVOList.isEmpty()){
            return StatisticsResponseDto.builder()
                    .categoryStatistics(categoryStatisticsVOList.stream().map(vo -> CategoryStatisticsDto.of(vo.getCategoryName(), vo.getColor(), vo.getRate())).collect(Collectors.toList()))
                    .mostCategory(categoryStatisticsVOList.get(0).getCategoryName())
                    .nowCategoryCount(categoryStatisticsVOList.get(0).getCount())
                    .beforeCategoryCount(previousCategoryStatisticsVO.getCount())
                    .todoStatistics(monthlyBarGraphAndRateStatisticsVOList.stream().map(vo -> TodoStatisticsDto.of(vo.getMonthDate(), vo.getCount())).collect(Collectors.toList()))
                    .nowTotalCount(totalCount)
                    .nowCountCompleted(monthlyBarGraphAndRateStatisticsVOList.get(0).getCount())
                    .diffCount(monthlyBarGraphAndRateStatisticsVOList.get(0).getCount() - monthlyBarGraphAndRateStatisticsVOList.get(1).getCount())
                    .achievementStatistics(monthlyBarGraphAndRateStatisticsVOList.stream().map(vo-> AchievementStatisticsDto.of(vo.getMonthDate(), vo.getRate())).collect(Collectors.toList()))
                    .nowAchievementRate(monthlyBarGraphAndRateStatisticsVOList.get(0).getRate() - monthlyBarGraphAndRateStatisticsVOList.get(1).getRate())
                    .build();
        } else{
            return StatisticsResponseDto.builder()
                    .categoryStatistics(Collections.emptyList())
                    .mostCategory("기본 카테고리")
                    .nowCategoryCount(0)
                    .beforeCategoryCount(0)
                    .todoStatistics(Collections.emptyList())
                    .nowTotalCount(0)
                    .nowCountCompleted(0)
                    .diffCount(0)
                    .achievementStatistics(Collections.emptyList())
                    .nowAchievementRate(0.0f)
                    .build();
        }
    }
}
