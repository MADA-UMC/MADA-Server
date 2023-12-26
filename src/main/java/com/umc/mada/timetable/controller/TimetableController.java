package com.umc.mada.timetable.controller;

import com.umc.mada.calendar.domain.Calendar;
import com.umc.mada.calendar.repository.CalendarRepository;
import com.umc.mada.timetable.dto.TimetableRequestDto;
import com.umc.mada.timetable.dto.TimetableResponseDto;
import com.umc.mada.timetable.service.TimetableService;
import com.umc.mada.todo.domain.Todo;
import com.umc.mada.todo.repository.TodoRepository;
import com.umc.mada.user.domain.User;
import com.umc.mada.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/home/time")
public class TimetableController {
    private final TimetableService timetableService;
    private final UserRepository userRepository;
    private final TodoRepository todoRepository;
    private final CalendarRepository calendarRepository;

    @Autowired
    public TimetableController(TodoRepository todoRepository, CalendarRepository calendarRepository, TimetableService timetableService, UserRepository userRepository) {
        this.todoRepository = todoRepository;
        this.calendarRepository = calendarRepository;
        this.timetableService = timetableService;
        this.userRepository = userRepository;
    }

    /**
     * 일일시간표
     *
     */

    @PostMapping("/daily")
    public ResponseEntity<Map<String, Object>> createDailyTimetable(Authentication authentication, @RequestBody TimetableRequestDto timetableRequestDto){
        // 일일 시간표 일정 생성 API
        Optional<User> userOptional = userRepository.findByAuthId(authentication.getName());
        User user = userOptional.get();
        TimetableResponseDto newTimetable = timetableService.createTimetable(user, timetableRequestDto);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("DailyTimetable", newTimetable);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("data", data);
        //result.put("status", 200);
        //result.put("success", true);
        //result.put("message", "시간표 생성이 완료되었습니다.");
        return ResponseEntity.ok().body(result);
    }

    @PatchMapping("/daily/update/{scheduleId}")
    public ResponseEntity<Map<String, Object>> updateDailyTimetable(Authentication authentication, @PathVariable int scheduleId, @RequestBody TimetableRequestDto timetableRequestDto){
        // 일일 시간표 일정 수정 API
        Optional<User> userOptional = userRepository.findByAuthId(authentication.getName());
        User user = userOptional.get();
        TimetableResponseDto updatedTimetable = timetableService.updateTimetable(user, scheduleId, timetableRequestDto);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("DailyTimetable", updatedTimetable);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("data", data);
        //result.put("status", 200);
        //result.put("success", true);
        //result.put("message", "시간표 수정이 완료되었습니다.");
        return ResponseEntity.ok().body(result);
    }

    @PatchMapping("/daily/delete/{scheduleId}")
    public ResponseEntity<Map<String, Object>> deleteDailyTimetable(Authentication authentication, @PathVariable int scheduleId) {
        // 일일 시간표 일정 삭제 API
        Optional<User> userOptional = userRepository.findByAuthId(authentication.getName());
        User user = userOptional.get();
        timetableService.deleteTimetable(user, scheduleId);
        Map<String, Object> result = new LinkedHashMap<>();
        //result.put("status", 200);
        //result.put("success", true);
        //result.put("message", "시간표 삭제가 완료되었습니다.");
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/daily/date/{date}")
    // 일일 시간표 조회 API
    public ResponseEntity<Map<String, Object>> getUserDailyTimetable(Authentication authentication, @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date){
        Optional<User> userOptional = userRepository.findByAuthId(authentication.getName());
        User user = userOptional.get();
        List<TimetableResponseDto> userTimetables = timetableService.getDailyTimetable(user, date);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("DailyTimetableList", userTimetables);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("data", data);
        //result.put("status", 200);
        //result.put("success", true);
        //result.put("message", "시간표가 정상적으로 조회되었습니다.");
        return ResponseEntity.ok().body(result);
    }

    /**
     * 주간시간표
     *
     */
    @PostMapping("/weekly/create")
    public ResponseEntity<Map<String, Object>> createWeeklyTimetable(Authentication authentication, @RequestBody TimetableRequestDto timetableRequestDto){
        // 주간 시간표 일정 생성 API
        Optional<User> userOptional = userRepository.findByAuthId(authentication.getName());
        User user = userOptional.get();
        TimetableResponseDto newTimetable = timetableService.createTimetable(user, timetableRequestDto);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("WeeklyTimetable", newTimetable);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("data", data);
        //result.put("status", 200);
        //result.put("success", true);
        //result.put("message", "시간표 생성이 완료되었습니다.");
        return ResponseEntity.ok().body(result);
    }

    @PatchMapping("/weekly/update/{scheduleId}")
    public ResponseEntity<Map<String, Object>> updateWeeklyTimetable(Authentication authentication, @PathVariable int scheduleId, @RequestBody TimetableRequestDto timetableRequestDto){
        // 주간 시간표 일정 수정 API
        Optional<User> userOptional = userRepository.findByAuthId(authentication.getName());
        User user = userOptional.get();
        TimetableResponseDto updatedTimetable = timetableService.updateTimetable(user, scheduleId, timetableRequestDto);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("WeeklyTimetable", updatedTimetable);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("data", data);
        //result.put("status", 200);
        //result.put("success", true);
        //result.put("message", "시간표 수정이 완료되었습니다.");
        return ResponseEntity.ok().body(result);
    }

    @PatchMapping("/weekly/delete/{scheduleId}")
    public ResponseEntity<Map<String, Object>> deleteWeeklyTimetable(Authentication authentication, @PathVariable int scheduleId) {
        // 주간 시간표 일정 삭제 API
        Optional<User> userOptional = userRepository.findByAuthId(authentication.getName());
        User user = userOptional.get();
        timetableService.deleteTimetable(user, scheduleId);
        Map<String, Object> result = new LinkedHashMap<>();
        //result.put("status", 200);
        //result.put("success", true);
        //result.put("message", "시간표 삭제가 완료되었습니다.");
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/weekly")
    // 주간 시간표 일정 조회 API
    public ResponseEntity<Map<String, Object>> getUserWeeklyTimetable(Authentication authentication){
        Optional<User> userOptional = userRepository.findByAuthId(authentication.getName());
        User user = userOptional.get();
        List<TimetableResponseDto> userTimetables = timetableService.getWeeklyTimetable(user);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("WeeklyTimetableList", userTimetables);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("data", data);
        //result.put("status", 200);
        //result.put("success", true);
        //result.put("message", "시간표가 정상적으로 조회되었습니다.");
        return ResponseEntity.ok().body(result);
    }


    @GetMapping("search/date/{date}")
    // 시간표 추가 시, 특정 유저 일정(캘린더)과 투두 조회 API
    public ResponseEntity<Map<String, Object>> getTodoAndCalendar(Authentication authentication, @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        Optional<User> userOptional = userRepository.findByAuthId(authentication.getName());
        User user = userOptional.get();
        List<Todo> todos = todoRepository.findTodosByUserId(user);
        List<Calendar> calendars = calendarRepository.findAllByUser(user);
        List<Map<String, Object>> todoList = new ArrayList<>();
        for (Todo todo : todos) {
            Map<String, Object> todoMap = new LinkedHashMap<>();
            todoMap.put("iconId", todo.getCategory().getIcon().getId()); // Category의 아이콘 ID
            todoMap.put("todoName", todo.getTodoName());
            if (todo.getStartRepeatDate() != null && todo.getEndRepeatDate() != null){
                if (!date.isBefore(todo.getStartRepeatDate()) && !date.isAfter(todo.getEndRepeatDate())){
                    todoList.add(todoMap);
                }
            } else {
                if (todo.getDate().equals(date)) {
                    todoList.add(todoMap);
                }
            }
        }

        List<Map<String, Object>> calendarList = new ArrayList<>();
        for (Calendar calendar : calendars) {
            // 시작일과 종료일 사이에 date가 있는 경우만 추가
            if (!date.isBefore(calendar.getStartDate().toLocalDate()) && !date.isAfter(calendar.getEndDate().toLocalDate())) {
                Map<String, Object> calendarMap = new LinkedHashMap<>();
                calendarMap.put("CalendarName", calendar.getCalendarName());
                calendarMap.put("color", calendar.getColor());
                calendarMap.put("startTime", calendar.getStartTime()); // 시작 시간
                calendarMap.put("endTime", calendar.getEndTime());     // 종료 시간
                calendarMap.put("d-day", calendar.getDday());
                calendarList.add(calendarMap);
            }
        }
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("calendarList", calendarList);
        data.put("todoList", todoList);

        Map<String, Object> result = new LinkedHashMap<>();
        //result.put("status", 200);
        //result.put("success", true);
        //result.put("message", "Calendar와 Todo 정보 조회 완료");
        result.put("data",data);
        return ResponseEntity.ok().body(result);
    }
}
