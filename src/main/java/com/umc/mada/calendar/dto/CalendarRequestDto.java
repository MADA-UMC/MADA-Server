package com.umc.mada.calendar.dto;

import lombok.*;
import reactor.util.annotation.Nullable;

import java.sql.Date;
import java.time.LocalTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CalendarRequestDto {
    @Nullable
    private Long calendarId;
    private String calendarName;
    @Nullable
    private Date startDate;
    private Date endDate;
    private String color;
    private String repeatInfo;
    @Nullable
    private LocalTime startTime;
    private LocalTime endTime;
    private Character  repeat;
    private Character dday;
    private String memo;
    @Nullable
    private Boolean isExpired;
}
