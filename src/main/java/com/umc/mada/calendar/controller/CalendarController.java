package com.umc.mada.calendar.controller;

import com.umc.mada.calendar.dto.CalendarRequestDto;
import com.umc.mada.calendar.dto.CalendarResponseDto;
import com.umc.mada.calendar.service.CalendarService;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
        return ResponseEntity.ok(calendarService.readCalendars(authentication));
    }

    @PostMapping("/add") //로그인 구현 이후 토큰으로 사용
    ResponseEntity<Map<String,Object>> calendarAdd(Authentication authentication, @RequestBody CalendarRequestDto calendarDto){
        Map<String,Object> map;
        map = calendarService.createCalendar(authentication,calendarDto);
        return ResponseEntity.ok(map);
    }
    @PatchMapping("/edit/{id}")
    ResponseEntity<Map<String,Object>> calendarEdit(Authentication authentication, @PathVariable Long id, @RequestBody CalendarRequestDto calendarRequestDto){
        Map<String,Object> map = new LinkedHashMap<>();
        Map<String,Object> data ;
        data = calendarService.editCalendar(authentication,id,calendarRequestDto);
        map.put("data", data);
        return ResponseEntity.ok(map);
    }
    @DeleteMapping("/edit/{id}")
    ResponseEntity<Map<String,Object>> calendarDelete(Authentication authentication, @PathVariable Long id ){
        Map<String,Object> map = new LinkedHashMap<>();
        map.put("data",calendarService.deleteCalendar(authentication,id));
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
    @GetMapping("/?{date}")
    ResponseEntity<Map<String,Object>> readCalendarByDate(Authentication authentication, @PathVariable @DateTimeFormat(pattern = "yyyy-MM-DD") LocalDate date){
        return ResponseEntity.ok(calendarService.readDayCalendars(authentication,date));
    }

}
