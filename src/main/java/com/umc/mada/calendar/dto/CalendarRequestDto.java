package com.umc.mada.calendar.dto;

import lombok.*;
import org.joda.time.DateTime;
import reactor.util.annotation.Nullable;

import java.sql.Date;
import org.joda.time.LocalDate;
import java.time.LocalDateTime;
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
    private LocalDate startDate;
    private LocalDate endDate;
    private String color;
    private Integer repeatInfo;
    @Nullable
    private LocalTime startTime;
    private LocalTime endTime;
    private Character  repeat;
    private Character dday;
    private String memo;
    @Nullable
    private Boolean isExpired;
}
