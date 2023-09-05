package com.umc.mada.todo.service;

import com.umc.mada.todo.domain.*;
import com.umc.mada.todo.dto.*;
import com.umc.mada.todo.repository.StatisticsVO;
import com.umc.mada.todo.repository.TodoRepository;
import com.umc.mada.global.BaseResponseStatus;
import com.umc.mada.category.domain.Category;
import com.umc.mada.category.repository.CategoryRepository;
import com.umc.mada.user.domain.User;
import com.umc.mada.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.time.DayOfWeek;
import java.time.temporal.TemporalAdjusters;

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
    public TodoResponseDto createTodo(User user, TodoRequestDto todoRequestDto) {
        // 유효성 검사 메서드를 호출하여 해당 ID가 데이터베이스에 존재하는지 확인
        validateUserId(user);
        validateCategoryId(todoRequestDto.getCategory().getId());
        validateTodoName(todoRequestDto.getTodoName());

        Category category = categoryRepository.findCategoryByUserIdAndId(user, todoRequestDto.getCategory().getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리 ID입니다."));

        // 투두 앤티티 생성
        Todo todo = new Todo(
                user,
                todoRequestDto.getDate() != null ? todoRequestDto.getDate() : LocalDate.now(),
                category,
                todoRequestDto.getTodoName(),
                todoRequestDto.getComplete() != null ? todoRequestDto.getComplete() : false,
                todoRequestDto.getRepeat(),
                todoRequestDto.getRepeatWeek(),
                todoRequestDto.getRepeatMonth(),
                todoRequestDto.getStartRepeatDate(),
                todoRequestDto.getEndRepeatDate(),
                todoRequestDto.getIsDeleted() != null ? todoRequestDto.getIsDeleted() : false
        );

        // 투두를 저장하고 저장된 투두 앤티티 반환
        Todo savedTodo = todoRepository.save(todo);

        // 저장된 투두 정보를 기반으로 TodoResponseDto 생성하여 반환
        return TodoResponseDto.of(savedTodo);
    }

    /**
     * 투두 평균 API
     */
    public Map<String, Object> calcTodoAverage(User user, TodoAverageRequestDto todoAverageRequestDto){
        LocalDate date =  todoAverageRequestDto.getDate();
        String option = todoAverageRequestDto.getOption();

        if(option.equals("week")) {
            LocalDate firstDayOfFirstWeek = date.with(TemporalAdjusters.firstInMonth(DayOfWeek.SUNDAY));

            // 해당 월의 첫 주의 마지막날을 구합니다
            LocalDate lastDayOfFirstWeek = firstDayOfFirstWeek.plusDays(6);
//            List<Todo> todoList = todoRepository.findTodosByUserIdAndDateBetweenAndRepeat(user, firstDayOfFirstWeek,lastDayOfFirstWeek,Repeat.N);
//
//            List<Todo> todos = todoRepository.findTodosByUserIdAndRepeat(user,Repeat.N);
//
//            if(todoList.size() == 0||todos.size()==0){
//                return TodoAverageResponseDto.builder().todosPercent((double)todos.size()).completeTodoPercent((double)todoList.size()).build();
//            }
//            List<Todo> completeTodoList = todoList.stream().filter(todo-> todo.getComplete()).collect(Collectors.toList());
//            double completePercent =  Math.round(completeTodoList.size()/todoList.size()*10)/10.0;
//            double todosPercent =  Math.round(todoList.size()/todos.size()*10)/10.0;
//
//            return TodoAverageResponseDto.builder().todosPercent(todosPercent).completeTodoPercent(completePercent).build();
//            Optional<TodoRepository.statisticsVO> statisticsVO = todoRepository.findTodosAVG(user.getId(), firstDayOfFirstWeek,lastDayOfFirstWeek);
//            return statisticsVO.get();
            List<StatisticsVO> statisticsVOS = todoRepository.findTodosWeekAVG(user.getId(), firstDayOfFirstWeek, lastDayOfFirstWeek);
            Map<String, Object> map = new LinkedHashMap<>();
            List<TodoStatisticsResponseDto> result = new ArrayList<>();
            for(StatisticsVO s : statisticsVOS){
                result.add(TodoStatisticsResponseDto.of(s.getTodosPercent(),s.getCompleteTodoPercent()));
            }
            map.put("statistics", result);
            return map;
        }
        if(option.equals("month")) {
            LocalDate firstDayOfMonth = date.with(TemporalAdjusters.firstDayOfMonth());

            // 해당 달의 마지막날을 가져옵니다
            LocalDate lastDayOfMonth = date.with(TemporalAdjusters.lastDayOfMonth());
//            List<Todo> todoList = todoRepository.findTodosByUserIdAndDateBetweenAndRepeat(user, firstDayOfMonth,lastDayOfMonth,Repeat.N);
//            List<Todo> todos = todoRepository.findTodosByUserIdAndRepeat(user,Repeat.N);
//            if(todoList.size() == 0){
//                return TodoAverageResponseDto.builder().todosPercent((double)todos.size()).completeTodoPercent((double)todoList.size()).build();
//            }
//            List<Todo> completeTodoList = todoList.stream().filter(todo-> todo.getComplete()).collect(Collectors.toList());
//            double completePercent =  Math.round(completeTodoList.size()/todoList.size()*10)/10.0;
//            double todosPercent = Math.round( todoList.size()/todos.size()*10)/10.0;
//            return TodoAverageResponseDto.builder().todosPercent(todosPercent).completeTodoPercent(completePercent).build();
//            Optional<TodoRepository.statisticsVO> statisticsVO = todoRepository.findTodosMonthAVG(user.getId(), firstDayOfMonth, lastDayOfMonth);
//            return todoRepository.findTodosMonthAVG(user.getId(), firstDayOfMonth, lastDayOfMonth)
//                    .stream()
//                    .map(TodoStatisticsResponseDto::of)
//                    .collect(Collectors.toList());
            List<StatisticsVO> statisticsVOS = todoRepository.findTodosMonthAVG(user.getId(), firstDayOfMonth, lastDayOfMonth);
            Map<String, Object> map = new LinkedHashMap<>();
            List<TodoStatisticsResponseDto> result = new ArrayList<>();
            for(StatisticsVO s : statisticsVOS){
                result.add(TodoStatisticsResponseDto.of(s.getTodosPercent(),s.getCompleteTodoPercent()));
            }
            map.put("statistics", result);
            return map;
        }
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("statistics",TodoAverageResponseDto.builder().todosPercent(0.0).completeTodoPercent(0.0).build());
        return map;
    }


    @Transactional
    // 투두 수정 로직
    public TodoResponseDto updateTodo(User user, int todoId, TodoRequestDto todoRequestDto) {
        // 유효성 검사 메서드를 호출하여 해당 ID가 데이터베이스에 존재하는지 확인
        validateUserId(user);

        // 주어진 todoId를 이용하여 카테고리 엔티티 조회
        Todo todo = todoRepository.findTodoByUserIdAndId(user, todoId)
                .orElseThrow(() -> new IllegalArgumentException("NOT_FOUND_ERROR"));

        // 투두 반복 변경 처리
        Repeat repeat;

        if (todoRequestDto.getRepeat() != null){
            repeat = Repeat.valueOf(todoRequestDto.getRepeat().name());
            todo.setRepeat(repeat);
        }else{
            repeat = todo.getRepeat();
        }

        // 투두 매주 반복 변경 처리
        RepeatWeek repeatWeek = null;
        if(todoRequestDto.getRepeatWeek() != null){
            repeatWeek = RepeatWeek.valueOf(todoRequestDto.getRepeatWeek().name());
            todo.setRepeatWeek(repeatWeek);
            if(todo.getRepeat() != Repeat.WEEK){
                throw new IllegalArgumentException("매주 반복이 아닌 경우 반복 요일을 설정할 수 없습니다.");
            }
        }else{
            repeatWeek = todo.getRepeatWeek();
        }

        // 투두 매달 반복 변경 처리
        RepeatMonth repeatMonth = null;
        if(todoRequestDto.getRepeatMonth() != null){
            repeatMonth = RepeatMonth.fromValue(todoRequestDto.getRepeatMonth().getDayOfMonth());
            todo.setRepeatMonth(repeatMonth);
            if(todo.getRepeat() != Repeat.MONTH){
                throw new IllegalArgumentException("매달 반복이 아닌 경우 반복 날짜를 설정할 수 없습니다.");
            }
        }else{
            repeatMonth = todo.getRepeatMonth();
        }

        if (todoRequestDto.getRepeat() == Repeat.N){
            repeatWeek = null;
            repeatMonth = null;
            todo.setRepeatWeek(repeatWeek);
            todo.setRepeatMonth(repeatMonth);
            todo.setStartRepeatDate(null);
            todo.setEndRepeatDate(null);
            if (todoRequestDto.getRepeatWeek() != null || todoRequestDto.getRepeatMonth() != null || todoRequestDto.getStartRepeatDate() != null || todoRequestDto.getEndRepeatDate() != null) {
                throw new IllegalArgumentException("repeat 값이 N인 경우 repeatWeek, repeatMonth, startRepeatDate, endRepeatDate는 모두 null이어야 합니다.");
            }
        } else if (todoRequestDto.getRepeat() == Repeat.DAY) {
            repeatWeek = null;
            repeatMonth = null;
            todo.setRepeatWeek(repeatWeek);
            todo.setRepeatMonth(repeatMonth);
            if (todoRequestDto.getRepeatWeek() != null || todoRequestDto.getRepeatMonth() != null) {
                throw new IllegalArgumentException("repeat 값이 DAY인 경우 repeatWeek, repeatMonth는 null이어야 합니다.");
            }
        } else if (todoRequestDto.getRepeat() == Repeat.WEEK) {
            repeatMonth = null;
            todo.setRepeatMonth(repeatMonth);
            if (todoRequestDto.getRepeatWeek() == null || todoRequestDto.getRepeatMonth() != null) {
                throw new IllegalArgumentException("repeatWeek 값은 null이 될 수 없으며, repeatMonth는 null이어야 합니다.");
            }
        } else if (todoRequestDto.getRepeat() == Repeat.MONTH){
            repeatWeek = null;
            todo.setRepeatWeek(repeatWeek);
            if (todoRequestDto.getRepeatMonth() == null || todoRequestDto.getRepeatWeek() != null) {
                throw new IllegalArgumentException("repeatMonth 값은 null이 될 수 없으며, repeatWeek는 null이어야 합니다.");
            }
        }

        // 카테고리 ID 변경 처리
        if (todoRequestDto.getCategory() != null) {
            Category category = categoryRepository.findCategoryByUserIdAndId(user, todoRequestDto.getCategory().getId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리 ID입니다."));
            todo.setCategory(category);
        }

        // 투두 이름 변경 처리
        if (todoRequestDto.getTodoName() != null) {
            todo.setTodoName(todoRequestDto.getTodoName());
        }

        // 투두 완료 여부 변경 처리
        if (todoRequestDto.getComplete() != null){
            todo.setComplete(todoRequestDto.getComplete());
        }

        // 투두 반복 시작일 / 종료일 변경 처리
        if (todoRequestDto.getStartRepeatDate() != null){
            todo.setStartRepeatDate(todoRequestDto.getStartRepeatDate());
            if(todo.getRepeat() == Repeat.N){
                throw new IllegalArgumentException("반복 투두가 아닌 경우 반복 시작일을 설정할 수 없습니다.");
            }
        }

        if (todoRequestDto.getEndRepeatDate() != null){
            todo.setEndRepeatDate(todoRequestDto.getEndRepeatDate());
            if(todo.getRepeat() == Repeat.N){
                throw new IllegalArgumentException("반복 투두가 아닌 경우 반복 종료일을 설정할 수 없습니다.");
            }
        }

        // 수정된 Todo를 저장하고 저장된 투두 엔티티 반환
        Todo updatedTodo = todoRepository.save(todo);

        // 저장된 투두 정보를 기반으로 TodoResponseDto 생성하여 반환
        return TodoResponseDto.of(updatedTodo);
    }

    @Transactional
    // 투두 삭제 로직
    public void deleteTodo(User userId, int todoId) {
        // 주어진 투두 ID를 이용하여 투두 엔티티 조회
        Optional<Todo> optionalTodo = todoRepository.findTodoByUserIdAndId(userId, todoId);
        if (optionalTodo.isPresent() && !optionalTodo.get().getIsDeleted()) {
            Todo todo = optionalTodo.get();
            todo.setIsDeleted(true);
            todoRepository.save(todo);
        } else {
            throw new IllegalArgumentException(BaseResponseStatus.NOT_FOUND.getMessage());
        }
    }

    // 특정 유저 투두 조회 로직
    public List<TodoResponseDto> getUserTodo(User userId, LocalDate date) {
        /* List<Todo> userTodos = todoRepository.findTodosByUserIdAndDateIs(userId, date);*/
        List<Todo> userTodos = todoRepository.findTodosByUserId(userId);
        List<Todo> filteredTodos = new ArrayList<>();

        for (Todo todo : userTodos){
            if (todo.getStartRepeatDate() != null && todo.getEndRepeatDate() != null) {
                if (!date.isBefore(todo.getStartRepeatDate()) && !date.isAfter(todo.getEndRepeatDate())) {
                    if (!todo.getCategory().getIsInActive()) {
                        filteredTodos.add(todo);
                    }
                }
            } else {
                if (todo.getDate().equals(date)) {
                    if (!todo.getCategory().getIsInActive()) {
                        filteredTodos.add(todo);
                    }
                }
            }
        }
        return filteredTodos.stream()
                .filter((todo -> !todo.getIsDeleted()))
                .map(TodoResponseDto::of)
                .collect(Collectors.toList());
        // 조회 결과가 존재하는 경우에는 해당 할 일을 TodoResponseDto로 매핑하여 반환
        //return userTodos.stream().map(TodoResponseDto::of).collect(Collectors.toList());
   }

   // 특정 유저 반복 투두 조회 로직
    public List<TodoResponseDto> getUserRepeatTodo(User userId){
        List<Repeat> nonNRepeats = Arrays.asList(Repeat.DAY, Repeat.WEEK, Repeat.MONTH);
        List<Todo> nonNRepeatTodos = todoRepository.findByUserIdAndRepeatIn(userId, nonNRepeats);
        return nonNRepeatTodos.stream().map(TodoResponseDto::of).collect(Collectors.toList());
    }

    // 투두 이름 유효성 검사 메서드
    private void validateTodoName(String todoName) {
        if (todoName == null || todoName.isEmpty()) {
            throw new IllegalArgumentException(BaseResponseStatus.REQUEST_ERROR.getMessage());
        }
    }

    // 유저 ID 유효성 검사 메서드
    private void validateUserId(User userId) {
        Optional<User> optionalUser = userRepository.findUserById(userId.getId());
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