package com.umc.mada.todo.dto;

import lombok.*;
import com.umc.mada.todo.domain.Todo.Repeat;
import com.umc.mada.todo.domain.Todo.RepeatWeek;
import java.time.LocalDate;
import java.util.List;

@Data
public class TodoRequestDto {
    private long userId; // 유저 ID
    private int categoryId; // 카테고리 ID
    private String todoName; // 투두 이름
    private boolean complete; // 완료 여부 (y: 오늘 할 일 완료, n: 오늘 할 일 미완료)
    private Repeat  repeat; // 반복 설정 (N: 반복 안함, day: 매일 반복, week: 매주 반복, month: 매월 반복)
    private RepeatWeek  repeatWeek; // 매주 반복 요일 ("mon", "tue", "wed", "thu", "fri", "sat", "sun")
    private LocalDate startRepeatDate; // 반복 시작 일자
    private LocalDate endRepeatDate; // 반복 종료 일자
//    private List<Integer> monthlyRepeatDates; // 매월 반복하는 날짜 목록
}
