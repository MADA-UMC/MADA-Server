package com.umc.mada.todo.controller;

import com.umc.mada.global.BaseResponse;
import com.umc.mada.todo.dto.TodoRequestDto;
import com.umc.mada.todo.dto.TodoResponseDto;
import com.umc.mada.todo.service.TodoService;
import com.umc.mada.user.domain.User;
import com.umc.mada.user.dto.UserRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
    public BaseResponse<TodoResponseDto> createTodo(@RequestBody TodoRequestDto todoRequestDto) {
        // Todo 생성 API
        TodoResponseDto createdTodo = todoService.createTodo(todoRequestDto);
        return new BaseResponse<>(createdTodo);
    }

    @DeleteMapping("/{todo_id}")
    public BaseResponse<String> deleteTodo(@PathVariable("todo_id") Integer id) {
        // Todo 삭제 API
        todoService.deleteTodo(id);
        return new BaseResponse<>("Todo 삭제 완료");
    }

    @PutMapping("/{todo_id}")
    public BaseResponse<TodoResponseDto> updateTodo(@PathVariable("todo_id") Integer id, @RequestBody TodoRequestDto todoRequestDto) {
        // Todo 수정 API
        TodoResponseDto updatedTodo = todoService.updateTodo(id, todoRequestDto);
        return new BaseResponse<>(updatedTodo);
    }

    @GetMapping("/{todo_id}")
    public BaseResponse<TodoResponseDto> getTodoById(@PathVariable("todo_id") Integer id) {
        // 특정 Todo 조회 API
        TodoResponseDto todoDto = todoService.getTodoById(id);
        return new BaseResponse<>(todoDto);
    }

    @GetMapping
    public BaseResponse<List<TodoResponseDto>> getAllTodos() {
        // 모든 Todo 목록 조회 API
        List<TodoResponseDto> todos = todoService.getAllTodos();
        return new BaseResponse<>(todos);
    }
}
