package com.umc.mada.user.controller;

import com.umc.mada.timetable.domain.Timetable;
import com.umc.mada.todo.domain.Todo;
import com.umc.mada.todo.dto.TodoAverageRequestDto;
import com.umc.mada.todo.dto.TodoAverageResponseDto;
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
//        System.out.println(authentication.getName());
//        return ResponseEntity.status(HttpStatus.OK).body(authentication.getName());

        //httpServletResponse 방법
        response.setHeader("Content-type", "text/plain");
        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer "+token);
        PrintWriter writer = response.getWriter();
        writer.println("ok");

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

    @Operation(description = "dsfasdf")
    @GetMapping("/signup")
    public void signupToken(HttpServletResponse response, @RequestParam String token) throws IOException {//
        response.setHeader("Content-type", "text/plain");
        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer "+token);
        PrintWriter writer = response.getWriter();
        writer.println("ok");
    }

    @Operation(description = "회원가입한 유저가 닉네임 입력하는 곳")
    @PostMapping("/signup/nickName")
    public ResponseEntity<String> signupNickName(@RequestBody Map<String, String> nickName, Authentication authentication){
        Optional<User> userOptional = userRepository.findByAuthId(authentication.getName());
        userService.nickNameSetting(nickName, userOptional.get());
//        return ResponseEntity.status(HttpStatus.OK).body("닉네임 입력 성공했습니다.");
        return ResponseEntity.ok().build();
    }

    @Operation(description = "로그아웃")
    @GetMapping("/logout")
    public ResponseEntity<String> logout(){
        //세션 삭제
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().build();
    }

    @Operation(description = "회원탈퇴")
    @DeleteMapping("/withdrawal")
    public ResponseEntity<String> withdrawal(Authentication authentication){ //@AuthenticationPrincipal CusomtUserDetails cusomtUserDetails
//        User user = cusomtUserDetails.getUser();
        Optional<User> userOptional = userRepository.findByAuthId(authentication.getName());
        userService.withdrawal(userOptional.get());
        return ResponseEntity.ok().build();
    }

//    private User findUser(Authentication authentication){
//        return userRepository.findByAuthId(authentication.getName())
//                .orElseThrow()
//    }

    /**
     * 프로필 편집창 API
     */
    @GetMapping("/profile/change")
    public ResponseEntity<Map<String, Object>>userProfile(Authentication authentication) {
        Map<String, Object> map = new HashMap<>();
        map.put("data", userService.changeProfile(authentication));
        return ResponseEntity.ok(map);
    }

    /**
     * 닉네임 변경 API
     */
    @PatchMapping("/profile/change/nickname")
    public ResponseEntity<Map<String, Object>>changeNickname(Authentication authentication,
                                                             @Validated @RequestBody NicknameRequestDto changeNicknameRequestDto) {
        Optional<User> userOptional = userRepository.findByAuthId(authentication.getName());
        User user = userOptional.get();
        Map<String, Object> map = new HashMap<>();
        map.put("data", userService.changeNickname(user, changeNicknameRequestDto));
//        if(bindingResult.hasErrors()){
//            ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();
//            return ResponseEntity
//        }
        return ResponseEntity.ok(map);
    }

    /**
     * 구독 API
     */
    @PatchMapping("/subscribe")
    public ResponseEntity<Void> userSubscribe(Authentication authentication,@RequestBody Map<String,Boolean> is_subscribe){
//        Map<String,Object> map = new HashMap<>();
//        map.put("data",new HashMap<>().put("is_subscribe",userService.userSubscribeSettings(authentication,is_subscribe)));
        userService.userSubscribeSettings(authentication,is_subscribe);
        return ResponseEntity.ok().build();
    }

    /**
     * 화면 설정 API
     */
    @PostMapping("/pageInfo/change")
    public ResponseEntity<Map<String,Object>> userPageInfo(Authentication authentication, @RequestBody Map<String,Boolean> map) {
        return ResponseEntity.ok(userService.userPageSettings(authentication, map));
    }

    /**
     * 알림 설정 API
     */
    @PatchMapping("/alarmInfo/change")
    public ResponseEntity<Map<String, Object>> userAlarmInfo(Authentication authentication, @RequestBody Map<String, Boolean> map) {
        return ResponseEntity.ok(userService.userAlarmSettings(authentication, map));
    }

    /**
     * 화면 설정 조회 API
     */
    @GetMapping("/pageInfo")
    public ResponseEntity<Map<String, Object>> pageToggleInfo(Authentication authentication, Map<String, Object> map) {
        map.put("data", userService.userPageSet(authentication, map));
        return ResponseEntity.ok(map);
    }

    /**
     * 알람 설정 조회 API
     */
    @GetMapping("/alarmInfo")
    public ResponseEntity<Map<String, Object>> alarmToggleInfo(Authentication authentication, Map<String, Object> map) {
        map.put("data", userService.userAlarmSet(authentication, map));
        return ResponseEntity.ok(map);
    }

    /**
     * 투두 일별 통계 API
     */
    @GetMapping("/statistics/day/{date}")
    public ResponseEntity<Map<String, Object>> getTodoAndTimetable(Authentication authentication, @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date){
        Optional<User> userOptional = userRepository.findByAuthId(authentication.getName());
        User user = userOptional.get();
        List<Todo> todos = todoRepository.findTodosByUserIdAndDateIs(user, date);
        List<Timetable> timetables = timetableRepository.findTimetablesByUserIdAndDateIs(user, date);

        List<Map<String, Object>> todoList = new ArrayList<>();
        for (Todo todo : todos) {
            Map<String, Object> todoMap = new LinkedHashMap<>();
            //todoMap.put("iconId", todo.getCategoryId().getIconId()); // Category의 아이콘 ID
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

    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> userTodoAvg(Authentication authentication, @RequestBody TodoAverageRequestDto todoAverageRequestDto) {
        Optional<User> userOptional = userRepository.findByAuthId(authentication.getName());
        User user = userOptional.get();
        Map<String,Object> map = new LinkedHashMap<>();
//        Map<String,Object> data = new LinkedHashMap<>();
//        data.put("average", todoService.calcTodoAverage(user,todoAverageRequestDto));
//        TodoAverageResponseDto todoAverageResponseDto = todoService.calcTodoAverage(user,todoAverageRequestDto);
//        TodoRepository.statisticsVO = todoService.calcTodoAverage(user,todoAverageRequestDto);
        map.put("data", todoService.calcTodoAverage(user,todoAverageRequestDto));
        return ResponseEntity.ok().body(map);
    }
}
