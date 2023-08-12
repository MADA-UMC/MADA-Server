package com.umc.mada.todo.controller;

import com.umc.mada.global.BaseResponse;
import com.umc.mada.todo.dto.TodoRequestDto;
import com.umc.mada.todo.dto.TodoResponseDto;
import com.umc.mada.todo.service.TodoService;
import com.umc.mada.user.domain.CusomtUserDetails;
import com.umc.mada.user.domain.User;
import com.umc.mada.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
        TodoResponseDto newTodo = todoService.createTodo(user, todoRequestDto);
        Map<String, Object> result = new LinkedHashMap<>();
        //result.put("status", 200);
        //result.put("success", true);
        //result.put("message", "투두 생성이 완료되었습니다.");
        result.put("data", newTodo);
        return ResponseEntity.ok().body(result);
    }

    @PatchMapping("/todoId/{todoId}")
    public ResponseEntity<Map<String, Object>> updateTodo(Authentication authentication, @PathVariable int todoId, @RequestBody TodoRequestDto todoRequestDto){
        // 투두 수정 API
        Optional<User> userOptional = userRepository.findByAuthId(authentication.getName());
        User user = userOptional.get();
        TodoResponseDto updatedTodo = todoService.updateTodo(user, todoId, todoRequestDto);
        Map<String, Object> result = new LinkedHashMap<>();
        //result.put("status", 200);
        //result.put("success", true);
        //result.put("message", "투두 수정이 완료되었습니다.");
        result.put("data", updatedTodo);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/todoId/{todoId}")
    public ResponseEntity<Map<String, Object>> deleteTodo(Authentication authentication, @PathVariable int todoId) {
        // 투두 삭제 API
        try{
            Optional<User> userOptional = userRepository.findByAuthId(authentication.getName());
            User user = userOptional.get();
            todoService.deleteTodo(user, todoId);
            Map<String, Object> result = new LinkedHashMap<>();
            //result.put("status", 200);
            //result.put("success", true);
            //result.put("message", "투두 삭제가 완료되었습니다.");
            return ResponseEntity.ok().body(result);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/date/{date}")
        // 특정 유저 투두 조회 API
    public ResponseEntity<Map<String, Object>> getUserTodo(Authentication authentication, @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date){
        try{
            Optional<User> userOptional = userRepository.findByAuthId(authentication.getName());
            User user = userOptional.get();
            List<TodoResponseDto> userTodos = todoService.getUserTodo(user, date);
            Map<String, Object> result = new LinkedHashMap<>();
            //result.put("status", 200);
            //result.put("success", true);
            //result.put("message", "투두가 정상적으로 조회되었습니다.");
            result.put("data", userTodos);
            return ResponseEntity.ok().body(result);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
