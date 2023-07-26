package com.umc.mada.todo.dto;

import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TodoResponseDto {
    private int id; // Todo ID
    private long user_id; // 유저 ID
    private int category_id; // 카테고리 ID
    private String todo_name; // Todo 이름
    private char complete; // 완료 여부 (y: 오늘 할 일 완료, n: 오늘 할 일 미완료)
    private LocalDate startRepeatDate; // 반복 시작일자
    private LocalDate endRepeatDate; // 반복 종료일자
    private boolean isRepeatMon; // 월요일 반복 여부
    private boolean isRepeatTue; // 화요일 반복 여부
    private boolean isRepeatWed; // 수요일 반복 여부
    private boolean isRepeatThu; // 목요일 반복 여부
    private boolean isRepeatFri; // 금요일 반복 여부
    private boolean isRepeatSat; // 토요일 반복 여부
    private boolean isRepeatSun; // 일요일 반복 여부
    private List<Integer> monthlyRepeatDates; // 매월 반복하는 날짜 목록
}
