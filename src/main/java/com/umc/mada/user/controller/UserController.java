package com.umc.mada.user.controller;

import com.umc.mada.exception.NotFoundUserException;
import com.umc.mada.timetable.domain.Timetable;
import com.umc.mada.todo.domain.Todo;
import com.umc.mada.todo.dto.TodoStatisticsRequestDto;
import com.umc.mada.todo.service.TodoService;
import com.umc.mada.user.domain.User;
import com.umc.mada.user.dto.nickname.NicknameRequestDto;
import com.umc.mada.user.repository.UserRepository;
import com.umc.mada.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.umc.mada.todo.repository.TodoRepository;
import com.umc.mada.timetable.repository.TimetableRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.*;


@RestController
@RequestMapping("/user") //user
public class UserController {
    private final UserService userService;
    private final TodoService todoService;
    private final UserRepository userRepository;
    private final TodoRepository todoRepository;
    private final TimetableRepository timetableRepository;


    @Autowired
    public UserController(UserService userService, TodoService todoService, UserRepository userRepository, TodoRepository todoRepository, TimetableRepository timetableRepository){
        this.userService = userService;
        this.todoService = todoService;
        this.userRepository = userRepository;
        this.todoRepository = todoRepository;
        this.timetableRepository = timetableRepository;
    }

    @GetMapping("/test")
    public void test(Authentication authentication, HttpServletResponse response, @RequestParam String token) throws IOException { //@AuthenticationPrincipal CusomtUserDetails cusomtUserDetails
//        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
//        System.out.println("oAuth2User = " + oAuth2User);
//        authentication.getPrincipal()
//        System.out.println(cusomtUserDetails.getUser());
//        System.out.println(authentication.getName(), false).orElseThrow(()-> new RuntimeException("올바른 유저 ID가 아닙니다."));
//        return ResponseEntity.status(HttpStatus.OK).body(authentication.getName(), false).orElseThrow(()-> new RuntimeException("올바른 유저 ID가 아닙니다."));

        //httpServletResponse 방법
        response.setHeader("Content-type", "text/plain");
        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer "+token);
//        PrintWriter writer = response.getWriter();
//        writer.println("ok");

        //responseEntity 방법
//        HttpHeaders headers = new HttpHeaders();
//        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+token);
//
//        return ResponseEntity.ok().headers(headers).build();
    }

//    @GetMapping("/oauth2/code/{provider}")
//    public ResponseEntity<String> login(@PathVariable String provider, HttpServletResponse response){
//        //response.getHeader(HttpHeaders.AUTHORIZATION)
//        System.out.println(response.getHeader(HttpHeaders.AUTHORIZATION));
//        return ResponseEntity.status(HttpStatus.OK).body(response.getHeader(HttpHeaders.AUTHORIZATION));
//    }

    @Operation(description = "회원가입 로그인")
    @GetMapping("/signup")
    public void signupToken(HttpServletResponse response, @RequestParam String token) throws IOException {//
        response.setHeader("Content-type", "text/plain");
        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer "+token);
        PrintWriter writer = response.getWriter();
        writer.println("ok");
    }

    @Operation(description = "회원가입한 유저가 닉네임 입력하는 곳")
    @PostMapping("/signup/nickName")
    public ResponseEntity<String> signupNickname(@RequestBody Map<String, String> nickname, Authentication authentication) {
        User user = userRepository.findByAuthIdAndAccountExpired(authentication.getName(), false).orElseThrow(()-> new RuntimeException("올바른 유저 ID가 아닙니다."));
        userService.setNickname(nickname, user);
//        return ResponseEntity.status(HttpStatus.OK).body("닉네임 입력 성공했습니다.");
        return ResponseEntity.ok().build();
    }

    @Operation(description = "로그아웃")
    @GetMapping("/logout")
    public ResponseEntity<String> logout() {
        //세션 삭제
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().build();
    }

    @Operation(description = "회원탈퇴")
    @DeleteMapping("/withdrawal")
    public ResponseEntity<String> userRemove (Authentication authentication){ //@AuthenticationPrincipal CusomtUserDetails cusomtUserDetails
//        User user = cusomtUserDetails.getUser();
        User user = userRepository.findByAuthIdAndAccountExpired(authentication.getName(), false).orElseThrow(()-> new RuntimeException("올바른 유저 ID가 아닙니다."));
        userService.removeUser(user);
        return ResponseEntity.ok().build();
    }

    /**
     * 프로필 편집창 API
     */
    @GetMapping("/profile/change")
    public ResponseEntity<Map<String, Object>>userProfileList(Authentication authentication) {
        Map<String, Object> map = new HashMap<>();
        map.put("data", userService.findUserProfile(authentication));
        return ResponseEntity.ok(map);
    }

    /**
     * 닉네임 변경 API
     */
    @PatchMapping("/profile/change/nickname")
    public ResponseEntity<Map<String, Object>>nicknameModify(Authentication authentication,
                                                             @Validated @RequestBody NicknameRequestDto changeNicknameRequestDto) {
        User user = userRepository.findByAuthIdAndAccountExpired(authentication.getName(), false).orElseThrow(()-> new RuntimeException("올바른 유저 ID가 아닙니다."));
        Map<String, Object> map = new HashMap<>();
        map.put("data", userService.modifyNickname(user, changeNicknameRequestDto));
//        if(bindingResult.hasErrors()){
//            ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();
//            return ResponseEntity
//        }
        return ResponseEntity.ok(map);
    }

    @PatchMapping("/attendance")
    public ResponseEntity<Map<String, Object>>attendanceCount(Authentication authentication) {
        Map<String, Object> map = new HashMap<>();
        map.put("data", userService.calcAttendance(authentication));
        return ResponseEntity.ok(map);
    }

    @GetMapping("/attendance/total")
    public ResponseEntity<Map<String, Object>>totalAttendanceCount(Authentication authentication) {
        Map<String, Object> map = new HashMap<>();
        map.put("data", userService.findAttendanceCount(authentication));
        return ResponseEntity.ok(map);
    }

    /**
     * 구독 설정 API
     */
    @Operation(description = "유료 회원 관리")
    @PatchMapping("/subscribe")
    public ResponseEntity<Void> subscribeToggleSave(Authentication authentication,@RequestBody Map<String,Boolean> is_subscribe){
//        Map<String,Object> map = new HashMap<>();
//        map.put("data",new HashMap<>().put("is_subscribe",userService.userSubscribeSettings(authentication,is_subscribe)));
        userService.isSubscribe(authentication,is_subscribe);
        return ResponseEntity.ok().build();
    }

    /**
     * 화면 설정 API
     */
    @PostMapping("/display/change")
    public ResponseEntity<Map<String,Object>> displayToggleSave(Authentication authentication, @RequestBody Map<String,Boolean> map) {
        return ResponseEntity.ok(userService.saveUserPageSet(authentication, map));
    }

    /**
     * 알림 설정 API
     */
    @PatchMapping("/alarm/change")
    public ResponseEntity<Map<String, Object>> alarmToggleSave(Authentication authentication, @RequestBody Map<String, Boolean> map) {
        return ResponseEntity.ok(userService.saveUserAlarmSet(authentication, map));
    }

    /**
     * 화면 설정 조회 API
     */
    @GetMapping("/display")
    public ResponseEntity<Map<String, Object>> displayToggleList(Authentication authentication) {
        Map<String, Object> map = new HashMap<>();
        map.put("data", userService.findUserPageSet(authentication));
        return ResponseEntity.ok(map);
    }

    /**
     * 알람 설정 조회 API
     */
    @GetMapping("/alarm")
    public ResponseEntity<Map<String, Object>> alarmToggleList(Authentication authentication) {
        Map<String, Object> map = new HashMap<>();
        map.put("data", userService.findUserAlarmSet(authentication));
        return ResponseEntity.ok(map);
    }

    /**
     * 투두 일별 통계 API
     */
    @GetMapping("/statistics/day/{date}")
    public ResponseEntity<Map<String, Object>> findDailyTodoAndTimetableAvg(Authentication authentication, @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date){
        User user= userRepository.findByAuthIdAndAccountExpired(authentication.getName(), false).orElseThrow(()-> new RuntimeException("올바른 유저 ID가 아닙니다."));
        List<Todo> todos = todoRepository.findTodosByUserIdAndDateIs(user, date);
        List<Timetable> timetables = timetableRepository.findTimetablesByUserIdAndDateIs(user, date);

        List<Map<String, Object>> todoList = new ArrayList<>();
        for (Todo todo : todos) {
            Map<String, Object> todoMap = new LinkedHashMap<>();
            //todoMap.put("iconId", todo.getCategoryId().getIconId()); // 카테고리의 아이콘 ID
            todoMap.put("categoryName", todo.getCategory().getCategoryName());
            todoMap.put("todoName", todo.getTodoName());
            todoMap.put("complete", todo.getComplete());
            todoList.add(todoMap);
        }

        List<Map<String, Object>> timetableList = new ArrayList<>();
        for (Timetable timetable : timetables) {
            Map<String, Object> timetableMap = new LinkedHashMap<>();
            timetableMap.put("scheduleName", timetable.getScheduleName());
            timetableMap.put("color", timetable.getColor());
            timetableMap.put("startTime", timetable.getStartTime());
            timetableMap.put("endTime", timetable.getEndTime());
            timetableMap.put("memo", timetable.getMemo());
            timetableList.add(timetableMap);
        }
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("todoList", todoList);
        data.put("timetableList", timetableList);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("data",data);
        return ResponseEntity.ok().body(result);
    }

    /**
     * 투두 통계 API
     */
    @PostMapping("/statistics")
    public ResponseEntity<Map<String, Object>> userTodoAvg(Authentication authentication, @RequestBody TodoStatisticsRequestDto todoStatisticsRequestDto) {
        User user = userRepository.findByAuthIdAndAccountExpired(authentication.getName(), false).orElseThrow(()-> new NotFoundUserException("유저를 찾을 수 없습니다."));
        Map<String,Object> map = new LinkedHashMap<>();
//        Map<String,Object> data = new LinkedHashMap<>();
//        data.put("average", todoService.calcTodoAverage(user,todoAverageRequestDto));
//        TodoAverageResponseDto todoAverageResponseDto = todoService.calcTodoAverage(user,todoAverageRequestDto);
//        TodoRepository.statisticsVO = todoService.calcTodoAverage(user,todoAverageRequestDto);
        map.put("data", todoService.calcTodoAverage(user, todoStatisticsRequestDto));
        return ResponseEntity.ok().body(map);
    }
}
