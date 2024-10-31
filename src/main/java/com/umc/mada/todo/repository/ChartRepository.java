package com.umc.mada.todo.repository;

import com.umc.mada.todo.domain.Todo;
import com.umc.mada.todo.repository.statistics.*;
import com.umc.mada.todo.repository.statistics.CategoryStatisticsVO;
import com.umc.mada.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ChartRepository extends JpaRepository<Todo, Integer> {
    @Query(value = "select A.category_id as categoryId, A.category_name as categoryName, A.color, COUNT(A.category_name) as count, ROUND(COUNT(A.category_name) / (SELECT COUNT(*) FROM TODO WHERE user_id = :uid and (date between :startDate and :endDate) and is_deleted = FALSE and complete = 1)*100, 0)as rate\n" +
            "from (select T.user_id, T.date, T.complete, C.category_name, C.id as category_id, C.color as color\n" +
            "    from TODO T join CATEGORY C on C.id = T.category_id\n" +
            "    where T.user_id = :uid and (T.date between :startDate and :endDate) and T.is_deleted = FALSE) A\n" +
            "where A.complete = 1\n" +
            "GROUP BY A.category_id\n" +
            "ORDER BY rate DESC;", nativeQuery = true)
    List<CategoryStatisticsVO> statisticsOnCategories(@Param("uid") Long uid, @Param("startDate") LocalDate startDate , @Param("endDate") LocalDate endDate);

    @Query(value = "select COUNT(category_id) as count\n" +
            "from (select T.user_id, T.date, T.complete,C.id as category_id\n" +
            "      from TODO T join CATEGORY C on C.id = T.category_id\n" +
            "      where T.user_id = :uid and (T.date between :startDate and :endDate) and T.is_deleted = FALSE) A\n" +
            "where user_id = :uid and category_id = :categoryId and date between :startDate and :endDate and complete = 1\n" +
            "GROUP BY category_id;", nativeQuery = true)
    PreviousCategoryStatisticsVO statisticsOnPreviousCategories(@Param("uid") Long uid, @Param("startDate") LocalDate startDate , @Param("endDate") LocalDate endDate, @Param("categoryId") int categoryId);

    @Query(value = "select date as todoDate, COUNT(*) as count\n" +
            "from TODO\n" +
            "where user_id = :uid and date between :startDate and :endDate and is_deleted = FALSE and complete = 1\n" +
            "GROUP BY date\n" +
            "ORDER BY date desc;", nativeQuery = true)
//    @Query("select t.date, count(t) from Todo t where t.userId = :user and t.date between :startDate and :endDate and t.complete=true GROUP BY t.date order by t.date")
    List<TodoBarGraphStatisticsVO> dayTodoBarGraphStatistics(@Param("uid") Long uid, @Param("startDate") LocalDate startDate , @Param("endDate") LocalDate endDate);
    Integer countAllByUserIdAndDate(User user, LocalDate date);
    Integer countAllByUserIdAndDateBetween(User user, LocalDate start, LocalDate end);

    @Query(value = "select  A.date, COUNT(*) as count, ROUND(COUNT(*)/(select COUNT(*) as count from TODO T where T.user_id = :uid and T.date = A.date GROUP BY T.date)*100, 1) as rate\n" +
            "from TODO A\n" +
            "where user_id = :uid and (A.date between :startDate and :endDate) and is_deleted = FALSE and A.complete=1\n" +
            "group by A.date\n" +
            "order by A.date desc;", nativeQuery = true)
    List<AchievementRateStatisticsVO> dayStatisticsOnAchievementRate(@Param("uid") Long uid, @Param("startDate") LocalDate startDate , @Param("endDate") LocalDate endDate);

    @Query(value = "select DATE_FORMAT(DATE_SUB(T.date, INTERVAL (DAYOFWEEK(T.date)-1) DAY), '%Y-%m-%d') as startDate,\n" +
            "       DATE_FORMAT(DATE_SUB(T.date, INTERVAL (DAYOFWEEK(T.date)-7) DAY), '%Y-%m-%d') as endDate,\n" +
            "       DATE_FORMAT(T.date, '%Y%U') AS weekDate,\n" +
            "       COUNT(T.id) as count, ROUND(COUNT(*)/(select COUNT(*) as count from TODO A where A.user_id = :uid and DATE_FORMAT(A.date, '%Y%U') = weekDate GROUP BY DATE_FORMAT(A.date, '%Y%U'))*100, 1) as rate\n" +
            "from TODO T\n" +
            "where T.user_id = :uid and T.date between :startDate and :endDate and T.is_deleted = FALSE and T.complete=1\n" +
            "GROUP BY weekDate\n" +
            "ORDER BY weekDate desc;", nativeQuery = true)
    List<WeeklyBarGraphAndRateStatisticsVO> weeklyTodoBarGraphAndRateStatistics(@Param("uid") Long uid, @Param("startDate") LocalDate startDate , @Param("endDate") LocalDate endDate);

    @Query(value = "select  DATE_FORMAT(A.date, '%Y-%m') AS monthDate, COUNT(*) as count, \n" +
            "        ROUND(COUNT(*)/(select COUNT(*) as count from TODO T where T.user_id = :uid and DATE_FORMAT(T.date, '%Y-%m') = monthDate GROUP BY DATE_FORMAT(T.date, '%Y-%m') and T.is_deleted = FALSE)*100, 1) as rate\n" +
            "from TODO A\n" +
            "where user_id = :uid and (A.date between :startDate and :endDate) and is_deleted = FALSE and A.complete=1\n" +
            "group by monthDate\n" +
            "ORDER BY monthDate desc;", nativeQuery = true)
    List<MonthlyBarGraphAndRateStatisticsVO> monthlyTodoBarGraphAndRateStatistics(@Param("uid") Long uid, @Param("startDate") LocalDate startDate , @Param("endDate") LocalDate endDate);
}
