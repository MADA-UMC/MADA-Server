package com.umc.mada.todo.service;

import com.umc.mada.category.dto.CategoryResponseDto;
import com.umc.mada.file.domain.File;
import com.umc.mada.todo.dto.TodoRequestDto;
import com.umc.mada.todo.dto.TodoResponseDto;
import com.umc.mada.todo.domain.Todo;
import com.umc.mada.todo.repository.TodoRepository;
import com.umc.mada.global.BaseResponseStatus;
import com.umc.mada.category.domain.Category;
import com.umc.mada.category.repository.CategoryRepository;
import com.umc.mada.user.domain.User;
import com.umc.mada.user.repository.UserRepository;
import com.umc.mada.todo.domain.Todo.Repeat;
import com.umc.mada.todo.domain.Todo.RepeatWeek;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TodoService {
    private final TodoRepository todoRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;


    @Autowired
    public TodoService(UserRepository userRepository, TodoRepository todoRepository, CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.todoRepository = todoRepository;
        this.categoryRepository = categoryRepository;
    }

    // 투두 생성 로직
    public TodoResponseDto createTodo(TodoRequestDto todoRequestDto) {
        // 유효성 검사 메서드를 호출하여 해당 ID가 데이터베이스에 존재하는지 확인
        validateUserId(todoRequestDto.getUserId().getId());
        validateCategoryId(todoRequestDto.getCategoryId().getId());
        validateTodoName(todoRequestDto.getTodoName());

//        // Enum 타입으로 변환
//        Repeat repeat = Repeat.valueOf(todoRequestDto.getRepeat().name()); // Enum 타입 변환 수정
//        RepeatWeek repeatWeek = todoRequestDto.getRepeatWeek() != null
//                ? RepeatWeek.valueOf(todoRequestDto.getRepeatWeek().name()) : null; // Enum 타입 변환 수정

        User user = userRepository.findUserById(todoRequestDto.getUserId().getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자 ID입니다."));
        Category category = categoryRepository.findCategoryById(todoRequestDto.getCategoryId().getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리 ID입니다."));

        // 투두 앤티티 생성
        Todo todo = new Todo(user, todoRequestDto.getDate(), category,
                todoRequestDto.getTodoName(), todoRequestDto.isComplete(),
                todoRequestDto.getRepeat(), todoRequestDto.getRepeatWeek(), todoRequestDto.getStartRepeatDate(), todoRequestDto.getEndRepeatDate());

        // 투두를 저장하고 저장된 투두 앤티티 반환
        Todo savedTodo = todoRepository.save(todo);
        // 저장된 카테고리 정보를 기반으로 TodoResponseDto 생성하여 반환
        return new TodoResponseDto(savedTodo.getUserId(), savedTodo.getDate(), savedTodo.getCategoryId(),
                savedTodo.getTodoName(), savedTodo.isComplete(),
                savedTodo.getRepeat(), savedTodo.getRepeatWeek(), savedTodo.getStartRepeatDate(), savedTodo.getEndRepeatDate());
    }

    @Transactional
    // 투두 수정 로직
    public TodoResponseDto updateTodo(int todoId, TodoRequestDto todoRequestDto) {
        // 주어진 투두 Id가 유효한지 검사
        if (todoId <= 0){
            throw new IllegalArgumentException("유효하지 않은 투두 ID입니다.");
        }

        // 주어진 todoId를 이용하여 카테고리 엔티티 조회
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new IllegalArgumentException("Todo not found with id: " + todoId));

//        // Enum 타입으로 변환
//        Repeat repeat = Repeat.valueOf(todoRequestDto.getRepeat().name()); // Enum 타입 변환 수정
//        RepeatWeek repeatWeek = todoRequestDto.getRepeatWeek() != null
//                ? RepeatWeek.valueOf(todoRequestDto.getRepeatWeek().name()) : null; // Enum 타입 변환 수정


        // 카테고리 ID 변경 처리
        if (todoRequestDto.getCategoryId() != null) {
            Category category = categoryRepository.findById(todoRequestDto.getCategoryId().getId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리 ID입니다."));
            todo.setCategoryId(category);
        }

        // 투두 이름 변경 처리
        if (todoRequestDto.getTodoName() != null) {
            todo.setTodoName(todoRequestDto.getTodoName());
        }

        // 투두 완료 여부 변경 처리
        todo.setComplete(todoRequestDto.isComplete());

        // 투두 반복 변경 처리
        if (todoRequestDto.getRepeat() != null){
            todo.setRepeat(todoRequestDto.getRepeat());
        }
        todo.setRepeatWeek(todoRequestDto.getRepeatWeek());

        // 투두 반복 시작일 / 종료일 변경 처리
        todo.setStartRepeatDate(todoRequestDto.getStartRepeatDate());
        todo.setEndRepeatDate(todoRequestDto.getEndRepeatDate());

        // 수정된 Todo를 저장하고 저장된 투두 엔티티 반환
        Todo updatedTodo = todoRepository.save(todo);

        // 저장된 투두 정보를 기반으로 TodoResponseDto 생성하여 반환
        return new TodoResponseDto(updatedTodo.getUserId(), updatedTodo.getDate(), updatedTodo.getCategoryId(), updatedTodo.getTodoName(),
                updatedTodo.isComplete(), updatedTodo.getRepeat(), updatedTodo.getRepeatWeek(),
                updatedTodo.getStartRepeatDate(), updatedTodo.getEndRepeatDate());
    }

    // 투두 삭제 로직
    public void deleteTodo(int todoId) {
        // 주어진 투두 ID를 이용하여 투두 엔티티 조회
        Optional<Todo> optionalTodo = todoRepository.findTodoById(todoId);
        if (optionalTodo.isPresent()) {
            // 주어진 투두 ID에 해당하는 Todo가 존재하는 경우 삭제
            todoRepository.deleteById(todoId);
        } else {
            // 해당 ID의 Todo가 존재하지 않을 경우에 대한 처리 (예외 처리 등)
            throw new IllegalArgumentException(BaseResponseStatus.NOT_FOUND.getMessage());
        }
    }

    // 특정 유저 투두 조회 로직
//    public List<TodoResponseDto> getUserTodo(User userId, LocalDate date) {
//        List<Todo> userTodos = todoRepository.findTodosByUserIdAndDateIs(userId, date);
//
//        // 조회 결과가 존재하는 경우에는 해당 할 일을 TodoResponseDto로 매핑하여 반환
//        return userTodos.stream().map(todo -> new TodoResponseDto(todo.getUserId(), todo.getDate(), todo.getCategoryId(), todo.getTodoName(),
//                todo.isComplete(), todo.getRepeat(), todo.getRepeatWeek(),
//                todo.getStartRepeatDate(), todo.getEndRepeatDate())).collect(Collectors.toList());
//    }
    // UserId로 User 엔티티 조회
    public User getUserById(long userId) {
        Optional<User> optionalUser = userRepository.findUserById(userId);
        return optionalUser.orElse(null);
    }
    public List<TodoResponseDto> getUserTodo(Long userId, LocalDate date) {
        User user = getUserById(userId);
        if (user == null) {
            // 유저가 존재하지 않을 때
            throw new IllegalArgumentException("User not found");
        }

        List<Todo> userTodos = todoRepository.findTodosByUserIdAndDateIs(user.getId(), date);

        // 조회 결과가 존재하는 경우에는 해당 할 일을 TodoResponseDto로 매핑하여 반환
        return userTodos.stream().map(todo -> new TodoResponseDto(todo.getUserId(), todo.getDate(), todo.getCategoryId(), todo.getTodoName(),
                todo.isComplete(), todo.getRepeat(), todo.getRepeatWeek(),
                todo.getStartRepeatDate(), todo.getEndRepeatDate())).collect(Collectors.toList());
    }

    // 반복 투두 목록 조회

    // 모든 투두 목록 조회 로직
    public List<TodoResponseDto> getAllTodos() {
        // 모든 투두 엔티티 조회
        List<Todo> todos = todoRepository.findAll();
        // 각 투두 엔티티를 TodoResponseDto로 매핑하여 리스트로 반환
        return todos.stream()
                .map(todo -> new TodoResponseDto(todo.getUserId(), todo.getDate(), todo.getCategoryId(), todo.getTodoName(),
                        todo.isComplete(), todo.getRepeat(), todo.getRepeatWeek(),
                        todo.getStartRepeatDate(), todo.getEndRepeatDate())).collect(Collectors.toList());
    }

    // 특정 투두 조회 로직
    public TodoResponseDto getTodoById(int todoId) {
        // 주어진 투두 ID를 이용하여 투두 엔티티 조회
        Optional<Todo> optionalTodo = todoRepository.findTodoById(todoId);
        if (optionalTodo.isPresent()) {
            Todo todo = optionalTodo.get();
            return new TodoResponseDto(todo.getUserId(), todo.getDate(), todo.getCategoryId(), todo.getTodoName(),
                    todo.isComplete(), todo.getRepeat(), todo.getRepeatWeek(),
                    todo.getStartRepeatDate(), todo.getEndRepeatDate());
        }
        // 해당 ID의 Todo가 존재하지 않을 경우에 대한 처리 (예외 처리 등)
        throw new IllegalArgumentException(BaseResponseStatus.NOT_FOUND.getMessage());
    }

    // 투두 이름 유효성 검사 메서드
    private void validateTodoName(String todoName) {
        if (todoName == null || todoName.isEmpty()) {
            throw new IllegalArgumentException(BaseResponseStatus.REQUEST_ERROR.getMessage());
        }
    }

    // 유저 ID 유효성 검사 메서드
    private void validateUserId(long userId) {
        Optional<User> optionalUser = userRepository.findUserById(userId);
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 유저 ID입니다.");
        }
    }

    // 카테고리 ID 유효성 검사 메서드
    private void validateCategoryId(int categoryId) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        if (optionalCategory.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 카테고리 ID입니다.");
        }
    }
}