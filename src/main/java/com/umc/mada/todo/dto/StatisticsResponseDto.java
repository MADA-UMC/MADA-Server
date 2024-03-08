package com.umc.mada.todo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.umc.mada.todo.repository.statistics.AchievementRateStatisticsVO;
import com.umc.mada.todo.repository.statistics.CategoryStatisticsVO;
import com.umc.mada.todo.repository.statistics.PreviousCategoryStatisticsVO;
import com.umc.mada.todo.repository.statistics.TodoBarGraphStatisticsVO;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
public class StatisticsResponseDto {
    private final List<CategoryStatisticsDto> categoryStatistics;
    private final String mostCategory; //가장 많이 완료한 카테고리
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final float ratio1; //비율
    private final float nowCategoryCount; //오늘, 이번주, 이번달 가장 많이 완료한 카테고리 수
    private final float beforeCategoryCount;

    private final List<TodoStatisticsDto> todoStatistics;
    private final float nowTotalCount; //오늘, 이번주, 이번달 전체 투두 수
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final float ratio2; //비율
    private final float nowCountCompleted; // 완료한 투두 수
    private final float diffCount; //어제, 지난주, 지난 달에 완료한 투두 수보다 얼마나 더 많이 했는지

    private final List<AchievementStatisticsDto> achievementStatistics;
    private final float nowAchievementRate; //오늘, 이번주, 이번달 투두 달성도의 상승, 하락 비율
    private final float nowCountCompleted_A;
    private final float diffCount_A; //오늘, 이번주, 이번달에 어제, 저번주, 저번달보다 얼마나 더 많이 했는지

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
                .diffCount(todoBarGraphStatisticsVOList.get(0).getCount() - todoBarGraphStatisticsVOList.get(1).getCount())
                .achievementStatistics(achievementRateStatisticsVOList.stream().map(vo-> AchievementStatisticsDto.of(vo.getDate(), vo.getRate())).collect(Collectors.toList()))
                .nowAchievementRate(achievementRateStatisticsVOList.get(0).getRate())
                .nowCountCompleted_A(achievementRateStatisticsVOList.get(0).getCount())
                .diffCount_A(achievementRateStatisticsVOList.get(0).getCount() - achievementRateStatisticsVOList.get(1).getCount())
                .build();
    }
}
