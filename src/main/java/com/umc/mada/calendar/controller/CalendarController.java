package com.umc.mada.calendar.controller;

import com.umc.mada.calendar.dto.CalendarRequestDto;
import com.umc.mada.calendar.dto.CalendarResponseDto;
import com.umc.mada.calendar.service.CalendarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.*;

@RestController
@RequestMapping("/api/calendar")
public class CalendarController {
    private final CalendarService calendarService;
    @Autowired
    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @GetMapping("/")
    ResponseEntity<Map<String,Object>> calendarRead(Authentication authentication){

        return ResponseEntity.ok(calendarService.calendarsRead(authentication));
    }

    @PostMapping("/add") //로그인 구현 이후 토큰으로 사용
    ResponseEntity<Map<String,Object>> calendarAdd(Authentication authentication, @RequestBody CalendarRequestDto calendarDto){
        Map<String,Object> map = new LinkedHashMap<>();
        Map<String,Object> data = new LinkedHashMap<>();
        CalendarResponseDto calendarResponseDto = calendarService.calendarCreate(authentication,calendarDto);
        data.put("calendars",calendarResponseDto);
        map.put("data",data);
        return ResponseEntity.ok(map);
    }
    @PatchMapping("/edit/{id}")
    ResponseEntity<Map<String,Object>> calendarEdit(Authentication authentication, @PathVariable Long id, @RequestBody CalendarRequestDto calendarRequestDto){
        Map<String,Object> map = new LinkedHashMap<>();
        Map<String,Object> data = new LinkedHashMap<>();
        map.put("data", data.put("calendars",calendarService.calendarEdit(authentication,id,calendarRequestDto)));
        return ResponseEntity.ok(map);
    }
    @DeleteMapping("/edit/{id}")
    ResponseEntity<Map<String,Object>> calendarDelete(Authentication authentication, @PathVariable Long id){
        Map<String,Object> map = new LinkedHashMap<>();
        Map<String,Object> data = new LinkedHashMap<>();
        data.put("calendars",calendarService.calendarDelete(authentication,id));
        map.put("data",data);
        return ResponseEntity.ok(map);
    }
    @GetMapping("/dday")
    ResponseEntity<Map<String,Object>> readD_days(Authentication authentication){
        return ResponseEntity.ok(calendarService.readDday(authentication));
    }
    @GetMapping("/?{year}&{month}")
    ResponseEntity<Map<String,Object>> readCalendarByMonth(Authentication authentication ,@PathVariable int year ,@PathVariable int month){
        return ResponseEntity.ok(calendarService.readMonthCalendar(authentication,year,month));
    }
    @GetMapping("/date/{date}")
    ResponseEntity<Map<String,Object>> readCalendarByDate(Authentication authentication, @PathVariable Date date){
        return ResponseEntity.ok(calendarService.calendarsReadByDate(authentication,date));
    }
    @GetMapping("/repeat")
    ResponseEntity<Map<String ,Object>> readRepeats(Authentication authentication){
        return ResponseEntity.ok(calendarService.readRepeats(authentication));
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
