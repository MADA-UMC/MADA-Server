package com.umc.mada.calendar.dto;

import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CalendarRequestDto {
    private String calender_name;
    private Timestamp startDate;
    private Timestamp endDate;
    private String color;
    private String repeat;
    private Character d_day;
    private String memo;
}
