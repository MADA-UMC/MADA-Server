package com.umc.mada.calendar.dto;

import lombok.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CalendarRequestDto {
    private Long calendarId;
    private String calendarName;
    private Date startDate;
    private Date endDate;
    private String color;
    private String  repeat;
    private Character dday;
    private String memo;
}
