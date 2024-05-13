package com.umc.mada.todo.repository;

import com.umc.mada.todo.domain.Repeat;
import com.umc.mada.todo.domain.Todo;
import com.umc.mada.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Integer>{//, TodoRepositoryCustom

    List<Todo> findByUserIdAndRepeatIn(User userId, List<Repeat> repeats);
    List<Todo> findTodosByUserIdAndDateIs(User userId, LocalDate date);
    List<Todo> findTodosByUserIdAndCategoryId(User userId, int categoryId);
    Optional<Todo> findTodoByUserIdAndId(User userId, int id);
    List<Todo> findTodosByUserIdAndIsDeletedIsFalse(User user);
    List<Todo> findTodosByUserIdAndCategoryIdAndIsDeletedIsFalse(User userId, int categoryId);

    @Query("SELECT DISTINCT FUNCTION('DAY', t.date) FROM Todo t WHERE t.userId.id = :userId AND FUNCTION('DATE_FORMAT', t.date, '%Y-%m') = :yearMonth")
    List<Integer> findDistinctDaysByUserIdAndYearMonth(@Param("userId") Long userId, @Param("yearMonth") String yearMonth);

    @Query(value = "select ROUND(IFNULL(AVG(A.complete) * 100, 0), 1) as completeTodoPercent, ROUND(COUNT(A.complete)/COUNT(*),1) as todosPercent\n" +
            "from (select T.user_id, T.date, T.complete\n" +
            "      from TODO T\n" +
            "      where T.user_id = :uid and (T.date between :startDate and :endDate) and `repeat` = 'N') A right join (\n" +
            "    SELECT DATE_FORMAT(ADDDATE(:startDate, INTERVAL @num\\:=@num+1 DAY), '%Y-%m-%d') `date`\n" +
            "    FROM CUSTOM_ITEM, (SELECT @num\\:=-1) num\n" +
            "    LIMIT :length\n" +
            ") B on A.date = B.date\n" +
            "group by MONTH(B.date);", nativeQuery = true)
//    @Query(value = "select ROUND(IFNULL(AVG(IFNULL(A.complete, 0)) * 100, 0), 1) as completeTodoPercent, ROUND(COUNT(IFNULL(A.complete, 0)) / COUNT(*), 1) as todosPercent\n" +
//        "from (select user_id, T.date, T.complete\n" +
//        "      from TODO T\n" +
//        "      where `repeat` = 'N') A right join\n" +
//        "    (SELECT DATE_FORMAT(ADDDATE('2023-08-01', INTERVAL @num\\:=@num+1 DAY), '%Y-%m-%d') `date`\n" +
//        "    FROM TODO, (SELECT @num\\:=-1) num\n" +
//        "    LIMIT 31) B on A.date = B.date\n" + //TODO: limit 진짜 한달 날짜로 해야함
//        "group by month(B.date);", nativeQuery = true)
//    @Query(value = "select ROUND(IFNULL(AVG(IFNULL(A.complete, 0)) * 100, 0), 1) as completeTodoPercent, ROUND(COUNT(IFNULL(A.complete, 0)) / COUNT(*), 1) as todosPercent\n" +
//            "from (select user_id, T.date, T.complete\n" +
//            "      from TODO T\n" +
//            "      where user_id = 55 and (T.date between '2023-08-01' and '2023-08-31') and `repeat` = 'N') A right join (\n" +
//            "    SELECT DATE_FORMAT(ADDDATE('2023-08-01', INTERVAL @num:=@num+1 DAY), '%Y-%m-%d') `date`\n" +
//            "    FROM TODO, (SELECT @num:=-1) num\n" +
//            "    LIMIT 31\n" +
//            ") B on A.date = B.date\n" +
//            "group by month(B.date);", nativeQuery = true)
    TodoStatisticsVO findTodosMonthAVG(@Param("uid")Long uid, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, int length);

//    @Query(value = "select IFNULL(ROUND(AVG(A.complete)*100,1),0) as completeTodoPercent, ROUND(COUNT(A.complete)/COUNT(*),1) as todosPercent\n" +
//            "from (select user_id, T.date, T.complete\n" +
//            "      from TODO T\n" +
//            "      where (user_id = 55) and (T.date between '2023-08-01' and '2023-08-31') and `repeat` = 'N') A right join\n" +
//            "    (SELECT DATE_FORMAT(ADDDATE('2023-08-01', INTERVAL @num\\:=@num+1 DAY), '%Y-%m-%d') `date`\n" +
//            "    FROM TODO, (SELECT @num\\:=-1) num\n" +
//            "    LIMIT 31) B on A.date = B.date\n" + //TODO: limit 진짜 한달 날짜로 해야함
//            "group by week(B.date);", nativeQuery = true)
//    @Query(value = "select IFNULL(ROUND(AVG(A.complete)*100,1),0) as completeTodoPercent, ROUND(COUNT(A.complete)/COUNT(*),1) as todosPercent\n" +
//            "from (select user_id, T.date, T.complete\n" +
//            "      from TODO T\n" +
//            "      where user_id = 55 and (T.date between '2023-09-01' and '2023-09-31') and `repeat` = 'N') A right join (\n" +
//            "    SELECT DATE_FORMAT(ADDDATE('2023-09-01', INTERVAL @num\\:=@num+1 DAY), '%Y-%m-%d') `date`\n" +
//            "    FROM TODO, (SELECT @num\\:=-1) num\n" +
//            "    LIMIT 30\n" +
//            ") B on A.date = B.date\n" +
//            "group by week(B.date);", nativeQuery = true)
    @Query(value = "select ROUND(IFNULL(AVG(A.complete) * 100, 0), 1) as completeTodoPercent, ROUND(COUNT(A.complete)/COUNT(*),1) as todosPercent\n" +
            "from (select T.user_id, T.date, T.complete\n" +
            "      from TODO T\n" +
            "      where T.user_id = :uid and (T.date between :startDate and :endDate) and `repeat` = 'N') A right join (\n" +
            "    SELECT DATE_FORMAT(ADDDATE(:startDate, INTERVAL @num\\:=@num+1 DAY), '%Y-%m-%d') `date`\n" +
            "    FROM CUSTOM_ITEM, (SELECT @num\\:=-1) num\n" +
            "    LIMIT :length\n" +
            ") B on A.date = B.date\n" +
            "group by MONTH(B.date);", nativeQuery = true)
    List<TodoStatisticsVO> findTodosWeekAVG(@Param("uid") Long uid, @Param("startDate") LocalDate startDate , @Param("endDate") LocalDate endDate, int length);

    @Query(value = "select A.category_name as categoryName, A.color, " +
            "ROUND(COUNT(A.category_name) / (SELECT COUNT(*) FROM TODO WHERE user_id = :uid and complete = 1 and (date between :startDate and :endDate) and `repeat` = 'N')*100, 0)as rate\n" +
            "from (select T.user_id, T.date, T.complete, C.category_name, C.id as category_id, C.color as color\n" +
            "      from TODO T join CATEGORY C\n" +
            "        on C.id = T.category_id\n" +
            "      where T.user_id = :uid and (T.date between :startDate and :endDate) and `repeat` = 'N') A\n" +
            "    right join\n" +
            "    (SELECT DATE_FORMAT(ADDDATE(:startDate, INTERVAL @num\\:=@num+1 DAY), '%Y-%m-%d') `date`\n" +
            "     FROM CUSTOM_ITEM, (SELECT @num\\:=-1) num\n" +
            "     LIMIT :length) B\n" +
            "on A.date = B.date\n" +
            "where A.complete = 1\n" +
            "GROUP BY A.category_id\n" +
            "ORDER BY rate DESC\n" +
            "LIMIT 5", nativeQuery = true)
    List<CategoryStatisticsVO> findCategoryAVG(@Param("uid") Long uid, @Param("startDate") LocalDate startDate ,@Param("endDate") LocalDate endDate, int length);
}