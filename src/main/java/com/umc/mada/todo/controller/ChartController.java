package com.umc.mada.todo.controller;

import com.umc.mada.todo.service.ChartService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.YearMonth;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chart")
public class ChartController {
    private final ChartService chartService;

    @GetMapping("/day")
    public ResponseEntity<?> dailyStatistics(Authentication authentication, @RequestParam(value = "date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date){
        return ResponseEntity.ok().body(chartService.dailyStatistics(authentication, date));
    }

    @GetMapping("/week")
    public ResponseEntity<?> weeklyStatistics(Authentication authentication, @RequestParam(value = "date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date){
        return ResponseEntity.ok().body(chartService.weeklyStatistics(authentication, date));
    }

    @GetMapping("/month")
    public ResponseEntity<?> monthlyStatistics(Authentication authentication, @RequestParam(value = "date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date){
        return ResponseEntity.ok().body(chartService.monthlyStatistics(authentication, date));
    }

    @GetMapping("/checkTodos")
    public ResponseEntity<?> getTodoDatesByMonth(Authentication authentication, @RequestParam(value = "yearMonth") YearMonth yearMonth){
        return ResponseEntity.ok().body(chartService.findDatesWithTodosByMonth(authentication, yearMonth));
    }
}