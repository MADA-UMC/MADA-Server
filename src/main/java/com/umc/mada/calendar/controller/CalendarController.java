package com.umc.mada.calendar.controller;

import com.umc.mada.calendar.domain.ManyD_dayException;
import com.umc.mada.calendar.domain.SameCalendarNameExist;
import com.umc.mada.calendar.dto.CalendarRequestDto;
import com.umc.mada.calendar.dto.CalendarResponseDto;
import com.umc.mada.calendar.service.CalendarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@Slf4j
@Transactional
@RequestMapping("/api/calender")
public class CalendarController {
    private final CalendarService calendarService;
    @Autowired
    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @GetMapping("/")
    ResponseEntity<Map<String,Object>> calendarRead(Authentication authentication){
        Map<String,Object> map = new HashMap<>();
        map.put("data", calendarService.calendarsRead(authentication));
        return ResponseEntity.ok(map);
    }

    @PostMapping("/add") //로그인 구현 이후 토큰으로 사용
    ResponseEntity<Map<String,Object>> calendarAdd(Authentication authentication, @RequestBody CalendarRequestDto calenderDto){
        Map<String,Object> map = new HashMap<>();
        map.put("data",calendarService.calendarCreate(authentication,calenderDto));
        return ResponseEntity.ok(map);
    }
    @PatchMapping("/edit/{id}")
    ResponseEntity<Map<String,Object>> calendarEdit(Authentication authentication, @PathVariable Long id, @RequestBody CalendarRequestDto calendarRequestDto){
        Map<String,Object> map = new HashMap<>();
        map.put("data", calendarService.calendarEdit(authentication,id,calendarRequestDto));
        return ResponseEntity.ok(map);
    }
    @DeleteMapping("/edit/{id}")
    ResponseEntity<Map<String,Object>> calendarDelete(Authentication authentication, @PathVariable Long id){
        Map<String,Object> map = new HashMap<>();
        map.put("data",calendarService.calendarDelete(authentication,id));
        return ResponseEntity.ok(map);
    }
    @GetMapping("/dday")
    ResponseEntity<Map<String,Object>> readD_days(Authentication authentication){
        Map<String,Object> map = new HashMap<>();
        map.put("data",calendarService.readDday(authentication));
        return ResponseEntity.ok(map);
    }
    @GetMapping("/{month}")
    ResponseEntity<Map<String,Object>> readCalendarByMonth(Authentication authentication ,@PathVariable int month){
        Map<String ,Object> map = new HashMap<>();
        map.put("data",calendarService.readMonthCalendar(authentication,month));
        return ResponseEntity.ok(map);
    }
    @GetMapping("/date/{date}")
    ResponseEntity<Map<String,Object>> readCalendarByDate(Authentication authentication, @PathVariable Date date){
        Map<String ,Object> map = new HashMap<>();
        map.put("data",calendarService.calendarsReadByDate(authentication,date));
        return ResponseEntity.ok(map);
    }

    /*@ExceptionHandler(ManyD_dayException.class)
    public ResponseEntity<String> manyD_dayExceptionHandler(ManyD_dayException manyDDayException){

    }
    @ExceptionHandler(SameCalendarNameExist.class)
    public ResponseEntity<String> sameCalendarNameExceptionHandler(SameCalendarNameExist sameCalendarNameExist){

    }
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> noSuchExceptionHandler(NoSuchElementException noSuchElementException){

    }*/
}
