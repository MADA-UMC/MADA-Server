package com.umc.mada.todo.controller;

import com.umc.mada.todo.domain.Todo;
import com.umc.mada.todo.dto.RepeatTodoResponseDto;
import com.umc.mada.todo.dto.TodoRequestDto;
import com.umc.mada.todo.dto.TodoResponseDto;
import com.umc.mada.todo.repository.TodoRepository;
import com.umc.mada.todo.service.TodoService;
import com.umc.mada.user.domain.User;
import com.umc.mada.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.*;


@RestController
@RequestMapping("/api/home/todo")
public class TodoController {
    private final TodoService todoService;
    private final UserRepository userRepository;

    @Autowired
    public TodoController(TodoService todoService, UserRepository userRepository) {
        this.todoService = todoService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createTodo(Authentication authentication, @RequestBody TodoRequestDto todoRequestDto) {
        // 투두 생성 API
        Optional<User> userOptional = userRepository.findByAuthId(authentication.getName());
        User user = userOptional.get();
        Map<String, Object> map = todoService.createTodo(user, todoRequestDto);
        //TodoResponseDto newTodo = todoService.createTodo(user, todoRequestDto);
        //Map<String, Object> data = new LinkedHashMap<>();
        //data.put("Todo", newTodo);
        //Map<String, Object> result = new LinkedHashMap<>();
        //result.put("status", 200);
        //result.put("success", true);
        //result.put("message", "투두 생성이 완료되었습니다.");
        //result.put("data", data);
        return ResponseEntity.ok().body(map);
    }

    @PatchMapping("/update/{todoId}")
    public ResponseEntity<Map<String, Object>> updateTodo(Authentication authentication, @PathVariable int todoId, @RequestBody TodoRequestDto todoRequestDto){
        // 투두 수정 API
        Optional<User> userOptional = userRepository.findByAuthId(authentication.getName());
        User user = userOptional.get();
        Map<String, Object> map = todoService.updateTodo(user, todoId, todoRequestDto);
        //TodoResponseDto updatedTodo = todoService.updateTodo(user, todoId, todoRequestDto);
        //Map<String, Object> data = new LinkedHashMap<>();
        //data.put("Todo", updatedTodo);
        //Map<String, Object> result = new LinkedHashMap<>();
        //result.put("status", 200);
        //result.put("success", true);
        //result.put("message", "투두 수정이 완료되었습니다.");
        //result.put("data", data);
        return ResponseEntity.ok().body(map);
    }

    @PatchMapping("/delete/{todoId}")
    public ResponseEntity<Map<String, Object>> deleteTodo(Authentication authentication, @PathVariable int todoId) {
        // 투두 삭제 API
        Optional<User> userOptional = userRepository.findByAuthId(authentication.getName());
        User user = userOptional.get();
        todoService.deleteTodo(user, todoId);
        Map<String, Object> result = new LinkedHashMap<>();
        //result.put("status", 200);
        //result.put("success", true);
        result.put("message", "투두 삭제가 완료되었습니다.");
        return ResponseEntity.ok().body(result);
    }

    @PatchMapping("/repeat/delete/{repeatTodoId}")
    public ResponseEntity<Map<String, Object>> deleteRepeatTodo(Authentication authentication, @PathVariable int repeatTodoId) {
        // 반복 투두 삭제 API (이 반복 투두)
        Optional<User> userOptional = userRepository.findByAuthId(authentication.getName());
        User user = userOptional.get();
        todoService.deleteRepeatTodo(user, repeatTodoId);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("message", "반복 투두 삭제가 완료되었습니다.");
        return ResponseEntity.ok().body(result);
    }

    @PatchMapping("/repeat/delete-all-future/{repeatTodoId}")
    public ResponseEntity<Map<String, Object>> deleteRepeatTodoAndFuture(Authentication authentication, @PathVariable int repeatTodoId) {
        // 반복 투두 삭제 API (이 반복 투두 및 향후 반복 투두)
        Optional<User> userOptional = userRepository.findByAuthId(authentication.getName());
        User user = userOptional.get();
        todoService.deleteRepeatTodoAndFuture(user, repeatTodoId);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("message", "해당 반복 투두 및 향후 반복 투두 삭제가 완료되었습니다.");
        return ResponseEntity.ok().body(result);
    }

    @PatchMapping("/repeat/delete-all/{repeatTodoId}")
    public ResponseEntity<Map<String, Object>> deleteAllRepeatTodos(Authentication authentication, @PathVariable int repeatTodoId) {
        // 반복 투두 삭제 API (모든 반복 투두)
        Optional<User> userOptional = userRepository.findByAuthId(authentication.getName());
        User user = userOptional.get();
        todoService.deleteAllRepeatTodos(user, repeatTodoId);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("message", "모든 반복 투두 삭제가 완료되었습니다.");
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/date/{date}")
        // 특정 유저 투두 조회 API
    public ResponseEntity<Map<String, Object>> getUserTodo(Authentication authentication, @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date){
        Optional<User> userOptional = userRepository.findByAuthId(authentication.getName());
        User user = userOptional.get();
        Map<String, Object> map = todoService.getUserTodo(user, date);
        //List<TodoResponseDto> userTodos = todoService.getUserTodo(user, date);
        //Map<String, Object> data = new LinkedHashMap<>();
        //data.put("nickname", user.getNickname());
        //data.put("TodoList", userTodos);
        //result.put("status", 200);
        //result.put("success", true);
        //result.put("message", "투두가 정상적으로 조회되었습니다.");
        //result.put("data", data);
        return ResponseEntity.ok().body(map);
    }

    @GetMapping("/repeat/all")
        // 반복 투두 조회 API
    public ResponseEntity<Map<String, Object>> getUserRepeatTodo(Authentication authentication){
        Optional<User> userOptional = userRepository.findByAuthId(authentication.getName());
        User user = userOptional.get();
        List<TodoResponseDto> repeatTodos = todoService.getUserRepeatTodo(user);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("RepeatTodoList", repeatTodos);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("data", data);
        return ResponseEntity.ok().body(result);
    }
}
