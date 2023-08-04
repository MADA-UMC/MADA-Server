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
import java.util.List;
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
    public ResponseEntity<String> createTimetable(Authentication authentication, @RequestBody TimetableRequestDto timetableRequestDto){
        // 시간표 생성 API
        Optional<User> userOptional = userRepository.findByAuthId(authentication.getName());
        User user = userOptional.get();
        timetableService.createTimetable(user, timetableRequestDto);
        return new ResponseEntity<>("시간표 생성 완료", HttpStatus.CREATED);
    }

    @PatchMapping("/scheduleId/{scheduleId}")
    public ResponseEntity<TimetableResponseDto> updateTimetable(Authentication authentication, @PathVariable int scheduleId, @RequestBody TimetableRequestDto timetableRequestDto){
        // 시간표 수정 API
        try{
            Optional<User> userOptional = userRepository.findByAuthId(authentication.getName());
            User user = userOptional.get();
            TimetableResponseDto updatedTimetalbe = timetableService.updateTimetable(user, scheduleId, timetableRequestDto);
            return new ResponseEntity<>(updatedTimetalbe, HttpStatus.OK);
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/scheduleId/{scheduleId}")
    public ResponseEntity<String> deleteTimetable(Authentication authentication, @PathVariable int scheduleId) {
        // 시간표 삭제 API
        try{
            Optional<User> userOptional = userRepository.findByAuthId(authentication.getName());
            User user = userOptional.get();
            timetableService.deleteTimetable(user, scheduleId);
            return new ResponseEntity<>("시간표 삭제 완료", HttpStatus.OK);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/date/{date}")
    // 특정 유저 시간표 조회 API
    public ResponseEntity<List<TimetableResponseDto>> getUserTimetable(Authentication authentication, @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date){
        try{
            Optional<User> userOptional = userRepository.findByAuthId(authentication.getName());
            User user = userOptional.get();
            List<TimetableResponseDto> userTimetables = timetableService.getUserTimetable(user, date);
            return new ResponseEntity<>(userTimetables, HttpStatus.OK);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
