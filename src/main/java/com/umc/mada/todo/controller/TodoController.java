package com.umc.mada.todo.controller;

import com.umc.mada.todo.dto.TodoRequestDto;
import com.umc.mada.todo.dto.TodoResponseDto;
import com.umc.mada.todo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/home/todo")
public class TodoController {
    private final TodoService todoService;

    @Autowired
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping
    public ResponseEntity<TodoResponseDto> createTodo(@RequestBody TodoRequestDto todoRequestDto) {
        // 투두 생성 API
        TodoResponseDto createdTodo = todoService.createTodo(todoRequestDto);
        return new ResponseEntity<>(createdTodo, HttpStatus.CREATED);
    }

    @DeleteMapping("/{todo_id}")
    public ResponseEntity<String> deleteTodo(@PathVariable("todo_id") Integer id) {
        // 투두 삭제 API
        todoService.deleteTodo(id);
        return new ResponseEntity<>("Todo 삭제 완료", HttpStatus.OK);
    }


    @PutMapping("/{todo_id}")
    public ResponseEntity<TodoResponseDto> updateTodo(@PathVariable("todo_id") Integer id, @RequestBody TodoRequestDto todoRequestDto) {
        // 투두 수정 API
        TodoResponseDto updatedTodo = todoService.updateTodo(id, todoRequestDto);
        return new ResponseEntity<>(updatedTodo, HttpStatus.OK);
    }

    @GetMapping("/{todo_id}")
    public ResponseEntity<TodoResponseDto> getTodoById(@PathVariable("todo_id") Integer id) {
        // 특정 투두 조회 API
        TodoResponseDto todoDto = todoService.getTodoById(id);
        return new ResponseEntity<>(todoDto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<TodoResponseDto>> getAllTodos() {
        // 모든 투두 목록 조회 API
        List<TodoResponseDto> todos = todoService.getAllTodos();
        return new ResponseEntity<>(todos, HttpStatus.OK);
    }
}
