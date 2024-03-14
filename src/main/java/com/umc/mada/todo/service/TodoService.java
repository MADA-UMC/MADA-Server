package com.umc.mada.todo.service;

import com.umc.mada.todo.domain.*;
import com.umc.mada.todo.dto.*;
import com.umc.mada.todo.repository.CategoryStatisticsVO;
import com.umc.mada.todo.repository.RepeatTodoRepository;
import com.umc.mada.todo.repository.TodoStatisticsVO;
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
    private final RepeatTodoRepository repeatTodoRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;


    @Autowired
    public TodoService(UserRepository userRepository, TodoRepository todoRepository, RepeatTodoRepository repeatTodoRepository, CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.todoRepository = todoRepository;
        this.repeatTodoRepository = repeatTodoRepository;
        this.categoryRepository = categoryRepository;
    }

    // 투두 생성 로직
    public Map<String, Object> createTodo(User user, TodoRequestDto todoRequestDto) {
        validateUserId(user);
        validateCategoryId(todoRequestDto.getCategory().getId());
        validateTodoName(todoRequestDto.getTodoName());

        Category category = categoryRepository.findCategoryByUserIdAndId(user, todoRequestDto.getCategory().getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리 ID입니다."));
        // 투두 앤티티 생성
        Todo todo = new Todo(user, (todoRequestDto.getRepeat() != Repeat.N) ? null :
                (todoRequestDto.getDate() != null) ? todoRequestDto.getDate() : LocalDate.now(),
                category, todoRequestDto.getTodoName(), todoRequestDto.getComplete() != null ? todoRequestDto.getComplete() : false, todoRequestDto.getRepeat(), todoRequestDto.getRepeatInfo(), todoRequestDto.getStartRepeatDate(), todoRequestDto.getEndRepeatDate(), todoRequestDto.getIsDeleted() != null ? todoRequestDto.getIsDeleted() : false
        );

        // 투두를 저장하고 저장된 투두 앤티티 반환
        Todo savedTodo = todoRepository.save(todo);
        List<RepeatTodoResponseDto> repeatTodoResponseDtoList = new ArrayList<>();
        if(todo.getRepeat()!=Repeat.N){
            repeatTodoResponseDtoList = createRepeatTodos(savedTodo);
        }
        Map<String, Object> map = new LinkedHashMap<>();
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("Todo", TodoResponseDto.of(savedTodo));
        data.put("RepeatTodos", repeatTodoResponseDtoList);
        map.put("data", data);
        return map;
    }

    /**
     * 투두 평균 API
     */
    public TodoStatisticsResponseDto calcTodoAverage(User user, TodoStatisticsRequestDto todoStatisticsRequestDto){
        LocalDate date =  todoStatisticsRequestDto.getDate();
        String option = todoStatisticsRequestDto.getOption();
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("nickName", user.getNickname());
        
        LocalDate startDay = null;
        LocalDate endDay = null;
        int length = 0;
        if(option.equals("week")) {
            startDay = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
            endDay= date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));
            
            if(startDay.getMonthValue() != date.getMonthValue()){
                startDay = date.with(TemporalAdjusters.firstDayOfMonth());
            }
            if(endDay.getMonthValue() != date.getMonthValue()){
                endDay = date.with(TemporalAdjusters.lastDayOfMonth());
            }

            length = startDay.until(endDay).getDays() + 1;
        }
        if(option.equals("month")) {
            startDay = date.with(TemporalAdjusters.firstDayOfMonth());
            endDay = date.with(TemporalAdjusters.lastDayOfMonth());
            
            length = date.lengthOfMonth(); //해당 달의 길이
        }
        TodoStatisticsVO todoStatisticsVO = todoRepository.findTodosMonthAVG(user.getId(), startDay, endDay, length);
        List<CategoryStatisticsVO> categoryStatisticsVOs = todoRepository.findCategoryAVG(user.getId(), startDay, endDay, length);
        if(categoryStatisticsVOs.size()==0){
//            CategoryStatisticsDto defaultCategoryStatisticsDto = new CategoryStatisticsDto()
            List<Category> categories =  categoryRepository.findCategoriesByUserId(user);
            List<CategoryStatisticsDto> categoryStatisticsDtos = categories.stream()
                    .map(category -> CategoryStatisticsDto.of(category.getCategoryName(), category.getColor(), (float) 0))
                    .collect(Collectors.toList());
            return new TodoStatisticsResponseDto(user.getNickname(),todoStatisticsVO.getTodosPercent(), todoStatisticsVO.getCompleteTodoPercent(), categoryStatisticsDtos);
        }
        return TodoStatisticsResponseDto.of(user.getNickname(),todoStatisticsVO.getTodosPercent(), todoStatisticsVO.getCompleteTodoPercent(), categoryStatisticsVOs);
    }


    @Transactional
    // 투두 수정 로직
    public Map<String, Object> updateTodo(User user, int todoId, TodoRequestDto todoRequestDto) {
        validateUserId(user);

        Todo todo = todoRepository.findTodoByUserIdAndId(user, todoId)
                .orElseThrow(() -> new IllegalArgumentException("NOT_FOUND_ERROR"));
        List <RepeatTodo> repeatTodos = repeatTodoRepository.readRepeatTodosByTodoId(todo);
        validateCategoryId(todo.getCategory().getId());

        // 반복 변경 처리
        if (todoRequestDto.getRepeat() != null){
            for(RepeatTodo repeatTodo : repeatTodos){
                repeatTodo.setIsDeleted(true);
            }
            if (todoRequestDto.getRepeat() == Repeat.DAY){
                todo.setRepeat(Repeat.DAY);
                todo.setRepeatInfo(null);
                todoRepository.save(todo);
                createRepeatTodos(todo);
            } else if (todoRequestDto.getRepeat() == Repeat.WEEK){
                todo.setRepeat(Repeat.WEEK);
                todo.setRepeatInfo(todoRequestDto.getRepeatInfo());
                todoRepository.save(todo);
                createRepeatTodos(todo);
            } else if (todoRequestDto.getRepeat() == Repeat.MONTH){
                todo.setRepeat(Repeat.MONTH);
                todo.setRepeatInfo(todoRequestDto.getRepeatInfo());
                todoRepository.save(todo);
                createRepeatTodos(todo);
            }
        }

        // 투두 반복 시작일 / 종료일 변경 처리
        if (todoRequestDto.getStartRepeatDate() != null || todoRequestDto.getEndRepeatDate() != null){
            for (RepeatTodo repeatTodo : repeatTodos) {
                LocalDate repeatTodoDate = repeatTodo.getDate();
                if (repeatTodoDate.isAfter(todoRequestDto.getEndRepeatDate()) || repeatTodoDate.isBefore(todoRequestDto.getStartRepeatDate())) {
                    repeatTodo.setIsDeleted(true);
                }
            }
            Set<LocalDate> existingRepeatDates = repeatTodos.stream()
                    .filter(repeatTodo -> !repeatTodo.getIsDeleted())
                    .map(RepeatTodo::getDate)
                    .collect(Collectors.toSet());

            for (LocalDate currentDate = todoRequestDto.getStartRepeatDate();
                 !currentDate.isAfter(todoRequestDto.getEndRepeatDate());
                 currentDate = currentDate.plusDays(1)) {
                if (!existingRepeatDates.contains(currentDate)) {
                    // 기존 반복 투두에 없는 날짜에 대해 새로운 반복 투두 생성
                    RepeatTodo newRepeatTodo = RepeatTodo.builder()
                            .todoId(todo)
                            .date(currentDate)
                            .complete(false)
                            .isDeleted(false)
                            .build();
                    repeatTodoRepository.save(newRepeatTodo);
                    repeatTodos.add(newRepeatTodo);
                }
            }

            todo.setStartRepeatDate(todoRequestDto.getStartRepeatDate());
            todo.setEndRepeatDate(todoRequestDto.getEndRepeatDate());
            if(todo.getRepeat() == Repeat.N){
                throw new IllegalArgumentException("반복 투두가 아닌 경우 반복 시작일을 설정할 수 없습니다.");
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

        // 투두 날짜 변경 처리
        if (todoRequestDto.getDate() != null){
            todo.setDate((todoRequestDto.getDate()));
        }

        // 수정된 Todo를 저장하고 저장된 투두 엔티티 반환
        Todo updatedTodo = todoRepository.save(todo);
        List<RepeatTodoResponseDto> updatedRepeatTodos = repeatTodoRepository.findRepeatTodosByTodoIdAndIsDeletedIsFalse(updatedTodo);

        // 저장된 투두 정보를 기반으로 TodoResponseDto 생성하여 반환
        Map<String, Object> map = new LinkedHashMap<>();
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("Todo", TodoResponseDto.of(updatedTodo));
        data.put("RepeatTodos", updatedRepeatTodos);
        map.put("data", data);
        return map;
    }

    @Transactional
    // 투두 삭제 로직
    public void deleteTodo(User userId, int todoId) {
        // 주어진 투두 ID를 이용하여 투두 엔티티 조회
        //Optional<Todo> optionalTodo = todoRepository.findTodoByUserIdAndId(userId, todoId);
        Todo todo = todoRepository.findTodoByUserIdAndId(userId, todoId)
                        .orElseThrow(() -> new IllegalArgumentException("NOT_FOUND_ERROR"));
        List <RepeatTodo> repeatTodos = repeatTodoRepository.readRepeatTodosByTodoId(todo);
        validateCategoryId(todo.getCategory().getId());

        if (!todo.getIsDeleted()) {
            //Todo todo = optionalTodo.get();
            todo.setIsDeleted(true);
            todoRepository.save(todo);
        } else {
            throw new IllegalArgumentException(BaseResponseStatus.NOT_FOUND.getMessage());
        }

        for(RepeatTodo repeatTodo: repeatTodos){
            repeatTodo.setIsDeleted(true);
            repeatTodoRepository.save(repeatTodo);
        }
    }

    @Transactional
    // 반복 투두 수정 로직
    public RepeatTodoResponseDto updateRepeatTodo(User user, int repeatTodoId, RepeatTodoRequestDto repeatTodoRequestDto) {
        validateUserId(user);
        RepeatTodo repeatTodo = repeatTodoRepository.findById(repeatTodoId)
                .orElseThrow(() -> new IllegalArgumentException("NOT_FOUND_ERROR"));
        Todo todo = repeatTodo.getTodoId();
        User todoUser = todo.getUserId();

        // 반복 투두 완료 여부 변경 처리
        if (repeatTodoRequestDto.getComplete() != null && todoUser.getId().equals(user.getId())){
            repeatTodo.setComplete(repeatTodoRequestDto.getComplete());
            repeatTodoRepository.save(repeatTodo);
        }
        return new RepeatTodoResponseDto(repeatTodo.getId(), repeatTodo.getTodoId().getId(), repeatTodo.getTodoId().getCategory().getId(), repeatTodo.getTodoId().getTodoName(), repeatTodo.getDate(), repeatTodo.getComplete());
    }

    @Transactional
    // 반복 투두 삭제 로직 (이 반복 투두)
    public void deleteRepeatTodo(User userId, int repeatTodoId) {
        RepeatTodo repeatTodo = repeatTodoRepository.findById(repeatTodoId)
                .orElseThrow(() -> new IllegalArgumentException("NOT_FOUND_ERROR"));
        validateCategoryId(repeatTodo.getTodoId().getCategory().getId());
        Todo todo = repeatTodo.getTodoId();
        User todoUser = todo.getUserId();
        if (todoUser.getId().equals(userId.getId())) {
            repeatTodo.setIsDeleted(true);
            repeatTodoRepository.save(repeatTodo);
        }
    }

    @Transactional
    // 반복 투두 삭제 로직 (이 반복 투두 및 향후 반복 투두)
    public void deleteRepeatTodoAndFuture(User userId, int repeatTodoId){
        RepeatTodo repeatTodo = repeatTodoRepository.findById(repeatTodoId)
                .orElseThrow(() -> new IllegalArgumentException("NOT_FOUND_ERROR"));
        validateCategoryId(repeatTodo.getTodoId().getCategory().getId());
        Todo todo = repeatTodo.getTodoId();
        User todoUser = todo.getUserId();
        if (todoUser.getId().equals(userId.getId())) {
            List<RepeatTodo> futureRepeatTodos = repeatTodoRepository.findAllByTodoIdAndDateGreaterThanEqual(todo, repeatTodo.getDate());

            for (RepeatTodo futureRepeatTodo : futureRepeatTodos) {
                futureRepeatTodo.setIsDeleted(true);
                repeatTodoRepository.save(futureRepeatTodo);
            }
        }
    }

    @Transactional
    // 반복 투두 삭제 로직 (모든 반복 투두)
    public void deleteAllRepeatTodos(User userId, int repeatTodoId){
        RepeatTodo repeatTodo = repeatTodoRepository.findById(repeatTodoId)
                .orElseThrow(() -> new IllegalArgumentException("NOT_FOUND_ERROR"));
        validateCategoryId(repeatTodo.getTodoId().getCategory().getId());
        Todo todo = repeatTodo.getTodoId();
        User todoUser = todo.getUserId();
        if (todoUser.getId().equals(userId.getId())) {
            List<RepeatTodo> allRepeatTodos = repeatTodoRepository.findAllByTodoId(todo);

            for (RepeatTodo repeatTodos : allRepeatTodos) {
                repeatTodos.setIsDeleted(true);
                repeatTodoRepository.save(repeatTodos);
            }
        }
    }

    // 특정 유저 투두 조회 로직
    public Map<String, Object> getUserTodo(User userId, LocalDate date) {
        List<Todo> userTodos = todoRepository.findTodosByUserIdAndIsDeletedIsFalse(userId);
        List<RepeatTodo> repeatTodos = repeatTodoRepository.findRepeatTodosByDateIsAndIsDeletedIsFalse(date);
        List<RepeatTodoResponseDto> filteredRepeatTodos = new ArrayList<>();
        List<TodoResponseDto> filteredTodos = new ArrayList<>();
        Map<String, Object> map = new LinkedHashMap<>();
        Map<String, Object> data = new LinkedHashMap<>();
        for (Todo todo : userTodos){
            if(todo.getDate()!= null && todo.getDate().equals(date) && todo.getRepeat().equals(Repeat.N)){
                filteredTodos.add(TodoResponseDto.of(todo));
            }
        }
        for (RepeatTodo repeatTodo : repeatTodos){
            if(repeatTodo.getTodoId().getUserId() == userId){
                filteredRepeatTodos.add(RepeatTodoResponseDto.of(repeatTodo));
            }
        }
        data.put("nickname", userId.getNickname());
        data.put("TodoList", filteredTodos);
        data.put("RepeatTodoList", filteredRepeatTodos);
        map.put("data", data);
        return map;
   }

   // 특정 유저 반복 투두 조회 로직
    public List<TodoResponseDto> getUserRepeatTodo(User userId){
        List<Repeat> nonNRepeats = Arrays.asList(Repeat.DAY, Repeat.WEEK, Repeat.MONTH);
        List<Todo> nonNRepeatTodos = todoRepository.findByUserIdAndRepeatIn(userId, nonNRepeats);
        return nonNRepeatTodos.stream()
                .filter((todo -> !todo.getIsDeleted()&& !todo.getCategory().getIsInActive()))
                .map(TodoResponseDto::of)
                .collect(Collectors.toList());
    }

    // 반복 투두 생성 로직
    public List<RepeatTodoResponseDto> createRepeatTodos(Todo todo) {
        LocalDate startRepeatDate = todo.getStartRepeatDate();
        LocalDate endRepeatDate = todo.getEndRepeatDate();

        List<LocalDate> dates = new ArrayList<>();
        List<RepeatTodo> repeatTodos = new ArrayList<>();

        if (todo.getRepeat() == Repeat.DAY){
            while(!startRepeatDate.isAfter(endRepeatDate)){
                dates.add(startRepeatDate);
                startRepeatDate = startRepeatDate.plusDays(1);
            }

        } else if (todo.getRepeat() == Repeat.WEEK) {
            int dayOfWeek = todo.getRepeatInfo();
            startRepeatDate = startRepeatDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.of(dayOfWeek)));
            while (!startRepeatDate.isAfter(endRepeatDate)) {
                dates.add(startRepeatDate);
                startRepeatDate = startRepeatDate.plusWeeks(1);
            }
        } else if (todo.getRepeat() == Repeat.MONTH){
            int dayOfMonth = todo.getRepeatInfo();
            // 반복 시작일이 반복 종료일 이후인 경우 반복 중지
            while (!startRepeatDate.isAfter(endRepeatDate) && !startRepeatDate.isBefore(todo.getStartRepeatDate())) {
                if (dayOfMonth != 0) {
                    // repeatInfo가 0이 아닌 경우
                    int startDay = startRepeatDate.getDayOfMonth();

                    if (dayOfMonth < startDay) {
                        startRepeatDate = startRepeatDate.plusMonths(1).withDayOfMonth(dayOfMonth);
                    } else {
                        startRepeatDate = startRepeatDate.withDayOfMonth(dayOfMonth);
                    }
                } else {
                    // repeatInfo가 0인 경우
                    startRepeatDate = startRepeatDate.withDayOfMonth(startRepeatDate.lengthOfMonth());
                }
                dates.add(startRepeatDate);
                startRepeatDate = startRepeatDate.plusMonths(1);
            }
        }

        for (LocalDate date : dates){
            RepeatTodo repeatTodo = RepeatTodo.builder()
                    .todoId(todo)
                    .date(date)
                    .complete(false)
                    .isDeleted(false)
                    .build();
            repeatTodoRepository.save(repeatTodo);
            repeatTodos.add(repeatTodo);
        }
        List<RepeatTodoResponseDto> result = new ArrayList<>();
        for(RepeatTodo repeatTodo: repeatTodos){
            result.add(repeatTodoToDto(repeatTodo));
        }
        return result;
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
        Category category = optionalCategory.get();
        if (category.getIsInActive()) {
            throw new IllegalArgumentException("종료된 카테고리에는 투두를 생성,수정,삭제할 수 없습니다.");
        }
    }

    public RepeatTodoResponseDto repeatTodoToDto(RepeatTodo repeatTodo){
        return RepeatTodoResponseDto.builder()
                .id(repeatTodo.getId())
                .todoId(repeatTodo.getTodoId().getId())
                .date(repeatTodo.getDate())
                .complete(repeatTodo.getComplete())
                .build();
    }

}