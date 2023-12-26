package com.umc.mada.timetable.dto;

import lombok.Data;
import reactor.util.annotation.Nullable;

import com.umc.mada.timetable.domain.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class TimetableRequestDto {
    private LocalDate date; // 시간표 일자
    private String scheduleName; // 일정 이름
    private String color; // 일정 색상
    private LocalTime startTime; // 일정 시작 시간
    private LocalTime endTime; // 일정 종료 시간
    private String memo; // 메모
    private String comment; // comment
    private Boolean isDeleted; // 삭제 여부
    private DayOfWeek dayOfWeek; // 주간 시간표 요일
}
