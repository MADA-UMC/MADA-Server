package com.umc.mada.todo.controller;

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
import java.util.List;
import java.util.Optional;


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
    public ResponseEntity<String> createTodo(Authentication authentication, @RequestBody TodoRequestDto todoRequestDto) {
        // 투두 생성 API
        Optional<User> userOptional = userRepository.findByAuthId(authentication.getName());
        User user = userOptional.get();
        todoService.createTodo(user, todoRequestDto);
        return new ResponseEntity<>("투두 생성 완료", HttpStatus.CREATED);
    }

    @PatchMapping("/todoId/{todoId}")
    public ResponseEntity<TodoResponseDto> updateTodo(Authentication authentication, @PathVariable int todoId, @RequestBody TodoRequestDto todoRequestDto){
        // 투두 수정 API
        try{
            Optional<User> userOptional = userRepository.findByAuthId(authentication.getName());
            User user = userOptional.get();
            TodoResponseDto updatedTodo = todoService.updateTodo(user, todoId, todoRequestDto);
            return new ResponseEntity<>(updatedTodo, HttpStatus.OK);
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/todoId/{todoId}")
    public ResponseEntity<String> deleteTodo(Authentication authentication, @PathVariable int todoId) {
        // 투두 삭제 API
        try{
            Optional<User> userOptional = userRepository.findByAuthId(authentication.getName());
            User user = userOptional.get();
            todoService.deleteTodo(user, todoId);
            return new ResponseEntity<>("투두 삭제 완료", HttpStatus.OK);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/date/{date}")
        // 특정 유저 투두 조회 API
    public ResponseEntity<List<TodoResponseDto>> getUserTodo(Authentication authentication, @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date){
        try{
            Optional<User> userOptional = userRepository.findByAuthId(authentication.getName());
            User user = userOptional.get();
            List<TodoResponseDto> userTodos = todoService.getUserTodo(user, date);
            return new ResponseEntity<>(userTodos, HttpStatus.OK);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
//    @GetMapping("/todoId/{todoId}")
//    public ResponseEntity<TodoResponseDto> getTodoById(@PathVariable int todoId) {
//        // 특정 투두 조회 API
//        try{
//            TodoResponseDto todoDto = todoService.getTodoById(todoId);
//            return new ResponseEntity<>(todoDto, HttpStatus.OK);
//        } catch (IllegalArgumentException e){
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
//    @GetMapping
//    public ResponseEntity<List<TodoResponseDto>> getAllTodos() {
//        // 모든 투두 목록 조회 API
//        List<TodoResponseDto> todos = todoService.getAllTodos();
//        return new ResponseEntity<>(todos, HttpStatus.OK);
//    }
}
