package com.umc.mada.todo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.umc.mada.todo.repository.statistics.*;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
public class StatisticsResponseDto {
    private final List<CategoryStatisticsDto> categoryStatistics;
    private final String mostCategory; //가장 많이 완료한 카테고리
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private final float totalTodo; //추가한 todo 개수
    private final float nowCategoryCount; //오늘, 이번주, 이번달 가장 많이 완료한 카테고리 수
    private final float beforeCategoryCount;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private final float diffCountA;

    private final List<TodoStatisticsDto> todoStatistics;
    private final float nowTotalCount; //오늘, 이번주, 이번달 전체 투두 수
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private final float ratio; //비율
    private final float nowCountCompleted; // 완료한 투두 수
    private final float diffCountB; //어제, 지난주, 지난 달에 완료한 투두 수보다 얼마나 더 많이 했는지

    private final List<AchievementStatisticsDto> achievementStatistics;
    private final float nowAchievementRate; //오늘, 이번주, 이번달 투두 달성도의 상승, 하락 비율
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private final float nowCountCompletedA;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private final float diffCountC; //오늘, 이번주, 이번달에 어제, 저번주, 저번달보다 얼마나 더 많이 했는지

    public static StatisticsResponseDto ofDay(List<CategoryStatisticsVO> categoryStatisticsVOList, PreviousCategoryStatisticsVO previousCategoryStatisticsVO,
                                           List<TodoBarGraphStatisticsVO> todoBarGraphStatisticsVOList, int totalCount, List<AchievementRateStatisticsVO> achievementRateStatisticsVOList){
        //각 list의 index 0은 오늘, 1은 어제이다.
        return StatisticsResponseDto.builder()
                .categoryStatistics(categoryStatisticsVOList.stream().map(vo -> CategoryStatisticsDto.of(vo.getCategoryName(), vo.getColor(), vo.getRate())).collect(Collectors.toList()))
                .mostCategory(categoryStatisticsVOList.get(0).getCategoryName())
                .nowCategoryCount(categoryStatisticsVOList.get(0).getCount())
                .beforeCategoryCount(previousCategoryStatisticsVO.getCount())
                .todoStatistics(todoBarGraphStatisticsVOList.stream().map(vo -> TodoStatisticsDto.of(vo.getTodoDate(), vo.getCount())).collect(Collectors.toList()))
                .nowTotalCount(totalCount)
                .nowCountCompleted(todoBarGraphStatisticsVOList.get(0).getCount())
                .diffCountB(todoBarGraphStatisticsVOList.get(0).getCount() - todoBarGraphStatisticsVOList.get(1).getCount())
                .achievementStatistics(achievementRateStatisticsVOList.stream().map(vo-> AchievementStatisticsDto.of(vo.getDate(), vo.getRate())).collect(Collectors.toList()))
                .nowAchievementRate(achievementRateStatisticsVOList.get(0).getRate() - achievementRateStatisticsVOList.get(1).getRate())
                .nowCountCompletedA(achievementRateStatisticsVOList.get(0).getCount())
                .diffCountC(achievementRateStatisticsVOList.get(0).getCount() - achievementRateStatisticsVOList.get(1).getCount())
                .build();
    }

    public static StatisticsResponseDto ofWeek(List<CategoryStatisticsVO> categoryStatisticsVOList, int totalCount,  PreviousCategoryStatisticsVO previousCategoryStatisticsVO,
                                               List<WeeklyBarGraphAndRateStatisticsVO> weeklyBarGraphAndRateStatisticsVOList){
        return StatisticsResponseDto.builder()
                .categoryStatistics(categoryStatisticsVOList.stream().map(vo -> CategoryStatisticsDto.of(vo.getCategoryName(), vo.getColor(), vo.getRate())).collect(Collectors.toList()))
                .totalTodo(totalCount)
                .mostCategory(categoryStatisticsVOList.get(0).getCategoryName())
                .nowCategoryCount(categoryStatisticsVOList.get(0).getCount())
                .beforeCategoryCount(previousCategoryStatisticsVO.getCount())
                .todoStatistics(weeklyBarGraphAndRateStatisticsVOList.stream().map(vo -> TodoStatisticsDto.of(vo.getStartDate(), vo.getEndDate(), vo.getCount())).collect(Collectors.toList()))
                .nowTotalCount(totalCount)
                .ratio(weeklyBarGraphAndRateStatisticsVOList.get(0).getRate())
                .nowCountCompleted(weeklyBarGraphAndRateStatisticsVOList.get(0).getCount())
                .diffCountB(weeklyBarGraphAndRateStatisticsVOList.get(0).getCount() - weeklyBarGraphAndRateStatisticsVOList.get(1).getCount())
                .achievementStatistics(weeklyBarGraphAndRateStatisticsVOList.stream().map(vo-> AchievementStatisticsDto.of(vo.getStartDate(), vo.getEndDate(), vo.getRate())).collect(Collectors.toList()))
                .nowAchievementRate(weeklyBarGraphAndRateStatisticsVOList.get(0).getRate() - weeklyBarGraphAndRateStatisticsVOList.get(1).getRate())
                .nowCountCompletedA(weeklyBarGraphAndRateStatisticsVOList.get(0).getCount())
                .diffCountC(weeklyBarGraphAndRateStatisticsVOList.get(0).getCount() - weeklyBarGraphAndRateStatisticsVOList.get(1).getCount())
                .build();
    }

    public static StatisticsResponseDto ofMonth(List<CategoryStatisticsVO> categoryStatisticsVOList, int totalCount,  PreviousCategoryStatisticsVO previousCategoryStatisticsVO,
                                                List<MonthlyBarGraphAndRateStatisticsVO> monthlyBarGraphAndRateStatisticsVOList){
        return StatisticsResponseDto.builder()
                .categoryStatistics(categoryStatisticsVOList.stream().map(vo -> CategoryStatisticsDto.of(vo.getCategoryName(), vo.getColor(), vo.getRate())).collect(Collectors.toList()))
                .mostCategory(categoryStatisticsVOList.get(0).getCategoryName())
                .nowCategoryCount(categoryStatisticsVOList.get(0).getCount())
                .beforeCategoryCount(previousCategoryStatisticsVO.getCount())
                .diffCountA(categoryStatisticsVOList.get(0).getCount() - previousCategoryStatisticsVO.getCount())
                .todoStatistics(monthlyBarGraphAndRateStatisticsVOList.stream().map(vo -> TodoStatisticsDto.of(vo.getMonthDate(), vo.getCount())).collect(Collectors.toList()))
                .nowTotalCount(totalCount)
                .ratio(monthlyBarGraphAndRateStatisticsVOList.get(0).getRate())
                .nowCountCompleted(monthlyBarGraphAndRateStatisticsVOList.get(0).getCount())
                .diffCountB(monthlyBarGraphAndRateStatisticsVOList.get(0).getCount() - monthlyBarGraphAndRateStatisticsVOList.get(1).getCount())
                .achievementStatistics(monthlyBarGraphAndRateStatisticsVOList.stream().map(vo-> AchievementStatisticsDto.of(vo.getMonthDate(), vo.getRate())).collect(Collectors.toList()))
                .nowAchievementRate(monthlyBarGraphAndRateStatisticsVOList.get(0).getRate() - monthlyBarGraphAndRateStatisticsVOList.get(1).getRate())
                .build();
    }
}
