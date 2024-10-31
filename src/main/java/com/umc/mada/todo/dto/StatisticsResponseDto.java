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
        float diffCount = calculateDiffCount(todoBarGraphStatisticsVOList);
        float nowAchievementRate = calculateNowAchievementRate(achievementRateStatisticsVOList);

        return StatisticsResponseDto.builder()
                .categoryStatistics(categoryStatisticsVOList.stream().map(vo -> CategoryStatisticsDto.of(vo.getCategoryName(), vo.getColor(), vo.getRate())).collect(Collectors.toList()))
                .mostCategory(categoryStatisticsVOList.get(0).getCategoryName())
                .nowCategoryCount(categoryStatisticsVOList.get(0).getCount())
                .beforeCategoryCount(previousCategoryStatisticsVO.getCount())
                .todoStatistics(todoBarGraphStatisticsVOList.stream().map(vo -> TodoStatisticsDto.of(vo.getTodoDate(), vo.getCount())).collect(Collectors.toList()))
                .nowTotalCount(totalCount)
                .nowCountCompleted(todoBarGraphStatisticsVOList.get(0).getCount())
                .diffCount(diffCount)
                .achievementStatistics(achievementRateStatisticsVOList.stream().map(vo -> AchievementStatisticsDto.of(vo.getDate(), vo.getRate())).collect(Collectors.toList()))
                .nowAchievementRate(nowAchievementRate)
                .nowCountCompletedA(achievementRateStatisticsVOList.get(0).getCount())
                .build();
    }

    public static StatisticsResponseDto ofWeek(List<CategoryStatisticsVO> categoryStatisticsVOList, int totalCount,  PreviousCategoryStatisticsVO previousCategoryStatisticsVO,
                                               List<WeeklyBarGraphAndRateStatisticsVO> weeklyBarGraphAndRateStatisticsVOList){
        float diffCount = calculateDiffCount(weeklyBarGraphAndRateStatisticsVOList);
        float nowAchievementRate = calculateNowAchievementRate(weeklyBarGraphAndRateStatisticsVOList);

        return StatisticsResponseDto.builder()
                .categoryStatistics(categoryStatisticsVOList.stream().map(vo -> CategoryStatisticsDto.of(vo.getCategoryName(), vo.getColor(), vo.getRate())).collect(Collectors.toList()))
                .mostCategory(categoryStatisticsVOList.get(0).getCategoryName())
                .nowCategoryCount(categoryStatisticsVOList.get(0).getCount())
                .beforeCategoryCount(previousCategoryStatisticsVO.getCount())
                .todoStatistics(weeklyBarGraphAndRateStatisticsVOList.stream().map(vo -> TodoStatisticsDto.of(vo.getStartDate(), vo.getEndDate(), vo.getCount())).collect(Collectors.toList()))
                .nowTotalCount(totalCount)
                .nowCountCompleted(weeklyBarGraphAndRateStatisticsVOList.get(0).getCount())
                .diffCount(diffCount)
                .achievementStatistics(weeklyBarGraphAndRateStatisticsVOList.stream().map(vo -> AchievementStatisticsDto.of(vo.getStartDate(), vo.getEndDate(), vo.getRate())).collect(Collectors.toList()))
                .nowAchievementRate(nowAchievementRate)
                .nowCountCompletedA(weeklyBarGraphAndRateStatisticsVOList.get(0).getCount())
                .build();
    }

    public static StatisticsResponseDto ofMonth(List<CategoryStatisticsVO> categoryStatisticsVOList, int totalCount,  PreviousCategoryStatisticsVO previousCategoryStatisticsVO,
                                                List<MonthlyBarGraphAndRateStatisticsVO> monthlyBarGraphAndRateStatisticsVOList){
        float diffCount = calculateDiffCount(monthlyBarGraphAndRateStatisticsVOList);
        float nowAchievementRate = calculateNowAchievementRate(monthlyBarGraphAndRateStatisticsVOList);

        return StatisticsResponseDto.builder()
                .categoryStatistics(categoryStatisticsVOList.stream().map(vo -> CategoryStatisticsDto.of(vo.getCategoryName(), vo.getColor(), vo.getRate())).collect(Collectors.toList()))
                .mostCategory(categoryStatisticsVOList.get(0).getCategoryName())
                .nowCategoryCount(categoryStatisticsVOList.get(0).getCount())
                .beforeCategoryCount(previousCategoryStatisticsVO.getCount())
                .todoStatistics(monthlyBarGraphAndRateStatisticsVOList.stream().map(vo -> TodoStatisticsDto.of(vo.getMonthDate(), vo.getCount())).collect(Collectors.toList()))
                .nowTotalCount(totalCount)
                .nowCountCompleted(monthlyBarGraphAndRateStatisticsVOList.get(0).getCount())
                .diffCount(diffCount)
                .achievementStatistics(monthlyBarGraphAndRateStatisticsVOList.stream().map(vo-> AchievementStatisticsDto.of(vo.getMonthDate(), vo.getRate())).collect(Collectors.toList()))
                .nowAchievementRate(nowAchievementRate)
                .build();
    }

    private static <T> float calculateNowAchievementRate(List<T> statisticsVOList) {
        if (statisticsVOList.size() > 1) {
            if (statisticsVOList.get(0) instanceof AchievementRateStatisticsVO) {
                return ((AchievementRateStatisticsVO) statisticsVOList.get(0)).getRate() - ((AchievementRateStatisticsVO) statisticsVOList.get(1)).getRate();
            } else if (statisticsVOList.get(0) instanceof WeeklyBarGraphAndRateStatisticsVO) {
                return ((WeeklyBarGraphAndRateStatisticsVO) statisticsVOList.get(0)).getRate() - ((WeeklyBarGraphAndRateStatisticsVO) statisticsVOList.get(1)).getRate();
            } else if (statisticsVOList.get(0) instanceof MonthlyBarGraphAndRateStatisticsVO) {
                return ((MonthlyBarGraphAndRateStatisticsVO) statisticsVOList.get(0)).getRate() - ((MonthlyBarGraphAndRateStatisticsVO) statisticsVOList.get(1)).getRate();
            }
        }
        if (statisticsVOList.get(0) instanceof AchievementRateStatisticsVO) {
            return ((AchievementRateStatisticsVO) statisticsVOList.get(0)).getRate();
        } else if (statisticsVOList.get(0) instanceof WeeklyBarGraphAndRateStatisticsVO) {
            return ((WeeklyBarGraphAndRateStatisticsVO) statisticsVOList.get(0)).getRate();
        } else if (statisticsVOList.get(0) instanceof MonthlyBarGraphAndRateStatisticsVO) {
            return ((MonthlyBarGraphAndRateStatisticsVO) statisticsVOList.get(0)).getRate();
        }
        return 0;
    }

    private static <T> float calculateDiffCount(List<T> statisticsVOList) {
        if (statisticsVOList.size() > 1) {
            if (statisticsVOList.get(0) instanceof TodoBarGraphStatisticsVO) {
                return ((TodoBarGraphStatisticsVO) statisticsVOList.get(0)).getCount() - ((TodoBarGraphStatisticsVO) statisticsVOList.get(1)).getCount();
            } else if (statisticsVOList.get(0) instanceof WeeklyBarGraphAndRateStatisticsVO) {
                return ((WeeklyBarGraphAndRateStatisticsVO) statisticsVOList.get(0)).getCount() - ((WeeklyBarGraphAndRateStatisticsVO) statisticsVOList.get(1)).getCount();
            } else if (statisticsVOList.get(0) instanceof MonthlyBarGraphAndRateStatisticsVO) {
                return ((MonthlyBarGraphAndRateStatisticsVO) statisticsVOList.get(0)).getCount() - ((MonthlyBarGraphAndRateStatisticsVO) statisticsVOList.get(1)).getCount();
            }
        }
        if (statisticsVOList.get(0) instanceof TodoBarGraphStatisticsVO) {
            return ((TodoBarGraphStatisticsVO) statisticsVOList.get(0)).getCount();
        } else if (statisticsVOList.get(0) instanceof WeeklyBarGraphAndRateStatisticsVO) {
            return ((WeeklyBarGraphAndRateStatisticsVO) statisticsVOList.get(0)).getCount();
        } else if (statisticsVOList.get(0) instanceof MonthlyBarGraphAndRateStatisticsVO) {
            return ((MonthlyBarGraphAndRateStatisticsVO) statisticsVOList.get(0)).getCount();
        }
        return 0;
    }
}
