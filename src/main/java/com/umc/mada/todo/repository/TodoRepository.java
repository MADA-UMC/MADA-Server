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
public interface TodoRepository extends JpaRepository<Todo, Integer> {
    List<Todo> findByUserIdAndRepeatIn(User userId, List<Repeat> repeats);
    List<Todo> findTodosByUserIdAndDateIs(User userId, LocalDate date);
    List<Todo> deleteTodosByUserIdAndCategoryId(User userId, int categoryId);
    Optional<Todo> deleteTodoByUserIdAndId(User userId, int id);
    Optional<Todo> findTodoByUserIdAndId(User userId, int id);
//   @Query("select  AVG(complete) as avg from Todo where repeat = 'N' and endRepeatDate > :endDate and startRepeatDate < :startDate and userId = :uid group by userId")
   @Query("select AVG(T.complete) as avg\n" +
           "from Todo T\n" +
           "where T.repeat = 'N' and T.date between :startDate and :endDate and T.userId = :uid\n" +
           "group by T.userId")
   Optional<Double> findTodosAVG(@Param("uid") User uid,@Param("endDate") LocalDate endDate, @Param("startDate") LocalDate startDate);
}