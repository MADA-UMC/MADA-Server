package com.umc.mada.todo.repository;

import com.umc.mada.todo.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Integer>{
    Optional<Todo> findByUserId(long userId);
}
