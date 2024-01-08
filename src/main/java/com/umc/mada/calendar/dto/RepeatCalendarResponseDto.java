package com.umc.mada.calendar.dto;

import lombok.*;
import reactor.util.annotation.Nullable;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder

@Getter
@Setter
public class RepeatCalendarResponseDto {
    private Long calendarId;
    private LocalDate date;
    @Nullable
    private Boolean isExpired;
}
