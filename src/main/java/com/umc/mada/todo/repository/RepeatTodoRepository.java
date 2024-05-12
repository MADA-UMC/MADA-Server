package com.umc.mada.todo.repository;

import com.umc.mada.todo.domain.RepeatTodo;
import com.umc.mada.todo.domain.Todo;
import com.umc.mada.todo.dto.RepeatTodoResponseDto;
import com.umc.mada.user.domain.User;
import net.bytebuddy.asm.Advice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RepeatTodoRepository extends JpaRepository<RepeatTodo, Integer> {
    List<RepeatTodo> readRepeatTodosByTodoId(Todo todoId);
    List<RepeatTodoResponseDto> findRepeatTodosByTodoIdAndIsDeletedIsFalse(Todo todoId);
    List<RepeatTodo> findRepeatTodosByDateIsAndIsDeletedIsFalse(LocalDate date);
    List<RepeatTodo> findAllByTodoIdAndDateGreaterThanEqual(Todo todoId, LocalDate date);
    List<RepeatTodo> findAllByTodoId(Todo todoId);

    @Query("SELECT DISTINCT FUNCTION('DAY', rt.date) FROM RepeatTodo rt WHERE rt.todoId.userId.id = :userId AND FUNCTION('DATE_FORMAT', rt.date, '%Y-%m') = :yearMonth")
    List<Integer> findDistinctDaysByUserIdAndYearMonth(@Param("userId") Long userId, @Param("yearMonth") String yearMonth);
}
