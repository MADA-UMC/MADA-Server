package com.umc.mada.calendar.dto;

import lombok.*;
import org.springframework.web.bind.annotation.RequestBody;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CalendarResponseDto {
   private Long calendarId;
   private String calendarName;
   private Date startDate;
   private Date endDate;
   private Character dday;
   private LocalTime startTime;
   private LocalTime endTime;
   private String  repeat;
   private String memo;
   private String color;
   private String repeatInfo;
   private boolean isExpired;
}
