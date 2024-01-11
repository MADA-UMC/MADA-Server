package com.umc.mada.todo.repository;

import com.umc.mada.todo.domain.RepeatTodo;
import com.umc.mada.todo.domain.Todo;
import com.umc.mada.todo.dto.RepeatTodoResponseDto;
import com.umc.mada.user.domain.User;
import net.bytebuddy.asm.Advice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RepeatTodoRepository extends JpaRepository<RepeatTodo, Integer> {
    List<RepeatTodo> readRepeatTodosByTodoId(Todo todoId);
    List<RepeatTodo> findRepeatTodosByTodoIdAndIsDeletedIsFalse(Todo todoId);
    List<RepeatTodo> findRepeatTodosByDateIsAndIsDeletedIsFalse(LocalDate date);
}
