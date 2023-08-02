package com.umc.mada.todo.controller;

import com.umc.mada.todo.dto.TodoRequestDto;
import com.umc.mada.todo.dto.TodoResponseDto;
import com.umc.mada.todo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
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
    public ResponseEntity<String> createTodo(@RequestBody TodoRequestDto todoRequestDto) {
        // 투두 생성 API
        todoService.createTodo(todoRequestDto);
        return new ResponseEntity<>("투두 생성 완료", HttpStatus.CREATED);
    }

    @PatchMapping("/{todoId}")
    public ResponseEntity<TodoResponseDto> updateTodo(@PathVariable int todoId, @RequestBody TodoRequestDto todoRequestDto){
        // 투두 수정 API
        TodoResponseDto updatedTodo = todoService.updateTodo(todoId, todoRequestDto);
        if (updatedTodo != null){
            return new ResponseEntity<>(updatedTodo, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{todoId}")
    public ResponseEntity<String> deleteTodo(@PathVariable int todoId) {
        // 투두 삭제 API
        todoService.deleteTodo(todoId);
        return new ResponseEntity<>("Todo 삭제 완료", HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<TodoResponseDto>> getAllTodos() {
        // 모든 투두 목록 조회 API
        List<TodoResponseDto> todos = todoService.getAllTodos();
        return new ResponseEntity<>(todos, HttpStatus.OK);
    }
    @GetMapping("/{userId}/{date}")
    public ResponseEntity<List<TodoResponseDto>> getUserTodo(@PathVariable Long userId, @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date){
        // 특정 유저 투두 목록 조회 API
        try{
            List<TodoResponseDto> userTodos = todoService.getUserTodo(userId, date);
            return new ResponseEntity<>(userTodos, HttpStatus.OK);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/{todoId}")
    public ResponseEntity<TodoResponseDto> getTodoById(@PathVariable int todoId) {
        // 특정 투두 조회 API
        try{
            TodoResponseDto todoDto = todoService.getTodoById(todoId);
            return new ResponseEntity<>(todoDto, HttpStatus.OK);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
