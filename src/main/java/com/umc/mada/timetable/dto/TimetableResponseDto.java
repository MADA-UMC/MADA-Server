package com.umc.mada.timetable.dto;

import com.umc.mada.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimetableResponseDto {
    //private User userId; // 유저 ID
    private LocalDate date; // 시간표 일자
    private String scheduleName; // 일정 이름
    private String color; // 일정 색상
    private LocalTime startTime; // 일정 시작 시간
    private LocalTime endTime; // 일정 종료 시간
    private String memo; // 메모
}
