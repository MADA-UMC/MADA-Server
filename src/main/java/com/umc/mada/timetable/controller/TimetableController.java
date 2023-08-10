package com.umc.mada.timetable.controller;

import com.umc.mada.timetable.dto.TimetableRequestDto;
import com.umc.mada.timetable.dto.TimetableResponseDto;
import com.umc.mada.timetable.service.TimetableService;
import com.umc.mada.user.domain.User;
import com.umc.mada.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/home/time")
public class TimetableController {
    private final TimetableService timetableService;
    private final UserRepository userRepository;

    @Autowired
    public TimetableController(TimetableService timetableService, UserRepository userRepository) {
        this.timetableService = timetableService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createTimetable(Authentication authentication, @RequestBody TimetableRequestDto timetableRequestDto){
        // 시간표 생성 API
        Optional<User> userOptional = userRepository.findByAuthId(authentication.getName());
        User user = userOptional.get();
        TimetableResponseDto newTimetable = timetableService.createTimetable(user, timetableRequestDto);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", 200);
        result.put("success", true);
        result.put("message", "시간표 생성이 완료되었습니다.");
        result.put("data", newTimetable);
        return ResponseEntity.ok().body(result);
    }

    @PatchMapping("/scheduleId/{scheduleId}")
    public ResponseEntity<Map<String, Object>> updateTimetable(Authentication authentication, @PathVariable int scheduleId, @RequestBody TimetableRequestDto timetableRequestDto){
        // 시간표 수정 API
        try{
            Optional<User> userOptional = userRepository.findByAuthId(authentication.getName());
            User user = userOptional.get();
            TimetableResponseDto updatedTimetalbe = timetableService.updateTimetable(user, scheduleId, timetableRequestDto);
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("status", 200);
            result.put("success", true);
            result.put("message", "시간표 수정이 완료되었습니다.");
            result.put("data", updatedTimetalbe);
            return ResponseEntity.ok().body(result);
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/scheduleId/{scheduleId}")
    public ResponseEntity<Map<String, Object>> deleteTimetable(Authentication authentication, @PathVariable int scheduleId) {
        // 시간표 삭제 API
        try{
            Optional<User> userOptional = userRepository.findByAuthId(authentication.getName());
            User user = userOptional.get();
            timetableService.deleteTimetable(user, scheduleId);
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("status", 200);
            result.put("success", true);
            result.put("message", "시간표 삭제가 완료되었습니다.");
            return ResponseEntity.ok().body(result);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/date/{date}")
    // 특정 유저 시간표 조회 API
    public ResponseEntity<Map<String, Object>> getUserTimetable(Authentication authentication, @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date){
        try{
            Optional<User> userOptional = userRepository.findByAuthId(authentication.getName());
            User user = userOptional.get();
            List<TimetableResponseDto> userTimetables = timetableService.getUserTimetable(user, date);
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("status", 200);
            result.put("success", true);
            result.put("message", "시간표가 정상적으로 조회되었습니다.");
            result.put("data", userTimetables);
            return ResponseEntity.ok().body(result);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
