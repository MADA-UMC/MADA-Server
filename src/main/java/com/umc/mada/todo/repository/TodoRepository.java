package com.umc.mada.todo.repository;

import com.umc.mada.todo.domain.Repeat;
import com.umc.mada.todo.domain.RepeatMonth;
import com.umc.mada.todo.domain.Todo;
import com.umc.mada.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Integer> {
    List<Todo> findByUserIdAndRepeatIn(User userId, List<Repeat> repeats);

    List<Todo> findTodosByUserIdAndDateIs(User userId, LocalDate date);

    Optional<Todo> deleteTodoByUserIdAndId(User userId, int id);

    Optional<Todo> findTodoByUserIdAndId(User userId, int id);

    @Query("select  AVG(complete) from Todo where repeat = 'N' and endRepeatDate > :endDate and startRepeatDate < :startDate and userId = :uid")
    Double findTodosAVG(@Param("uid") User uid,@Param("endDate") LocalDate endDate, @Param("startDate") LocalDate startDate);
}