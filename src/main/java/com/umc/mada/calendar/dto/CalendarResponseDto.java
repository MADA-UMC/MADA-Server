package com.umc.mada.calendar.dto;

import lombok.*;
import org.springframework.web.bind.annotation.RequestBody;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CalendarResponseDto {
   private String calenderName;
   private Date startDate;
   private Date endDate;
   private Character dday;
   private Character repeat;
   private String memo;
   private String color;
}
