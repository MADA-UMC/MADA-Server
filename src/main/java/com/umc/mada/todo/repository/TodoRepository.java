package com.umc.mada.todo.repository;

import com.umc.mada.category.domain.Category;
import com.umc.mada.todo.domain.Todo;
import com.umc.mada.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Integer>{
    Optional<Todo> findTodoById(int todoId);
    // 특정 사용자의 해당 날짜의 할 일 목록 조회
    List<Todo> findTodosByUserIdAndDateIs(Long userId, LocalDate date);
}
