package com.umc.mada.calendar.dto;

import lombok.*;
import reactor.util.annotation.Nullable;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder

@Getter
@Setter
public class CalendarRequestDto {
    @Nullable
    private Long calendarId;
    private String calendarName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String color;
    private Integer repeatInfo;
    private LocalTime startTime;
    private LocalTime endTime;
    private Character  repeat;
    private Character dday;
    private String memo;
    @Nullable
    private Boolean isExpired;
}
