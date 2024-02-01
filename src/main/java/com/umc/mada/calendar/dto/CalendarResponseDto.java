package com.umc.mada.calendar.dto;

import lombok.*;
import org.joda.time.DateTime;

import org.springframework.web.bind.annotation.RequestBody;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder

@Getter
@Setter
public class CalendarResponseDto {
   private Long calendarId;
   private String calendarName;
   private LocalDate startDate;
   private LocalDate endDate;
   private Character dday;
   private LocalTime startTime;
   private LocalTime endTime;

   private String memo;
   private String color;

   private boolean isExpired;
}
