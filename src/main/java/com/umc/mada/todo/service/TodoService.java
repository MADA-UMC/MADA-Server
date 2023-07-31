package com.umc.mada.todo.service;

import com.umc.mada.category.repository.CategoryRepository;
import com.umc.mada.category.domain.Category;
import com.umc.mada.todo.domain.Todo;
import com.umc.mada.todo.dto.TodoRequestDto;
import com.umc.mada.todo.dto.TodoResponseDto;
import com.umc.mada.todo.repository.TodoRepository;
import com.umc.mada.global.BaseResponseStatus;
import com.umc.mada.user.domain.User;
import com.umc.mada.user.repository.UserRepository;
import com.umc.mada.todo.domain.Todo.Repeat;
import com.umc.mada.todo.domain.Todo.RepeatWeek;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TodoService {
    private final TodoRepository todoRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public TodoService(TodoRepository todoRepository, CategoryRepository categoryRepository) {
        this.todoRepository = todoRepository;
        this.categoryRepository = categoryRepository;
    }
    // 투두 생성 로직
    public TodoResponseDto createTodo(TodoRequestDto todoRequestDto) {
        // 유효성 검사 메서드를 호출하여 해당 ID가 데이터베이스에 존재하는지 확인
        //validateUserId(todoRequestDto.getUserId());
        validateCategoryId(todoRequestDto.getCategoryId());
        validateTodoName(todoRequestDto.getTodoName());

        // Enum 타입으로 변환
        Repeat repeat = Repeat.valueOf(todoRequestDto.getRepeat().name()); // Enum 타입 변환 수정
        RepeatWeek repeatWeek = todoRequestDto.getRepeatWeek() != null
                ? RepeatWeek.valueOf(todoRequestDto.getRepeatWeek().name()) : null; // Enum 타입 변환 수정

        // 투두 엔티티 생성
        Todo todo = Todo.builder()
                .userId(todoRequestDto.getUserId())
                .categoryId(todoRequestDto.getCategoryId())
                .todoName(todoRequestDto.getTodoName())
                .complete(todoRequestDto.isComplete())
                .repeat(repeat)
                .repeatWeek(repeatWeek)
                .startRepeatDate(todoRequestDto.getStartRepeatDate())
                .endRepeatDate(todoRequestDto.getEndRepeatDate())
                //.monthlyRepeatDates(todoRequestDto.getMonthlyRepeatDates())
                .build();

        // Todo를 저장하고 저장된 투두 앤티티 반환
        Todo savedTodo = todoRepository.save(todo);
        return convertToTodoResponseDto(savedTodo);
    }

    // 투두 수정 로직
    public TodoResponseDto updateTodo(Integer id, TodoRequestDto todoRequestDto) {
        // 주어진 투두 ID를 이용하여 투두 엔티티 조회
        Optional<Todo> optionalTodo = todoRepository.findById(id);
        if (optionalTodo.isPresent()) {
            Todo todo = optionalTodo.get();

            // 유효성 검사 메서드를 호출하여 해당 ID가 데이터베이스에 존재하는지 확인
            //validateUserId(todoRequestDto.getUserId());
            validateCategoryId(todoRequestDto.getCategoryId());
            validateTodoName(todoRequestDto.getTodoName());

            // Enum 타입으로 변환
            Repeat repeat = Enum.valueOf(Repeat.class, todoRequestDto.getRepeat().name());
            RepeatWeek repeatWeek = todoRequestDto.getRepeatWeek() != null
                    ? Enum.valueOf(RepeatWeek.class, todoRequestDto.getRepeatWeek().name()) : null;


            // 요청 데이터로 투두 엔티티 수정
            todo.setUserId(todoRequestDto.getUserId());
            todo.setCategoryId(todoRequestDto.getCategoryId());
            todo.setTodoName(todoRequestDto.getTodoName());
            todo.setComplete(todoRequestDto.isComplete());
            todo.setRepeat(repeat);
            todo.setRepeatWeek(repeatWeek);
            todo.setStartRepeatDate(todoRequestDto.getStartRepeatDate());
            todo.setEndRepeatDate(todoRequestDto.getEndRepeatDate());
            //todo.setMonthlyRepeatDates(todoRequestDto.getMonthlyRepeatDates());

            // 수정된 Todo를 저장하고 저장된 투두 엔티티 반환
            Todo updatedTodo = todoRepository.save(todo);

            return convertToTodoResponseDto(updatedTodo);
        }
        // 해당 ID의 Todo가 존재하지 않을 경우에 대한 처리 (예외 처리 등)
        throw new IllegalArgumentException(BaseResponseStatus.NOT_FOUND.getMessage());
    }

    // 투두 삭제 로직
    public void deleteTodo(Integer id) {
        // 주어진 투두 ID를 이용하여 투두 엔티티 조회
        Optional<Todo> optionalTodo = todoRepository.findById(id);
        if (optionalTodo.isPresent()) {
            // 주어진 투두 ID에 해당하는 Todo가 존재하는 경우 삭제
            todoRepository.deleteById(id);
        } else {
            // 해당 ID의 Todo가 존재하지 않을 경우에 대한 처리 (예외 처리 등)
            throw new IllegalArgumentException(BaseResponseStatus.NOT_FOUND.getMessage());
        }
    }

    // 모든 투두 목록 조회 로직
    public List<TodoResponseDto> getAllTodos() {
        // 모든 투두 엔티티 조회
        List<Todo> todos = todoRepository.findAll();
        // 각 투두 엔티티를 TodoResponseDto로 매핑하여 리스트로 반환
        return todos.stream().map(this::convertToTodoResponseDto).collect(Collectors.toList());
    }

    // 특정 투두 조회 로직
    public TodoResponseDto getTodoById(Integer id) {
        // 주어진 투두 ID를 이용하여 투두 엔티티 조회
        Optional<Todo> optionalTodo = todoRepository.findById(id);
        if (optionalTodo.isPresent()) {
            Todo todo = optionalTodo.get();
            return convertToTodoResponseDto(todo);
        }
        // 해당 ID의 Todo가 존재하지 않을 경우에 대한 처리 (예외 처리 등)
        throw new IllegalArgumentException(BaseResponseStatus.NOT_FOUND.getMessage());
    }

    private TodoResponseDto convertToTodoResponseDto(Todo todo) {
        return TodoResponseDto.builder()
                .id(todo.getId())
                .userId(todo.getUserId())
                .categoryId(todo.getCategoryId())
                .todoName(todo.getTodoName())
                .complete(todo.isComplete())
                .repeat(todo.getRepeat())
                .repeatWeek(todo.getRepeatWeek())
                //.monthlyRepeatDates(todo.getMonthlyRepeatDates())
                .build();
    }

    // 투두 이름 유효성 검사 메서드
    private void validateTodoName(String todoName) {
        if (todoName == null || todoName.isEmpty() || todoName.length() > 100) {
            throw new IllegalArgumentException(BaseResponseStatus.REQUEST_ERROR.getMessage());
        }
    }

//    // 유저 ID 유효성 검사 메서드
//    private void validateUserId(long userId) {
//        Optional<User> optionalUser = UserRepository.findById(userId);
//        if (optionalUser.isEmpty()) {
//            throw new IllegalArgumentException("존재하지 않는 유저 ID입니다.");
//        }
//    }

    // 카테고리 ID 유효성 검사 메서드
    private void validateCategoryId(int categoryId) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        if (optionalCategory.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 카테고리 ID입니다.");
        }
    }
}