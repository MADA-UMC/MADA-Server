package com.umc.mada.calendar.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.umc.mada.calendar.dto.CalendarRequestDto;
import com.umc.mada.calendar.dto.CalendarResponseDto;
import com.umc.mada.calendar.service.CalendarService;
import com.umc.mada.user.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@Slf4j
@Transactional
@RequestMapping("/api/calendar")
public class CalendarController {
    private final CalendarService calendarService;
    @Autowired
    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @GetMapping("/")
    ResponseEntity<List<CalendarResponseDto>> calendarRead(Authentication authentication){
        return ResponseEntity.ok(calendarService.readCalendars(authentication));
    }

    @PostMapping("/add") //로그인 구현 이후 토큰으로 사용
    ResponseEntity<CalendarResponseDto> calendarAdd(Authentication authentication, @RequestBody CalendarRequestDto calenderDto){
        CalendarResponseDto calendarResponseDto = calendarService.createCalendar(authentication,calenderDto);
        return ResponseEntity.ok(calendarResponseDto);
    }
    @PatchMapping("/edit/{id}")
    ResponseEntity<CalendarResponseDto> calendarEdit(Authentication authentication, @PathVariable Long id, @RequestBody CalendarRequestDto calendarRequestDto){
        CalendarResponseDto calendarResponseDto = calendarService.editCalendar(authentication,id,calendarRequestDto);
        return ResponseEntity.ok(calendarResponseDto);
    }
    @DeleteMapping("/edit/{id}")
    ResponseEntity<String> calendarDelete(Authentication authentication, @PathVariable Long id){
//        if(calendarService.deleteCalendar(authentication,id)){
//            return new ResponseEntity<>(HttpStatus.OK);
//        }
//        return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        calendarService.deleteCalendar(authentication,id);
        return ResponseEntity.status(HttpStatus.OK).body("삭제 성공했습니다.");
    }
}
