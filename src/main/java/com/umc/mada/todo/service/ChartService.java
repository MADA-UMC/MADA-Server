package com.umc.mada.todo.service;

import com.umc.mada.todo.dto.StatisticsResponseDto;
import com.umc.mada.todo.repository.ChartRepository;
import com.umc.mada.todo.repository.statistics.AchievementRateStatisticsVO;
import com.umc.mada.todo.repository.statistics.CategoryStatisticsVO;
import com.umc.mada.todo.repository.statistics.PreviousCategoryStatisticsVO;
import com.umc.mada.todo.repository.statistics.TodoBarGraphStatisticsVO;
import com.umc.mada.user.domain.User;
import com.umc.mada.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
        PreviousCategoryStatisticsVO previousCategoryStatisticsVO = chartRepository.statisticsOnPreviousCategories(user.getId(), date, date, categoryStatisticsVOList.get(0).getCategoryId());

        //todo 막대 그래프 통계
        List<TodoBarGraphStatisticsVO> todoBarGraphStatisticsVOList = chartRepository.dayTodoBarGraphStatistics(user.getId(), date, date);
        Integer totalCount = chartRepository.countAllByUserIdAndDate(user, date);

        //todo 달성률 통계
        List<AchievementRateStatisticsVO> achievementRateStatisticsVOList = chartRepository.dayStatisticsOnAchievementRate(user.getId(), date, date);

        return StatisticsResponseDto.ofDay(categoryStatisticsVOList, previousCategoryStatisticsVO, todoBarGraphStatisticsVOList, totalCount, achievementRateStatisticsVOList);
    }
}
