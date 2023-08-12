package com.umc.mada.todo.repository;

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
public interface TodoRepository extends JpaRepository<Todo, Integer>{
    Optional<Todo> findTodoById(int todoId);
    List<Todo> findTodosByUserIdAndDateIs(User userId, LocalDate date);
    Optional<Todo> deleteTodoByUserIdAndId(User userId, int id);
    Optional<Todo> findTodoByUserIdAndId(User userId, int id);
}
