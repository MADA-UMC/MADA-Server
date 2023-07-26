package com.umc.mada.todo.service;

import com.umc.mada.todo.domain.Todo;
import com.umc.mada.todo.dto.TodoRequestDto;
import com.umc.mada.todo.dto.TodoResponseDto;
import com.umc.mada.todo.repository.TodoRepository;
import com.umc.mada.global.BaseException;
import com.umc.mada.global.BaseResponseStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//2023-07-26 user, category 불러와서 다시 수정해야함 (BaseException도)

@Service
public class TodoService {
    private final TodoRepository todoRepository;

    @Autowired
    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }
    // Todo 생성 로직
    public TodoResponseDto createTodo(TodoRequestDto todoRequestDto) {
        // Todo 이름이 없거나 길이를 초과한 경우 예외 처리
        validateTodoName(todoRequestDto.getTodo_name());

        // Todo 엔티티 생성
        Todo todo = Todo.builder()
                .user_id(todoRequestDto.getUser_id())
                .category_id(todoRequestDto.getCategory_id())
                .todo_name(todoRequestDto.getTodo_name())
                .complete(todoRequestDto.getComplete())
                .startRepeatDate(todoRequestDto.getStartRepeatDate())
                .endRepeatDate(todoRequestDto.getEndRepeatDate())
                .isRepeatMon(todoRequestDto.isRepeatMon())
                .isRepeatTue(todoRequestDto.isRepeatTue())
                .isRepeatWed(todoRequestDto.isRepeatWed())
                .isRepeatThu(todoRequestDto.isRepeatThu())
                .isRepeatFri(todoRequestDto.isRepeatFri())
                .isRepeatSat(todoRequestDto.isRepeatSat())
                .isRepeatSun(todoRequestDto.isRepeatSun())
                .monthlyRepeatDates(todoRequestDto.getMonthlyRepeatDates())
                .build();

        // Todo를 저장하고 저장된 Todo 앤티티 반환
        Todo savedTodo = todoRepository.save(todo);

        // 저장된 Todo 정보를 기반으로 TodoResponseDto 생성하여 반환
        return TodoResponseDto.builder()
                .id(savedTodo.getId())
                .user_id(savedTodo.getUser_id())
                .category_id(savedTodo.getCategory_id())
                .todo_name(savedTodo.getTodo_name())
                .complete(savedTodo.getComplete())
                .startRepeatDate(savedTodo.getStartRepeatDate())
                .endRepeatDate(savedTodo.getEndRepeatDate())
                .isRepeatMon(savedTodo.isRepeatMon())
                .isRepeatTue(savedTodo.isRepeatTue())
                .isRepeatWed(savedTodo.isRepeatWed())
                .isRepeatThu(savedTodo.isRepeatThu())
                .isRepeatFri(savedTodo.isRepeatFri())
                .isRepeatSat(savedTodo.isRepeatSat())
                .isRepeatSun(savedTodo.isRepeatSun())
                .monthlyRepeatDates(todoRequestDto.getMonthlyRepeatDates())
                .build();
    }

    // Todo 수정 로직
    public TodoResponseDto updateTodo(Integer id, TodoRequestDto todoRequestDto) {
        // 주어진 Todo ID를 이용하여 Todo 엔티티 조회
        Optional<Todo> optionalTodo = todoRepository.findById(id);
        if (optionalTodo.isPresent()) {
            Todo todo = optionalTodo.get();

            // Todo 이름이 없거나 길이를 초과한 경우 예외 처리
            validateTodoName(todoRequestDto.getTodo_name());

            // 요청 데이터로 Todo 엔티티 수정
            todo.setUser_id(todoRequestDto.getUser_id());
            todo.setCategory_id(todoRequestDto.getCategory_id());
            todo.setTodo_name(todoRequestDto.getTodo_name());
            todo.setComplete(todoRequestDto.getComplete());
            todo.setStartRepeatDate(todoRequestDto.getStartRepeatDate());
            todo.setEndRepeatDate(todoRequestDto.getEndRepeatDate());
            todo.setRepeatMon(todoRequestDto.isRepeatMon());
            todo.setRepeatTue(todoRequestDto.isRepeatTue());
            todo.setRepeatWed(todoRequestDto.isRepeatWed());
            todo.setRepeatThu(todoRequestDto.isRepeatThu());
            todo.setRepeatFri(todoRequestDto.isRepeatFri());
            todo.setRepeatSat(todoRequestDto.isRepeatSat());
            todo.setRepeatSun(todoRequestDto.isRepeatSun());
            todo.setMonthlyRepeatDates(todoRequestDto.getMonthlyRepeatDates());

            // 수정된 Todo를 저장하고 저장된 Todo 엔티티 반환
            Todo updatedTodo = todoRepository.save(todo);

            // 저장된 Todo 정보를 기반으로 TodoResponseDto 생성하여 반환
            return TodoResponseDto.builder()
                    .id(updatedTodo.getId())
                    .user_id(updatedTodo.getUser_id())
                    .category_id(updatedTodo.getCategory_id())
                    .todo_name(updatedTodo.getTodo_name())
                    .complete(updatedTodo.getComplete())
                    .startRepeatDate(updatedTodo.getStartRepeatDate())
                    .endRepeatDate(updatedTodo.getEndRepeatDate())
                    .isRepeatMon(updatedTodo.isRepeatMon())
                    .isRepeatTue(updatedTodo.isRepeatTue())
                    .isRepeatWed(updatedTodo.isRepeatWed())
                    .isRepeatThu(updatedTodo.isRepeatThu())
                    .isRepeatFri(updatedTodo.isRepeatFri())
                    .isRepeatSat(updatedTodo.isRepeatSat())
                    .isRepeatSun(updatedTodo.isRepeatSun())
                    .build();
        }
        // 해당 ID의 Todo가 존재하지 않을 경우에 대한 처리 (예외 처리 등)
        throw new IllegalArgumentException(BaseResponseStatus.NOT_FOUND.getMessage());
    }

    // Todo 삭제 로직
    public void deleteTodo(Integer id) {
        // 주어진 Todo ID를 이용하여 Todo 엔티티 조회
        Optional<Todo> optionalTodo = todoRepository.findById(id);
        if (optionalTodo.isPresent()) {
            // 주어진 Todo ID에 해당하는 Todo가 존재하는 경우 삭제
            todoRepository.deleteById(id);
        } else {
            // 해당 ID의 Todo가 존재하지 않을 경우에 대한 처리 (예외 처리 등)
            throw new IllegalArgumentException(BaseResponseStatus.NOT_FOUND.getMessage());
        }
    }

    // 모든 Todo 목록 조회 로직
    public List<TodoResponseDto> getAllTodos() {
        // 모든 Todo 엔티티 조회
        List<Todo> todos = todoRepository.findAll();
        // 각 Todo 엔티티를 TodoResponseDto로 매핑하여 리스트로 반환
        return todos.stream()
                .map(todo -> TodoResponseDto.builder()
                        .id(todo.getId())
                        .user_id(todo.getUser_id())
                        .category_id(todo.getCategory_id())
                        .todo_name(todo.getTodo_name())
                        .complete(todo.getComplete())
                        .startRepeatDate(todo.getStartRepeatDate())
                        .endRepeatDate(todo.getEndRepeatDate())
                        .isRepeatMon(todo.isRepeatMon())
                        .isRepeatTue(todo.isRepeatTue())
                        .isRepeatWed(todo.isRepeatWed())
                        .isRepeatThu(todo.isRepeatThu())
                        .isRepeatFri(todo.isRepeatFri())
                        .isRepeatSat(todo.isRepeatSat())
                        .isRepeatSun(todo.isRepeatSun())
                        .build())
                .collect(Collectors.toList());
    }

    // 특정 Todo 조회 로직
    public TodoResponseDto getTodoById(Integer id) {
        // 주어진 Todo ID를 이용하여 Todo 엔티티 조회
        Optional<Todo> optionalTodo = todoRepository.findById(id);
        if (optionalTodo.isPresent()) {
            Todo todo = optionalTodo.get();
            // Todo 엔티티 정보를 기반으로 TodoResponseDto 생성하여 반환
            return TodoResponseDto.builder()
                    .id(todo.getId())
                    .user_id(todo.getUser_id())
                    .category_id(todo.getCategory_id())
                    .todo_name(todo.getTodo_name())
                    .complete(todo.getComplete())
                    .startRepeatDate(todo.getStartRepeatDate())
                    .endRepeatDate(todo.getEndRepeatDate())
                    .isRepeatMon(todo.isRepeatMon())
                    .isRepeatTue(todo.isRepeatTue())
                    .isRepeatWed(todo.isRepeatWed())
                    .isRepeatThu(todo.isRepeatThu())
                    .isRepeatFri(todo.isRepeatFri())
                    .isRepeatSat(todo.isRepeatSat())
                    .isRepeatSun(todo.isRepeatSun())
                    .build();
        }
        // 해당 ID의 Todo가 존재하지 않을 경우에 대한 처리 (예외 처리 등)
        throw new IllegalArgumentException(BaseResponseStatus.NOT_FOUND.getMessage());
    }

    // Todo 이름 유효성 검사 메서드
    private void validateTodoName(String todoName) {
        if (todoName == null || todoName.isEmpty() || todoName.length() > 100) {
            throw new IllegalArgumentException(BaseResponseStatus.REQUEST_ERROR.getMessage());
        }
    }
}