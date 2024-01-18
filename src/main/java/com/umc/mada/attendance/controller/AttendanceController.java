package com.umc.mada.attendance.controller;

import com.umc.mada.attendance.service.AttendanceService;
import com.umc.mada.todo.dto.TodoResponseDto;
import com.umc.mada.user.domain.User;
import com.umc.mada.user.repository.UserRepository;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {
    private final AttendanceService attendanceService;
    private final UserRepository userRepository;

    @Autowired
    public AttendanceController(AttendanceService attendanceService, UserRepository userRepository) {
        this.attendanceService = attendanceService;
        this.userRepository = userRepository;
    }


//    @PatchMapping("/user/attendance")
//    public ResponseEntity<Map<String, Object>> checkAttendance(Authentication authentication) {
//        Optional<User> userOptional = userRepository.findByAuthId(authentication.getName());
//        User user = userOptional.get();
//        TodoResponseDto updatedTodo = todoService.updateTodo(user);
//        Map<String, Object> data = new LinkedHashMap<>();
//        data.put("Todo", updatedTodo);
//        Map<String, Object> result = new LinkedHashMap<>();
//
//        result.put("data", data);
//        return ResponseEntity.ok().body(result);
//    }
//
//    @GetMapping("/my")
//    public ResponseEntity<Map<String, Object>> myPageList(Authentication authentication) {
//        Map<String, Object> map = new HashMap<>();
//        map.put("data", myService.findMyProfileList(authentication));
//        return ResponseEntity.ok(map);
//    }

//    @Autowired
//    public AttendanceController(AttendanceService attendanceService) {
//        this.attendanceService = attendanceService;
//    }
//
//    @PostMapping("/mark")
//    public ResponseEntity<String> markAttendance(@RequestParam Long userId) {
//
//        User user = userService.getUserById(userId);
//        if (user == null) {
//            return ResponseEntity.badRequest().body("User not found");
//        }
//
//        attendanceService.markAttendance(user);
//        return ResponseEntity.ok("Attendance marked successfully");
//    }
//
//    @GetMapping("/count")
//    public ResponseEntity<Integer> getAttendanceCount(@RequestParam Long userId) {
//        User user = userService.getUserById(userId);
//        if (user == null) {
//            return ResponseEntity.badRequest().body("User not found");
//        }
//
//        int attendanceCount = attendanceService.getAttendanceCountForMonth(user, LocalDate.now().getMonthValue());
//        return ResponseEntity.ok(attendanceCount);
//    }
}