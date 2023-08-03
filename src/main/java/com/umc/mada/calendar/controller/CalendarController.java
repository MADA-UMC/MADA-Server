//package com.umc.mada.calendar.controller;
//
//import com.fasterxml.jackson.databind.node.ObjectNode;
//import com.umc.mada.calendar.dto.CalendarRequestDto;
//import com.umc.mada.calendar.service.CalendarService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.bind.annotation.*;
//@RestController
//@Slf4j
//@Transactional
//@RequestMapping("/api")
//public class CalendarController {
//    private final CalendarService calendarService;
//    @Autowired
//    public CalendarController(CalendarService calendarService) {
//        this.calendarService = calendarService;
//    }
//
//    @PostMapping("/calender/add") //로그인 구현 이후 토큰으로 사용
//    ResponseEntity<ObjectNode> calenderAdd(@RequestBody CalendarRequestDto calenderDto){
//        return calendarService.createCalendar(calenderDto);
//    }
//}
