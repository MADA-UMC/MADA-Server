package com.umc.mada.calendar.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.umc.mada.calendar.dto.CalendarRequestDto;
import com.umc.mada.calendar.dto.CalendarResponseDto;
import com.umc.mada.calendar.service.CalendarService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


    @PostMapping("/add") //로그인 구현 이후 토큰으로 사용
    ResponseEntity<CalendarResponseDto> calendarAdd(@RequestHeader Long uid, @RequestBody CalendarRequestDto calenderDto){
        if(calendarService.isExistCalendarName(uid,calenderDto)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }
        if(calendarService.alreadyManyDDay(uid)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        CalendarResponseDto calendarResponseDto = calendarService.createCalendar(calenderDto);
        return ResponseEntity.ok(calendarResponseDto);
    }
    @PatchMapping("/edit/{id}")
    ResponseEntity<CalendarResponseDto> calendarEdit(@PathVariable Long id, @RequestBody CalendarRequestDto calendarRequestDto){
        CalendarResponseDto calendarResponseDto = calendarService.editCalendar(id,calendarRequestDto);
        return ResponseEntity.ok(calendarResponseDto);
    }
    @DeleteMapping("/edit/{id}")
    ResponseEntity<Void> calendarDelete(@PathVariable Long id, @RequestBody CalendarRequestDto calendarRequestDto){
        calendarService.deleteCalender(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
