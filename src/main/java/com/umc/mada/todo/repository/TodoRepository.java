package com.umc.mada.todo.repository;

import com.umc.mada.todo.domain.Repeat;
import com.umc.mada.todo.domain.Todo;
import com.umc.mada.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Integer> {

    List<Todo> findByUserIdAndRepeatIn(User userId, List<Repeat> repeats);
    List<Todo> findTodosByUserIdAndDateIs(User userId, LocalDate date);
    List<Todo> findTodosByUserIdAndEndRepeatDateAfter(User userId, LocalDate date);
    List<Todo> deleteTodosByUserIdAndCategoryId(User userId, int categoryId);
    Optional<Todo> deleteTodoByUserIdAndId(User userId, int id);
    Optional<Todo> findTodoByUserIdAndId(User userId, int id);
    List<Todo> findTodosByUserIdAndDateBetweenAndRepeat(User user,LocalDate startDate, LocalDate endDate, Repeat repeat);
    List<Todo> findTodosByUserIdAndRepeat(User user, Repeat repeat);
    List<Todo> findTodosByUserId(User user);

//    @Query(value = "select IFNULL(ROUND(AVG(A.complete)*100,1),0) as avg, ROUND(COUNT(A.complete)/COUNT(*),1) as countAvg\n" +
//            "from (select user_id, T.date, T.complete\n" +
//            "      from TODO T\n" +
//            "      where (user_id = \\:uid) and (T.date between \\:startDate and \\:endDate) and `repeat` = 'N') A right join\n" +
//            "    (SELECT DATE_FORMAT(ADDDATE(\\:startDate, INTERVAL @num\\:=@num+1 DAY), '%Y-%m-%d') `date`\n" +
//            "    FROM TODO, (SELECT @num\\:=-1) num\n" +
//            "    LIMIT 31) B on A.date = B.date\n" + //TODO: limit 진짜 한달 날짜로 해야함
//            "group by month(B.date);", nativeQuery = true)
    @Modifying
    @Transactional
    @Query(value = "select IFNULL(ROUND(AVG(A.complete)*100,1),0) as completeTodoPercent, ROUND(COUNT(A.complete)/COUNT(*),1) as todosPercent\n" +
        "from (select user_id, T.date, T.complete\n" +
        "      from TODO T\n" +
        "      where (user_id = 55) and (T.date between '2023-08-01' and '2023-08-31') and `repeat` = 'N') A right join\n" +
        "    (SELECT DATE_FORMAT(ADDDATE('2023-08-01', INTERVAL @num\\:=@num+1 DAY), '%Y-%m-%d') `date`\n" +
        "    FROM TODO, (SELECT @num\\:=-1) num\n" +
        "    LIMIT 31) B on A.date = B.date\n" + //TODO: limit 진짜 한달 날짜로 해야함
        "group by month(B.date);", nativeQuery = true)
//    @Query(value = "select IFNULL(ROUND(AVG(A.complete)*100,1),0) as completeTodoPercent, ROUND(COUNT(A.complete)/COUNT(*),1) as todosPercent\n" +
//            "from (select user_id, T.date, T.complete\n" +
//            "      from TODO T\n" +
//            "      where (user_id = 55) and (T.date between '2023-08-01' and '2023-08-31') and `repeat` = 'N') A right join\n" +
//            "    (SELECT DATE_FORMAT(ADDDATE('2023-08-01', INTERVAL @num\\:=@num+1 DAY), '%Y-%m-%d') `date`\n" +
//            "    FROM TODO, (SELECT @num\\:=-1) num\n" +
//            "    LIMIT 31) B on A.date = B.date\n" + //TODO: limit 진짜 한달 날짜로 해야함
//            "group by month(B.date);", nativeQuery = true)
    List<StatisticsVO> findTodosMonthAVG(@Param("uid") Long uid,@Param("endDate") LocalDate endDate, @Param("startDate") LocalDate startDate);

    @Query(value = "select IFNULL(ROUND(AVG(A.complete)*100,1),0) as completeTodoPercent, ROUND(COUNT(A.complete)/COUNT(*),1) as todosPercent\n" +
            "from (select user_id, T.date, T.complete\n" +
            "      from TODO T\n" +
            "      where (user_id = 55) and (T.date between '2023-08-01' and '2023-08-31') and `repeat` = 'N') A right join\n" +
            "    (SELECT DATE_FORMAT(ADDDATE('2023-08-01', INTERVAL @num\\:=@num+1 DAY), '%Y-%m-%d') `date`\n" +
            "    FROM TODO, (SELECT @num\\:=-1) num\n" +
            "    LIMIT 31) B on A.date = B.date\n" + //TODO: limit 진짜 한달 날짜로 해야함
            "group by week(B.date);", nativeQuery = true)
    List<StatisticsVO> findTodosWeekAVG(@Param("uid") Long uid,@Param("endDate") LocalDate endDate, @Param("startDate") LocalDate startDate);

}