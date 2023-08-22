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
    List<Todo> findTodosByUserIdAndEndRepeatDateAfter(User userId, LocalDate date);
    List<Todo> deleteTodosByUserIdAndCategoryId(User userId, int categoryId);
    Optional<Todo> deleteTodoByUserIdAndId(User userId, int id);
    Optional<Todo> findTodoByUserIdAndId(User userId, int id);
    List<Todo> findTodosByUserIdAndDateBetweenAndRepeat(User user,LocalDate startDate, LocalDate endDate, Repeat repeat);
    List<Todo> findTodosByUserIdAndRepeat(User user, Repeat repeat);
}