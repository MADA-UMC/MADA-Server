package com.umc.mada.calendar.dto;

import lombok.*;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder

@Getter
@Setter
public class RepeatCalendarResponseDto {
    private Long calendarId;
    private LocalDate date;
}
